package com.springddd.infrastructure.persistence.leaf;

import com.springddd.domain.leaf.service.LeafSegmentIdGenerateDomainService;
import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import com.springddd.infrastructure.persistence.r2dbc.LeafAllocRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
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

import static org.springframework.data.relational.core.query.Criteria.where;

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

    private Mono<Long> doNextId(LeafSegmentBuffer buffer, String bizTag) {
        return Mono.fromCallable(() -> {
            LeafSegment segment = buffer.getCurrent();
            long value = segment.getValue().getAndIncrement();

            if (value < segment.getMax()) {
                return value;
            }

            buffer.wLock().lock();
            try {
                segment = buffer.getCurrent();
                value = segment.getValue().getAndIncrement();
                if (value < segment.getMax()) {
                    return value;
                }

                if (buffer.isNextReady()) {
                    buffer.switchPos();
                    buffer.setNextReady(false);
                } else {
                    // Next buffer not ready, wait for it
                    long start = System.currentTimeMillis();
                    while (!buffer.isNextReady()) {
                        if (System.currentTimeMillis() - start > 5000) {
                            throw new RuntimeException("Leaf segment buffer not ready for bizTag: " + bizTag);
                        }
                        Thread.sleep(10);
                    }
                    buffer.switchPos();
                    buffer.setNextReady(false);
                }

                segment = buffer.getCurrent();
                return segment.getValue().getAndIncrement();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for segment buffer", e);
            } finally {
                buffer.wLock().unlock();
            }
        }).subscribeOn(Schedulers.boundedElastic())
                .doOnNext(id -> {
                    // Async preload next segment if needed
                    if (buffer.getCurrent().getStep() > 0) {
                        long threshold = buffer.getCurrent().getMax() - buffer.getCurrent().getStep() / 2;
                        if (buffer.getCurrent().getValue().get() >= threshold && !buffer.isNextReady() && buffer.getThreadRunning().compareAndSet(false, true)) {
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
                .flatMap(entity -> {
                    int step = entity.getStep();
                    long oldMaxId = entity.getMaxId();
                    long newMaxId = oldMaxId + step;

                    // Dynamic step logic
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
                                target.getValue().set(oldMaxId);
                                target.setMax(finalNewMaxId);
                                target.setStep(finalStep);

                                buffer.setStepUpdateTime(System.currentTimeMillis());
                                if (buffer.getMinStep() == 0) {
                                    buffer.setMinStep(entity.getStep());
                                }

                                if (!isInit) {
                                    buffer.setNextReady(true);
                                }
                                log.debug("Updated segment for bizTag={}, oldMaxId={}, newMaxId={}, step={}",
                                        bizTag, oldMaxId, finalNewMaxId, finalStep);
                            })
                            .then();
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Leaf alloc not found for bizTag: " + bizTag)));
    }

    public Map<String, LeafSegmentBuffer> getBufferCache() {
        return bufferCache;
    }

    public Mono<List<LeafAllocEntity>> findAllAlloc() {
        return leafAllocRepository.findAll().collectList();
    }
}
