package com.springddd.infrastructure.persistence.leaf;

import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import com.springddd.infrastructure.persistence.r2dbc.LeafAllocRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeafSegmentIdGenerateDomainServiceImplTest {

    @Mock
    private LeafAllocRepository leafAllocRepository;

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    private LeafSegmentIdGenerateDomainServiceImpl service;

    @BeforeEach
    void setUp() {
        service = spy(new LeafSegmentIdGenerateDomainServiceImpl(leafAllocRepository, r2dbcEntityTemplate));
    }

    @Test
    void nextId_shouldReturnId_whenBufferNotInitialized() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.getCurrent().setStep(100);
        buffer.getCurrent().setMax(200);
        buffer.getCurrent().setStart(100);
        buffer.getDisruptorLock().init(100, 200);
        service.getBufferCache().put("test", buffer);

        StepVerifier.create(service.nextId("test"))
                .expectNext(100L)
                .verifyComplete();
    }

    @Test
    void nextId_shouldReturnId_whenBufferInitializedAndIdInRange() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.getCurrent().setStep(100);
        buffer.getCurrent().setMax(200);
        buffer.getCurrent().setStart(100);
        buffer.getDisruptorLock().init(100, 200);
        buffer.getThreadRunning().set(true);
        service.getBufferCache().put("test", buffer);

        StepVerifier.create(service.nextId("test"))
                .expectNext(100L)
                .verifyComplete();
    }

    @Test
    void nextId_shouldSwitchSegment_whenNextReady() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.getCurrent().setStep(100);
        buffer.getCurrent().setMax(200);
        buffer.getCurrent().setStart(100);
        buffer.getDisruptorLock().init(200, 200); // current 已耗尽
        buffer.setNextReady(true);
        buffer.getNext().setStep(100);
        buffer.getNext().setMax(300);
        buffer.getNext().setStart(200);
        buffer.getThreadRunning().set(true);
        service.getBufferCache().put("test", buffer);

        StepVerifier.create(service.nextId("test"))
                .expectNext(200L)
                .verifyComplete();

        assertFalse(buffer.isNextReady());
        assertEquals(1, buffer.getCurrentPos());
    }

    @Test
    void nextId_shouldThrowException_whenNextBufferNotReadyAndTimeout() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.getCurrent().setStep(100);
        buffer.getCurrent().setMax(200);
        buffer.getCurrent().setStart(100);
        buffer.getDisruptorLock().init(200, 200); // current 已耗尽
        buffer.setNextReady(false);
        buffer.getThreadRunning().set(true);
        service.getBufferCache().put("test", buffer);

        StepVerifier.create(service.nextId("test"))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void getBufferCache_shouldReturnMap() {
        Map<String, LeafSegmentBuffer> cache = service.getBufferCache();
        assertNotNull(cache);
    }

    @Test
    void findAllAlloc_shouldDelegateToRepository() {
        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setId(1L);
        when(leafAllocRepository.findAll()).thenReturn(Flux.just(entity));

        StepVerifier.create(service.findAllAlloc())
                .assertNext(list -> {
                    assertEquals(1, list.size());
                    assertEquals(1L, list.get(0).getId());
                })
                .verifyComplete();
    }

    @Test
    void init_shouldLoadAllTags() {
        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setBizTag("test_tag");
        when(leafAllocRepository.findAll()).thenReturn(Flux.just(entity));

        service.init();

        assertTrue(service.getBufferCache().containsKey("test_tag"));
    }

    // ---------- nextId 未覆盖分支补充 ----------

    @Test
    void nextId_shouldInitAndReturnId_whenBufferTrulyNotInitialized() {
        doAnswer(invocation -> {
            LeafSegmentBuffer buf = invocation.getArgument(0);
            buf.getCurrent().setStep(100);
            buf.getCurrent().setMax(200);
            buf.getCurrent().setStart(100);
            buf.getDisruptorLock().init(100, 200);
            return Mono.empty();
        }).when(service).updateMaxIdFromDb(any(), anyString(), anyBoolean());

        StepVerifier.create(service.nextId("uninitialized_tag"))
                .expectNext(100L)
                .verifyComplete();
    }

    @Test
    void nextId_shouldRetryAndReturnId_whenCasSwitchFails() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.getCurrent().setStep(100);
        buffer.getCurrent().setMax(200);
        buffer.getCurrent().setStart(100);
        buffer.getDisruptorLock().init(200, 200); // current 已耗尽
        buffer.setNextReady(true);
        buffer.getNext().setStep(100);
        buffer.getNext().setMax(300);
        buffer.getNext().setStart(200);
        buffer.getThreadRunning().set(true);
        service.getBufferCache().put("test", buffer);

        // 手动切一次位置，模拟另一个线程已经切换过
        buffer.switchPos();
        buffer.setNextReady(false);
        buffer.getDisruptorLock().switchToSegment(200, 300);

        StepVerifier.create(service.nextId("test"))
                .expectNext(200L)
                .verifyComplete();
    }

    @Test
    void nextId_shouldSwitchSegment_whenNextBecomesReadyDuringWait() throws Exception {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.getCurrent().setStep(100);
        buffer.getCurrent().setMax(200);
        buffer.getCurrent().setStart(100);
        buffer.getDisruptorLock().init(200, 200); // current 已耗尽
        buffer.setNextReady(false);
        buffer.getThreadRunning().set(true);
        service.getBufferCache().put("test", buffer);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<Long> future = executor.submit(() -> service.nextId("test").block());
            Thread.sleep(500);
            buffer.getNext().setStep(100);
            buffer.getNext().setMax(300);
            buffer.getNext().setStart(200);
            buffer.setNextReady(true);

            Long result = future.get(10, TimeUnit.SECONDS);
            assertEquals(200L, result);
            assertFalse(buffer.isNextReady());
            assertEquals(1, buffer.getCurrentPos());
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    void nextId_shouldNotTriggerPreload_whenStepIsNotPositive() {
        LeafSegmentBuffer buffer = spy(new LeafSegmentBuffer("test"));
        LeafSegment segment = new LeafSegment();
        segment.setStep(0);
        segment.setMax(200);
        segment.setStart(100);
        buffer.getDisruptorLock().init(100, 200);
        when(buffer.isInitOk()).thenReturn(true);
        when(buffer.getCurrent()).thenReturn(segment);
        buffer.getThreadRunning().set(true);
        service.getBufferCache().put("test", buffer);

        StepVerifier.create(service.nextId("test"))
                .expectNext(100L)
                .verifyComplete();
    }

    @Test
    void nextId_shouldTriggerPreload_whenThresholdReached() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.getCurrent().setStep(100);
        buffer.getCurrent().setMax(200);
        buffer.getCurrent().setStart(100);
        buffer.getDisruptorLock().init(150, 200); // cursor=149，threshold=150
        buffer.setNextReady(false);
        buffer.getThreadRunning().set(false);
        service.getBufferCache().put("test", buffer);

        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setBizTag("test");
        entity.setStep(100);
        entity.setMaxId(1000L);

        when(r2dbcEntityTemplate.selectOne(any(), eq(LeafAllocEntity.class)))
                .thenReturn(Mono.just(entity));

        StepVerifier.create(service.nextId("test"))
                .expectNext(150L)
                .verifyComplete();
    }

    // ---------- updateMaxIdFromDb 全覆盖测试 ----------

    @Test
    void updateMaxIdFromDb_shouldInitializeBuffer_whenEntityFound() {
        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setBizTag("test");
        entity.setStep(100);
        entity.setMaxId(1000L);

        when(r2dbcEntityTemplate.selectOne(any(), eq(LeafAllocEntity.class)))
                .thenReturn(Mono.just(entity));
        when(r2dbcEntityTemplate.update(any(), any(), eq(LeafAllocEntity.class)))
                .thenReturn(Mono.just(1L));

        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");

        StepVerifier.create(service.updateMaxIdFromDb(buffer, "test", true))
                .verifyComplete();

        assertEquals(1000L, buffer.getCurrent().getStart());
        assertEquals(1100L, buffer.getCurrent().getMax());
        assertEquals(100, buffer.getCurrent().getStep());
        assertEquals(100, buffer.getMinStep());
        assertTrue(buffer.getStepUpdateTime() > 0);
        assertEquals(1000L, buffer.getDisruptorLock().getCursor() + 1);
    }

    @Test
    void updateMaxIdFromDb_shouldInitializeBuffer_whenStepUpdateTimePositive() {
        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setBizTag("test");
        entity.setStep(100);
        entity.setMaxId(1000L);

        when(r2dbcEntityTemplate.selectOne(any(), eq(LeafAllocEntity.class)))
                .thenReturn(Mono.just(entity));
        when(r2dbcEntityTemplate.update(any(), any(), eq(LeafAllocEntity.class)))
                .thenReturn(Mono.just(1L));

        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.setStepUpdateTime(System.currentTimeMillis());

        StepVerifier.create(service.updateMaxIdFromDb(buffer, "test", true))
                .verifyComplete();

        assertEquals(1000L, buffer.getCurrent().getStart());
    }

    @Test
    void updateMaxIdFromDb_shouldPreloadNextSegment_whenStepUpdateTimeIsZero() {
        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setBizTag("test");
        entity.setStep(100);
        entity.setMaxId(1000L);

        when(r2dbcEntityTemplate.selectOne(any(), eq(LeafAllocEntity.class)))
                .thenReturn(Mono.just(entity));
        when(r2dbcEntityTemplate.update(any(), any(), eq(LeafAllocEntity.class)))
                .thenReturn(Mono.just(1L));

        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");

        StepVerifier.create(service.updateMaxIdFromDb(buffer, "test", false))
                .verifyComplete();

        assertEquals(1000L, buffer.getNext().getStart());
        assertEquals(1100L, buffer.getNext().getMax());
        assertTrue(buffer.isNextReady());
    }

    @Test
    void updateMaxIdFromDb_shouldDoubleStep_whenDurationLessThan15Minutes() {
        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setBizTag("test");
        entity.setStep(100);
        entity.setMaxId(1000L);

        when(r2dbcEntityTemplate.selectOne(any(), eq(LeafAllocEntity.class)))
                .thenReturn(Mono.just(entity));
        when(r2dbcEntityTemplate.update(any(), any(), eq(LeafAllocEntity.class)))
                .thenReturn(Mono.just(1L));

        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.setStepUpdateTime(System.currentTimeMillis() - 5 * 60 * 1000L);
        buffer.setMinStep(50);

        StepVerifier.create(service.updateMaxIdFromDb(buffer, "test", false))
                .verifyComplete();

        assertEquals(1000L, buffer.getNext().getStart());
        assertEquals(1200L, buffer.getNext().getMax());
        assertEquals(200, buffer.getNext().getStep());
    }

    @Test
    void updateMaxIdFromDb_shouldKeepStep_whenDurationBetween15And30Minutes() {
        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setBizTag("test");
        entity.setStep(100);
        entity.setMaxId(1000L);

        when(r2dbcEntityTemplate.selectOne(any(), eq(LeafAllocEntity.class)))
                .thenReturn(Mono.just(entity));
        when(r2dbcEntityTemplate.update(any(), any(), eq(LeafAllocEntity.class)))
                .thenReturn(Mono.just(1L));

        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.setStepUpdateTime(System.currentTimeMillis() - 20 * 60 * 1000L);
        buffer.setMinStep(50);

        StepVerifier.create(service.updateMaxIdFromDb(buffer, "test", false))
                .verifyComplete();

        assertEquals(1000L, buffer.getNext().getStart());
        assertEquals(1100L, buffer.getNext().getMax());
        assertEquals(100, buffer.getNext().getStep());
    }

    @Test
    void updateMaxIdFromDb_shouldHalveStepWithDefaultMinStep_whenDurationGreaterThan30Minutes() {
        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setBizTag("test");
        entity.setStep(100);
        entity.setMaxId(1000L);

        when(r2dbcEntityTemplate.selectOne(any(), eq(LeafAllocEntity.class)))
                .thenReturn(Mono.just(entity));
        when(r2dbcEntityTemplate.update(any(), any(), eq(LeafAllocEntity.class)))
                .thenReturn(Mono.just(1L));

        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.setStepUpdateTime(System.currentTimeMillis() - 40 * 60 * 1000L);

        StepVerifier.create(service.updateMaxIdFromDb(buffer, "test", false))
                .verifyComplete();

        assertEquals(1000L, buffer.getNext().getStart());
        assertEquals(1050L, buffer.getNext().getMax());
        assertEquals(50, buffer.getNext().getStep());
    }

    @Test
    void updateMaxIdFromDb_shouldHalveStepWithExistingMinStep_whenDurationGreaterThan30Minutes() {
        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setBizTag("test");
        entity.setStep(100);
        entity.setMaxId(1000L);

        when(r2dbcEntityTemplate.selectOne(any(), eq(LeafAllocEntity.class)))
                .thenReturn(Mono.just(entity));
        when(r2dbcEntityTemplate.update(any(), any(), eq(LeafAllocEntity.class)))
                .thenReturn(Mono.just(1L));

        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.setStepUpdateTime(System.currentTimeMillis() - 40 * 60 * 1000L);
        buffer.setMinStep(30);

        StepVerifier.create(service.updateMaxIdFromDb(buffer, "test", false))
                .verifyComplete();

        assertEquals(1000L, buffer.getNext().getStart());
        assertEquals(1050L, buffer.getNext().getMax());
        assertEquals(50, buffer.getNext().getStep());
    }

    @Test
    void updateMaxIdFromDb_shouldNotUpdateMinStep_whenAlreadySet() {
        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setBizTag("test");
        entity.setStep(100);
        entity.setMaxId(1000L);

        when(r2dbcEntityTemplate.selectOne(any(), eq(LeafAllocEntity.class)))
                .thenReturn(Mono.just(entity));
        when(r2dbcEntityTemplate.update(any(), any(), eq(LeafAllocEntity.class)))
                .thenReturn(Mono.just(1L));

        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.setMinStep(50);

        StepVerifier.create(service.updateMaxIdFromDb(buffer, "test", true))
                .verifyComplete();

        assertEquals(50, buffer.getMinStep());
    }

    @Test
    void updateMaxIdFromDb_shouldThrowException_whenEntityNotFound() {
        when(r2dbcEntityTemplate.selectOne(any(), eq(LeafAllocEntity.class)))
                .thenReturn(Mono.empty());

        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");

        StepVerifier.create(service.updateMaxIdFromDb(buffer, "missing", true))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void nextId_shouldNotTriggerPreload_whenValueBelowThreshold() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.getCurrent().setStep(100);
        buffer.getCurrent().setMax(200);
        buffer.getCurrent().setStart(100);
        buffer.getDisruptorLock().init(100, 200);
        buffer.setNextReady(false);
        buffer.getThreadRunning().set(false);
        service.getBufferCache().put("test", buffer);

        StepVerifier.create(service.nextId("test"))
                .expectNext(100L)
                .verifyComplete();
    }

    @Test
    void nextId_shouldNotTriggerPreload_whenNextReadyIsTrue() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.getCurrent().setStep(100);
        buffer.getCurrent().setMax(200);
        buffer.getCurrent().setStart(100);
        buffer.getDisruptorLock().init(150, 200);
        buffer.setNextReady(true);
        buffer.getThreadRunning().set(false);
        service.getBufferCache().put("test", buffer);

        StepVerifier.create(service.nextId("test"))
                .expectNext(150L)
                .verifyComplete();
    }

    @Test
    void init_shouldLoadEmptyTags_whenRepositoryEmpty() {
        when(leafAllocRepository.findAll()).thenReturn(Flux.empty());

        service.init();

        assertTrue(service.getBufferCache().isEmpty());
    }

    @Test
    void nextId_shouldNotTriggerPreload_whenThreadRunningIsTrue() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.getCurrent().setStep(100);
        buffer.getCurrent().setMax(200);
        buffer.getCurrent().setStart(100);
        buffer.getDisruptorLock().init(150, 200);
        buffer.setNextReady(false);
        buffer.getThreadRunning().set(true);
        service.getBufferCache().put("test", buffer);

        StepVerifier.create(service.nextId("test"))
                .expectNext(150L)
                .verifyComplete();
    }

    @Test
    void nextId_shouldCoverCasFailureBranch_withMockedCas() {
        LeafSegmentBuffer buffer = spy(new LeafSegmentBuffer("test"));
        buffer.getCurrent().setStep(100);
        buffer.getCurrent().setMax(200);
        buffer.getCurrent().setStart(100);
        buffer.getDisruptorLock().init(200, 200); // exhausted
        buffer.setNextReady(true);
        buffer.getNext().setStep(100);
        buffer.getNext().setMax(300);
        buffer.getNext().setStart(200);
        buffer.getThreadRunning().set(true);
        service.getBufferCache().put("test", buffer);

        when(buffer.casCurrentPos(anyInt(), anyInt())).thenReturn(false).thenReturn(true);

        StepVerifier.create(service.nextId("test"))
                .expectNext(100L)
                .verifyComplete();
    }

    @Test
    void nextId_shouldCoverCasFailureBranch_underConcurrentAccess() throws Exception {
        doAnswer(invocation -> {
            LeafSegmentBuffer buf = invocation.getArgument(0);
            boolean isInit = invocation.getArgument(2);
            LeafSegment target = isInit ? buf.getCurrent() : buf.getNext();
            long start;
            if (isInit) {
                start = 1000;
            } else {
                start = buf.getCurrent().getMax();
            }
            target.setStep(50);
            target.setMax(start + 50);
            target.setStart(start);
            if (isInit) {
                buf.getDisruptorLock().init(start, start + 50);
            } else {
                buf.setNextReady(true);
            }
            return Mono.empty();
        }).when(service).updateMaxIdFromDb(any(), anyString(), anyBoolean());

        service.nextId("concurrent").block();

        int threads = 8;
        int count = 60;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        try {
            List<Future<List<Long>>> futures = new ArrayList<>();
            for (int i = 0; i < threads; i++) {
                futures.add(executor.submit(() -> {
                    List<Long> ids = new ArrayList<>();
                    for (int j = 0; j < count; j++) {
                        ids.add(service.nextId("concurrent").block());
                    }
                    return ids;
                }));
            }

            Set<Long> allIds = new HashSet<>();
            for (Future<List<Long>> f : futures) {
                List<Long> ids = f.get(30, TimeUnit.SECONDS);
                for (Long id : ids) {
                    assertTrue(allIds.add(id), "Duplicate ID detected: " + id);
                }
            }
            assertEquals(threads * count, allIds.size());
        } finally {
            executor.shutdownNow();
        }
    }
}
