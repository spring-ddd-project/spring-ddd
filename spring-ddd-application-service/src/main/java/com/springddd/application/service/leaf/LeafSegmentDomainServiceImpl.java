package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.exception.LeafAllocKeyNotExistsException;
import com.springddd.domain.leaf.LeafSegmentDomainService;
import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import com.springddd.infrastructure.persistence.r2dbc.LeafAllocRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class LeafSegmentDomainServiceImpl implements LeafSegmentDomainService {

    private final LeafAllocRepository leafAllocRepository;
    private final LeafSegmentTransactionalService transactionalService;
    private final ConcurrentHashMap<String, SegmentBuffer> cache = new ConcurrentHashMap<>();
    private volatile boolean initOK = false;

    private static final int MAX_STEP = 1_000_000;
    private static final long SEGMENT_DURATION = 15 * 60 * 1000L;

    @PostConstruct
    public void initCache() {
        updateCacheFromDb()
                .doOnSuccess(v -> initOK = true)
                .subscribe();

        Flux.interval(java.time.Duration.ofMinutes(1))
                .flatMap(i -> updateCacheFromDb())
                .onErrorContinue((err, obj) -> {
                })
                .subscribe();
    }

    @Override
    public Mono<Long> getId(String bizTag) {
        if (!initOK) {
            return Mono.error(new IllegalStateException("Leaf segment cache not initialized"));
        }
        SegmentBuffer buffer = cache.get(bizTag);
        if (buffer == null) {
            return Mono.error(new LeafAllocKeyNotExistsException(bizTag));
        }
        return getIdFromSegmentBuffer(buffer);
    }

    @Override
    public Mono<Void> init() {
        return updateCacheFromDb().doOnSuccess(v -> initOK = true);
    }

    @Override
    public Mono<Boolean> isInitialized() {
        return Mono.just(initOK);
    }

    @Override
    public Mono<Map<String, Object>> getCacheStatus() {
        return Mono.fromCallable(() -> {
            Map<String, Object> result = new LinkedHashMap<>();
            for (Map.Entry<String, SegmentBuffer> entry : cache.entrySet()) {
                SegmentBuffer buffer = entry.getValue();
                Map<String, Object> bufferMap = new LinkedHashMap<>();
                bufferMap.put("key", buffer.getKey());
                bufferMap.put("currentPos", buffer.getCurrentPos());
                bufferMap.put("nextReady", buffer.isNextReady());
                bufferMap.put("initOk", buffer.isInitOk());
                bufferMap.put("threadRunning", buffer.isThreadRunning());
                bufferMap.put("step", buffer.getStep());
                bufferMap.put("minStep", buffer.getMinStep());
                bufferMap.put("updateTimestamp", buffer.getUpdateTimestamp());

                List<Map<String, Object>> segmentList = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    Segment segment = buffer.getSegments()[i];
                    Map<String, Object> segMap = new LinkedHashMap<>();
                    segMap.put("index", i);
                    segMap.put("value", segment.getValue().get());
                    segMap.put("max", segment.getMax());
                    segMap.put("step", segment.getStep());
                    segMap.put("idle", segment.getIdle());
                    segmentList.add(segMap);
                }
                bufferMap.put("segments", segmentList);
                result.put(entry.getKey(), bufferMap);
            }
            return result;
        });
    }

    private Mono<Long> getIdFromSegmentBuffer(SegmentBuffer buffer) {
        if (!buffer.isInitOk()) {
            synchronized (buffer) {
                if (!buffer.isInitOk()) {
                    return doUpdateSegmentFromDb(buffer.getKey(), buffer.getCurrent())
                            .doOnSuccess(v -> buffer.setInitOk(true))
                            .then(Mono.defer(() -> doGetId(buffer)));
                }
            }
        }
        return doGetId(buffer);
    }

    private Mono<Long> doGetId(SegmentBuffer buffer) {
        return Mono.fromCallable(() -> {
            while (true) {
                Segment segment = buffer.getCurrent();
                long value = segment.getValue().getAndIncrement();
                if (value < segment.getMax()) {
                    maybePreload(buffer, segment);
                    return value;
                }

                waitForNextSegment(buffer);

                synchronized (buffer) {
                    segment = buffer.getCurrent();
                    value = segment.getValue().getAndIncrement();
                    if (value < segment.getMax()) {
                        return value;
                    }
                    if (buffer.isNextReady()) {
                        buffer.switchPos();
                        buffer.setNextReady(false);
                    } else {
                        throw new IllegalStateException("Both segments not ready for " + buffer.getKey());
                    }
                }
            }
        });
    }

    private void maybePreload(SegmentBuffer buffer, Segment current) {
        if (!buffer.isNextReady()
                && current.getIdle() < 0.9 * current.getStep()
                && buffer.startThreadRunning()) {

            preloadNextSegment(buffer)
                    .subscribeOn(Schedulers.boundedElastic())
                    .doOnTerminate(buffer::stopThreadRunning)
                    .subscribe();
        }
    }

    private Mono<Void> preloadNextSegment(SegmentBuffer buffer) {
        return Mono.defer(() -> {
            Segment next = buffer.getSegments()[buffer.nextPos()];
            return doUpdateSegmentFromDb(buffer.getKey(), next);
        });
    }

    private Mono<Void> doUpdateSegmentFromDb(String key, Segment segment) {
        return leafAllocRepository.findByBizTag(key)
                .flatMap(entity -> {
                    int nextStep = calculateNextStep(key, entity);
                    return transactionalService.updateMaxIdAndGet(key, nextStep)
                            .flatMap(updated -> {
                                SegmentBuffer buffer = segment.getBuffer();
                                long value = updated.getMaxId() - nextStep;
                                segment.getValue().set(value);
                                segment.setMax(updated.getMaxId());
                                segment.setStep(nextStep);
                                buffer.setStep(nextStep);
                                buffer.setMinStep(entity.getStep());
                                buffer.setUpdateTimestamp(System.currentTimeMillis());
                                return Mono.empty();
                            });
                });
    }

    private int calculateNextStep(String key, LeafAllocEntity entity) {
        SegmentBuffer buffer = cache.get(key);
        if (buffer == null) {
            return entity.getStep();
        }
        if (!buffer.isInitOk() || buffer.getUpdateTimestamp() == 0) {
            return entity.getStep();
        }
        long duration = System.currentTimeMillis() - buffer.getUpdateTimestamp();
        int nextStep = buffer.getStep();
        if (duration < SEGMENT_DURATION) {
            if (nextStep * 2 <= MAX_STEP) {
                nextStep = nextStep * 2;
            }
        } else if (duration < SEGMENT_DURATION * 2) {
            // do nothing
        } else {
            nextStep = nextStep / 2 >= buffer.getMinStep() ? nextStep / 2 : nextStep;
        }
        return nextStep;
    }

    private void waitForNextSegment(SegmentBuffer buffer) {
        int roll = 0;
        while (buffer.isThreadRunning()) {
            roll++;
            if (roll > 10000) {
                break;
            }
        }
    }

    private Mono<Void> updateCacheFromDb() {
        return leafAllocRepository.findAllBizTags()
                .collectList()
                .doOnNext(dbTags -> {
                    for (String tag : dbTags) {
                        cache.computeIfAbsent(tag, SegmentBuffer::new);
                    }
                    cache.keySet().retainAll(dbTags);
                })
                .then();
    }

    public static class SegmentBuffer {
        private final Segment[] segments = new Segment[2];
        private final AtomicInteger currentPos = new AtomicInteger(0);
        private final AtomicBoolean nextReady = new AtomicBoolean(false);
        private final AtomicBoolean threadRunning = new AtomicBoolean(false);
        private volatile boolean initOk = false;
        private volatile int step = 0;
        private volatile int minStep = 0;
        private volatile long updateTimestamp = 0;
        private final String key;

        public SegmentBuffer(String key) {
            this.key = key;
            segments[0] = new Segment(this);
            segments[1] = new Segment(this);
        }

        public String getKey() {
            return key;
        }

        public Segment[] getSegments() {
            return segments;
        }

        public Segment getCurrent() {
            return segments[currentPos.get()];
        }

        public int getCurrentPos() {
            return currentPos.get();
        }

        public int nextPos() {
            return (currentPos.get() + 1) % 2;
        }

        public void switchPos() {
            currentPos.set(nextPos());
        }

        public boolean isInitOk() {
            return initOk;
        }

        public void setInitOk(boolean initOk) {
            this.initOk = initOk;
        }

        public boolean isNextReady() {
            return nextReady.get();
        }

        public void setNextReady(boolean nextReady) {
            this.nextReady.set(nextReady);
        }

        public boolean isThreadRunning() {
            return threadRunning.get();
        }

        public boolean startThreadRunning() {
            return threadRunning.compareAndSet(false, true);
        }

        public void stopThreadRunning() {
            threadRunning.set(false);
        }

        public int getStep() {
            return step;
        }

        public void setStep(int step) {
            this.step = step;
        }

        public int getMinStep() {
            return minStep;
        }

        public void setMinStep(int minStep) {
            this.minStep = minStep;
        }

        public long getUpdateTimestamp() {
            return updateTimestamp;
        }

        public void setUpdateTimestamp(long updateTimestamp) {
            this.updateTimestamp = updateTimestamp;
        }
    }

    public static class Segment {
        private final AtomicLong value = new AtomicLong(0);
        private volatile long max = 0;
        private volatile int step = 0;
        private final SegmentBuffer buffer;

        public Segment(SegmentBuffer buffer) {
            this.buffer = buffer;
        }

        public AtomicLong getValue() {
            return value;
        }

        public long getMax() {
            return max;
        }

        public void setMax(long max) {
            this.max = max;
        }

        public int getStep() {
            return step;
        }

        public void setStep(int step) {
            this.step = step;
        }

        public SegmentBuffer getBuffer() {
            return buffer;
        }

        public long getIdle() {
            return this.max - value.get();
        }
    }
}
