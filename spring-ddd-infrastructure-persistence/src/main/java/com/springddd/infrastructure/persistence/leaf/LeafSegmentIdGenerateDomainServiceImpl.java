package com.springddd.infrastructure.persistence.leaf;

import com.springddd.domain.leaf.service.LeafSegmentIdGenerateDomainService;
import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import com.springddd.infrastructure.persistence.r2dbc.LeafAllocRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static org.springframework.data.relational.core.query.Criteria.where;

/**
 * Leaf 分段 ID 生成器实现。
 *
 * <p>使用 {@link DisruptorLock}（Disruptor Sequence + LockSupport parkNanos）
 * 替代 {@link java.util.concurrent.locks.ReentrantLock} + {@link Thread#onSpinWait()}，
 * 实现无锁、非阻塞的 segment 切换。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeafSegmentIdGenerateDomainServiceImpl implements LeafSegmentIdGenerateDomainService {

    private final LeafAllocRepository leafAllocRepository;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final Map<String, LeafSegmentBuffer> bufferCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        loadAllTags().subscribe();
    }

    private Mono<Void> loadAllTags() {
        return leafAllocRepository.findAll()
                .map(LeafAllocEntity::getBizTag)
                .collectList()
                .flatMapIterable(tags -> tags)
                .doOnNext(tag -> bufferCache.computeIfAbsent(tag, LeafSegmentBuffer::new))
                .then();
    }

    @Override
    public Mono<Long> nextId(String bizTag) {
        LeafSegmentBuffer buffer = bufferCache.computeIfAbsent(bizTag, LeafSegmentBuffer::new);

        if (!buffer.isInitOk()) {
            return initBuffer(buffer, bizTag).then(doNextId(buffer, bizTag));
        }

        return doNextId(buffer, bizTag);
    }

    /**
     * 核心分配逻辑：全程无锁，使用 Disruptor Sequence CAS + LockSupport parkNanos。
     */
    private Mono<Long> doNextId(LeafSegmentBuffer buffer, String bizTag) {
        return Mono.fromCallable(() -> {
            DisruptorLock lock = buffer.getDisruptorLock();

            while (true) {
                long claimed = lock.tryClaim();

                // 快路径：claimed 仍在当前 segment 范围内
                if (lock.isAvailable(claimed)) {
                    return claimed;
                }

                // 慢路径：segment 耗尽，尝试切换
                if (buffer.isNextReady()) {
                    int expectedPos = buffer.getCurrentPos();
                    int nextPos = buffer.nextPos();

                    // CAS 竞争：只有一个线程能成功切换 segment
                    if (buffer.casCurrentPos(expectedPos, nextPos)) {
                        LeafSegment newSeg = buffer.getCurrent();
                        lock.switchToSegment(newSeg.getStart(), newSeg.getMax());
                        buffer.setNextReady(false);
                    }
                    // 无论 CAS 成败，都重试分配
                    continue;
                }

                // next segment 未就绪：Disruptor 风格非阻塞等待
                long timeoutNanos = TimeUnit.SECONDS.toNanos(5);
                boolean ready = DisruptorLock.waitFor(buffer::isNextReady, timeoutNanos);
                if (!ready) {
                    throw new RuntimeException("Leaf segment buffer not ready for bizTag: " + bizTag);
                }
                // 等待成功后重试
            }
        }).subscribeOn(Schedulers.boundedElastic())
                .doOnNext(id -> {
                    LeafSegment current = buffer.getCurrent();
                    if (current.getStep() > 0) {
                        long threshold = current.getMax() - current.getStep() / 2;
                        if (buffer.getDisruptorLock().getCursor() + 1 >= threshold
                                && !buffer.isNextReady()
                                && buffer.getThreadRunning().compareAndSet(false, true)) {
                            preloadNextSegment(buffer, bizTag)
                                    .doFinally(sig -> buffer.getThreadRunning().set(false))
                                    .subscribeOn(Schedulers.boundedElastic())
                                    .subscribe();
                        }
                    }
                });
    }

    private Mono<Void> initBuffer(LeafSegmentBuffer buffer, String bizTag) {
        return updateMaxIdFromDb(buffer, bizTag, true);
    }

    private Mono<Void> preloadNextSegment(LeafSegmentBuffer buffer, String bizTag) {
        return updateMaxIdFromDb(buffer, bizTag, false);
    }

    @Transactional(rollbackFor = Exception.class)
    Mono<Void> updateMaxIdFromDb(LeafSegmentBuffer buffer, String bizTag, boolean isInit) {
        return r2dbcEntityTemplate.selectOne(
                        Query.query(where("biz_tag").is(bizTag)),
                        LeafAllocEntity.class)
                .switchIfEmpty(Mono.error(new RuntimeException("Leaf alloc not found for bizTag: " + bizTag)))
                .flatMap(entity -> {
                    int step = entity.getStep();
                    long oldMaxId = entity.getMaxId();

                    if (!isInit && buffer.getStepUpdateTime() > 0) {
                        long duration = System.currentTimeMillis() - buffer.getStepUpdateTime();
                        if (duration < 15 * 60 * 1000L) {
                            step = Math.min(step * 2, 1000000);
                        } else if (duration > 30 * 60 * 1000L) {
                            step = Math.max(step / 2, buffer.getMinStep() > 0 ? buffer.getMinStep() : 1);
                        }
                    }

                    final int finalStep = step;
                    final long finalNewMaxId = oldMaxId + finalStep;

                    return r2dbcEntityTemplate.update(
                                    Query.query(where("biz_tag").is(bizTag)),
                                    Update.update("max_id", finalNewMaxId)
                                            .set("step", finalStep)
                                            .set("update_time", LocalDateTime.now()),
                                    LeafAllocEntity.class)
                            .doOnSuccess(result -> {
                                LeafSegment target = isInit ? buffer.getCurrent() : buffer.getNext();
                                target.setStart(oldMaxId);
                                target.setMax(finalNewMaxId);
                                target.setStep(finalStep);

                                buffer.setStepUpdateTime(System.currentTimeMillis());
                                if (buffer.getMinStep() == 0) {
                                    buffer.setMinStep(entity.getStep());
                                }

                                if (!isInit) {
                                    buffer.setNextReady(true);
                                } else {
                                    buffer.getDisruptorLock().init(oldMaxId, finalNewMaxId);
                                }
                                log.debug("Updated segment for bizTag={}, oldMaxId={}, newMaxId={}, step={}",
                                        bizTag, oldMaxId, finalNewMaxId, finalStep);
                            })
                            .then();
                });
    }

    public Map<String, LeafSegmentBuffer> getBufferCache() {
        return bufferCache;
    }

    public Mono<List<LeafAllocEntity>> findAllAlloc() {
        return leafAllocRepository.findAll().collectList();
    }
}
