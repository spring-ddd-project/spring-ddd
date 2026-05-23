package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.exception.LeafAllocKeyNotExistsException;
import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import com.springddd.infrastructure.persistence.r2dbc.LeafAllocRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeafSegmentDomainServiceImplTest {

    @Mock
    private LeafAllocRepository leafAllocRepository;

    @Mock
    private LeafSegmentTransactionalService transactionalService;

    private LeafSegmentDomainServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new LeafSegmentDomainServiceImpl(leafAllocRepository, transactionalService);
    }

    // ---------- Reflection helpers ----------

    private void setInitOK(boolean value) throws Exception {
        Field field = LeafSegmentDomainServiceImpl.class.getDeclaredField("initOK");
        field.setAccessible(true);
        field.setBoolean(service, value);
    }

    private boolean getInitOK() throws Exception {
        Field field = LeafSegmentDomainServiceImpl.class.getDeclaredField("initOK");
        field.setAccessible(true);
        return field.getBoolean(service);
    }

    @SuppressWarnings("unchecked")
    private ConcurrentHashMap<String, LeafSegmentDomainServiceImpl.SegmentBuffer> getCache() throws Exception {
        Field field = LeafSegmentDomainServiceImpl.class.getDeclaredField("cache");
        field.setAccessible(true);
        return (ConcurrentHashMap<String, LeafSegmentDomainServiceImpl.SegmentBuffer>) field.get(service);
    }

    private int invokeCalculateNextStep(String key, LeafAllocEntity entity) throws Exception {
        Method method = LeafSegmentDomainServiceImpl.class.getDeclaredMethod("calculateNextStep", String.class, LeafAllocEntity.class);
        method.setAccessible(true);
        return (int) method.invoke(service, key, entity);
    }

    private void invokeMaybePreload(LeafSegmentDomainServiceImpl.SegmentBuffer buffer,
                                    LeafSegmentDomainServiceImpl.Segment current) throws Exception {
        Method method = LeafSegmentDomainServiceImpl.class.getDeclaredMethod("maybePreload",
                LeafSegmentDomainServiceImpl.SegmentBuffer.class,
                LeafSegmentDomainServiceImpl.Segment.class);
        method.setAccessible(true);
        method.invoke(service, buffer, current);
    }

    private void invokeWaitForNextSegment(LeafSegmentDomainServiceImpl.SegmentBuffer buffer) throws Exception {
        Method method = LeafSegmentDomainServiceImpl.class.getDeclaredMethod("waitForNextSegment",
                LeafSegmentDomainServiceImpl.SegmentBuffer.class);
        method.setAccessible(true);
        method.invoke(service, buffer);
    }

    private Mono<Void> invokePreloadNextSegment(LeafSegmentDomainServiceImpl.SegmentBuffer buffer) throws Exception {
        Method method = LeafSegmentDomainServiceImpl.class.getDeclaredMethod("preloadNextSegment",
                LeafSegmentDomainServiceImpl.SegmentBuffer.class);
        method.setAccessible(true);
        return (Mono<Void>) method.invoke(service, buffer);
    }

    private Mono<Long> invokeDoGetId(LeafSegmentDomainServiceImpl.SegmentBuffer buffer) throws Exception {
        Method method = LeafSegmentDomainServiceImpl.class.getDeclaredMethod("doGetId",
                LeafSegmentDomainServiceImpl.SegmentBuffer.class);
        method.setAccessible(true);
        return (Mono<Long>) method.invoke(service, buffer);
    }

    private Mono<Long> invokeGetIdFromSegmentBuffer(LeafSegmentDomainServiceImpl.SegmentBuffer buffer) throws Exception {
        Method method = LeafSegmentDomainServiceImpl.class.getDeclaredMethod("getIdFromSegmentBuffer",
                LeafSegmentDomainServiceImpl.SegmentBuffer.class);
        method.setAccessible(true);
        return (Mono<Long>) method.invoke(service, buffer);
    }

    // ---------- getId tests ----------

    @Test
    @DisplayName("getId 当 initOK=false 时应返回 IllegalStateException")
    void getId_whenNotInitialized_shouldReturnError() throws Exception {
        setInitOK(false);

        StepVerifier.create(service.getId("test"))
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    @DisplayName("getId 当 buffer 不存在时应返回 LeafAllocKeyNotExistsException")
    void getId_whenBufferNotExists_shouldReturnKeyNotExistsError() throws Exception {
        setInitOK(true);

        StepVerifier.create(service.getId("nonexistent"))
                .expectError(LeafAllocKeyNotExistsException.class)
                .verify();
    }

    @Test
    @DisplayName("getId 当 buffer 已初始化且有可用号段时应返回 ID")
    void getId_whenBufferExistsAndInitialized_shouldReturnId() throws Exception {
        setInitOK(true);

        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test");
        buffer.setInitOk(true);
        buffer.setStep(100);

        LeafSegmentDomainServiceImpl.Segment segment = buffer.getCurrent();
        segment.setMax(100);
        segment.setStep(100);
        segment.getValue().set(0);

        getCache().put("test", buffer);

        StepVerifier.create(service.getId("test"))
                .assertNext(id -> assertThat(id).isEqualTo(0L))
                .verifyComplete();

        assertThat(segment.getValue().get()).isEqualTo(1);
    }

    @Test
    @DisplayName("getId 当 buffer 未初始化时应从 DB 加载并返回 ID")
    void getId_whenBufferNotInitialized_shouldUpdateFromDbAndReturnId() throws Exception {
        setInitOK(true);

        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test");
        buffer.setInitOk(false);
        getCache().put("test", buffer);

        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setBizTag("test");
        entity.setStep(100);

        LeafAllocEntity updated = new LeafAllocEntity();
        updated.setBizTag("test");
        updated.setMaxId(1000L);

        when(leafAllocRepository.findByBizTag("test")).thenReturn(Mono.just(entity));
        when(transactionalService.updateMaxIdAndGet("test", 100)).thenReturn(Mono.just(updated));

        StepVerifier.create(service.getId("test"))
                .assertNext(id -> assertThat(id).isEqualTo(900L))
                .verifyComplete();

        assertThat(buffer.isInitOk()).isTrue();
        assertThat(buffer.getStep()).isEqualTo(100);
        assertThat(buffer.getMinStep()).isEqualTo(100);
    }

    @Test
    @DisplayName("getId 当当前号段耗尽且 nextReady=true 时应切换号段并返回 ID")
    void getId_whenSegmentExhausted_shouldSwitchAndReturnId() throws Exception {
        setInitOK(true);

        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test");
        buffer.setInitOk(true);
        buffer.setStep(100);
        buffer.setNextReady(true);

        // Current segment (pos 0) is exhausted
        LeafSegmentDomainServiceImpl.Segment seg0 = buffer.getSegments()[0];
        seg0.setMax(10);
        seg0.setStep(10);
        seg0.getValue().set(10);

        // Next segment (pos 1) has available IDs
        LeafSegmentDomainServiceImpl.Segment seg1 = buffer.getSegments()[1];
        seg1.setMax(100);
        seg1.setStep(100);
        seg1.getValue().set(0);

        getCache().put("test", buffer);

        StepVerifier.create(service.getId("test"))
                .assertNext(id -> assertThat(id).isEqualTo(0L))
                .verifyComplete();

        assertThat(buffer.getCurrentPos()).isEqualTo(1);
        assertThat(buffer.isNextReady()).isFalse();
        assertThat(seg1.getValue().get()).isEqualTo(1);
    }

    // ---------- init tests ----------

    @Test
    @DisplayName("initCache 应初始化缓存并设置 initOK=true")
    void initCache_shouldInitCacheAndSetInitOk() throws Exception {
        when(leafAllocRepository.findAllBizTags()).thenReturn(Flux.just("tag1", "tag2"));

        service.initCache();

        // Allow async subscription to complete
        Thread.sleep(200);

        assertThat(getInitOK()).isTrue();
        ConcurrentHashMap<String, LeafSegmentDomainServiceImpl.SegmentBuffer> cache = getCache();
        assertThat(cache).containsKeys("tag1", "tag2");
    }

    @Test
    @DisplayName("init 应更新缓存并设置 initOK=true")
    void init_shouldUpdateCacheAndSetInitOk() throws Exception {
        when(leafAllocRepository.findAllBizTags()).thenReturn(Flux.just("tag1", "tag2"));

        StepVerifier.create(service.init())
                .verifyComplete();

        assertThat(getInitOK()).isTrue();
        ConcurrentHashMap<String, LeafSegmentDomainServiceImpl.SegmentBuffer> cache = getCache();
        assertThat(cache).containsKeys("tag1", "tag2");
    }

    // ---------- isInitialized tests ----------

    @Test
    @DisplayName("isInitialized 当 initOK=false 时应返回 false")
    void isInitialized_whenFalse_shouldReturnFalse() throws Exception {
        setInitOK(false);

        StepVerifier.create(service.isInitialized())
                .assertNext(result -> assertThat(result).isFalse())
                .verifyComplete();
    }

    @Test
    @DisplayName("isInitialized 当 initOK=true 时应返回 true")
    void isInitialized_whenTrue_shouldReturnTrue() throws Exception {
        setInitOK(true);

        StepVerifier.create(service.isInitialized())
                .assertNext(result -> assertThat(result).isTrue())
                .verifyComplete();
    }

    // ---------- getCacheStatus tests ----------

    @Test
    @DisplayName("getCacheStatus 应返回缓存状态映射")
    void getCacheStatus_shouldReturnCacheMap() throws Exception {
        setInitOK(true);

        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test");
        buffer.setInitOk(true);
        buffer.setStep(100);
        buffer.setMinStep(10);
        buffer.setNextReady(true);
        buffer.setUpdateTimestamp(12345L);

        LeafSegmentDomainServiceImpl.Segment seg = buffer.getCurrent();
        seg.setMax(200);
        seg.setStep(100);
        seg.getValue().set(50);

        getCache().put("test", buffer);

        StepVerifier.create(service.getCacheStatus())
                .assertNext(status -> {
                    assertThat(status).containsKey("test");
                    @SuppressWarnings("unchecked")
                    Map<String, Object> bufferMap = (Map<String, Object>) status.get("test");
                    assertThat(bufferMap).isNotNull();
                    assertThat(bufferMap.get("key")).isEqualTo("test");
                    assertThat(bufferMap.get("currentPos")).isEqualTo(0);
                    assertThat(bufferMap.get("nextReady")).isEqualTo(true);
                    assertThat(bufferMap.get("initOk")).isEqualTo(true);
                    assertThat(bufferMap.get("threadRunning")).isEqualTo(false);
                    assertThat(bufferMap.get("step")).isEqualTo(100);
                    assertThat(bufferMap.get("minStep")).isEqualTo(10);
                    assertThat(bufferMap.get("updateTimestamp")).isEqualTo(12345L);

                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> segments = (List<Map<String, Object>>) bufferMap.get("segments");
                    assertThat(segments).hasSize(2);

                    Map<String, Object> seg0 = segments.get(0);
                    assertThat(seg0.get("index")).isEqualTo(0);
                    assertThat(seg0.get("value")).isEqualTo(50L);
                    assertThat(seg0.get("max")).isEqualTo(200L);
                    assertThat(seg0.get("step")).isEqualTo(100);
                    assertThat(seg0.get("idle")).isEqualTo(150L);
                })
                .verifyComplete();
    }

    // ---------- SegmentBuffer tests ----------

    @Test
    @DisplayName("SegmentBuffer 应正确管理状态")
    void segmentBuffer_shouldManageStateCorrectly() {
        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("order");

        assertThat(buffer.getKey()).isEqualTo("order");
        assertThat(buffer.getSegments()).hasSize(2);
        assertThat(buffer.getCurrentPos()).isEqualTo(0);
        assertThat(buffer.getCurrent()).isSameAs(buffer.getSegments()[0]);
        assertThat(buffer.nextPos()).isEqualTo(1);

        buffer.switchPos();
        assertThat(buffer.getCurrentPos()).isEqualTo(1);
        assertThat(buffer.getCurrent()).isSameAs(buffer.getSegments()[1]);
        assertThat(buffer.nextPos()).isEqualTo(0);

        buffer.switchPos();
        assertThat(buffer.getCurrentPos()).isEqualTo(0);

        assertThat(buffer.isInitOk()).isFalse();
        buffer.setInitOk(true);
        assertThat(buffer.isInitOk()).isTrue();

        assertThat(buffer.isNextReady()).isFalse();
        buffer.setNextReady(true);
        assertThat(buffer.isNextReady()).isTrue();

        assertThat(buffer.isThreadRunning()).isFalse();
        assertThat(buffer.startThreadRunning()).isTrue();
        assertThat(buffer.isThreadRunning()).isTrue();
        assertThat(buffer.startThreadRunning()).isFalse();
        buffer.stopThreadRunning();
        assertThat(buffer.isThreadRunning()).isFalse();

        buffer.setStep(500);
        assertThat(buffer.getStep()).isEqualTo(500);

        buffer.setMinStep(50);
        assertThat(buffer.getMinStep()).isEqualTo(50);

        buffer.setUpdateTimestamp(99999L);
        assertThat(buffer.getUpdateTimestamp()).isEqualTo(99999L);
    }

    // ---------- Segment tests ----------

    @Test
    @DisplayName("Segment 应正确管理数值和边界")
    void segment_shouldManageValueAndMax() {
        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test");
        LeafSegmentDomainServiceImpl.Segment segment = new LeafSegmentDomainServiceImpl.Segment(buffer);

        assertThat(segment.getBuffer()).isSameAs(buffer);
        assertThat(segment.getValue().get()).isEqualTo(0L);
        assertThat(segment.getMax()).isEqualTo(0L);
        assertThat(segment.getStep()).isEqualTo(0);
        assertThat(segment.getIdle()).isEqualTo(0L);

        segment.setMax(100);
        segment.setStep(10);
        segment.getValue().set(30);

        assertThat(segment.getMax()).isEqualTo(100L);
        assertThat(segment.getStep()).isEqualTo(10);
        assertThat(segment.getValue().get()).isEqualTo(30L);
        assertThat(segment.getIdle()).isEqualTo(70L);
    }

    // ---------- calculateNextStep tests ----------

    @Test
    @DisplayName("calculateNextStep 当 buffer 为 null 时应返回 entity 的 step")
    void calculateNextStep_whenBufferNull_shouldReturnEntityStep() throws Exception {
        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setStep(200);

        int result = invokeCalculateNextStep("missing", entity);
        assertThat(result).isEqualTo(200);
    }

    @Test
    @DisplayName("calculateNextStep 当 buffer 未 initOk 时应返回 entity 的 step")
    void calculateNextStep_whenBufferNotInitOk_shouldReturnEntityStep() throws Exception {
        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test");
        buffer.setInitOk(false);
        getCache().put("test", buffer);

        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setStep(200);

        int result = invokeCalculateNextStep("test", entity);
        assertThat(result).isEqualTo(200);
    }

    @Test
    @DisplayName("calculateNextStep 当 updateTimestamp 为 0 时应返回 entity 的 step")
    void calculateNextStep_whenUpdateTimestampZero_shouldReturnEntityStep() throws Exception {
        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test");
        buffer.setInitOk(true);
        buffer.setUpdateTimestamp(0);
        getCache().put("test", buffer);

        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setStep(200);

        int result = invokeCalculateNextStep("test", entity);
        assertThat(result).isEqualTo(200);
    }

    @Test
    @DisplayName("calculateNextStep 当持续时间小于 SEGMENT_DURATION 时应将 step 翻倍")
    void calculateNextStep_whenDurationLessThanSegmentDuration_shouldDoubleStep() throws Exception {
        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test");
        buffer.setInitOk(true);
        buffer.setStep(1000);
        buffer.setUpdateTimestamp(System.currentTimeMillis() - 5 * 60 * 1000L); // 5 min ago
        getCache().put("test", buffer);

        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setStep(100);

        int result = invokeCalculateNextStep("test", entity);
        assertThat(result).isEqualTo(2000);
    }

    @Test
    @DisplayName("calculateNextStep 当翻倍后超过 MAX_STEP 时应保持 step 不变")
    void calculateNextStep_whenDoubleExceedsMaxStep_shouldKeepStep() throws Exception {
        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test");
        buffer.setInitOk(true);
        buffer.setStep(900_000);
        buffer.setUpdateTimestamp(System.currentTimeMillis() - 5 * 60 * 1000L);
        getCache().put("test", buffer);

        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setStep(100);

        int result = invokeCalculateNextStep("test", entity);
        assertThat(result).isEqualTo(900_000);
    }

    @Test
    @DisplayName("calculateNextStep 当持续时间在 SEGMENT_DURATION 和 2*SEGMENT_DURATION 之间时应保持 step 不变")
    void calculateNextStep_whenDurationBetweenOneAndTwoSegments_shouldKeepStep() throws Exception {
        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test");
        buffer.setInitOk(true);
        buffer.setStep(5000);
        buffer.setUpdateTimestamp(System.currentTimeMillis() - 20 * 60 * 1000L); // 20 min ago
        getCache().put("test", buffer);

        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setStep(100);

        int result = invokeCalculateNextStep("test", entity);
        assertThat(result).isEqualTo(5000);
    }

    @Test
    @DisplayName("calculateNextStep 当持续时间大于等于 2*SEGMENT_DURATION 且一半大于等于 minStep 时应将 step 减半")
    void calculateNextStep_whenDurationGreaterThanTwoSegments_shouldHalveStep() throws Exception {
        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test");
        buffer.setInitOk(true);
        buffer.setStep(1000);
        buffer.setMinStep(100);
        buffer.setUpdateTimestamp(System.currentTimeMillis() - 40 * 60 * 1000L); // 40 min ago
        getCache().put("test", buffer);

        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setStep(100);

        int result = invokeCalculateNextStep("test", entity);
        assertThat(result).isEqualTo(500);
    }

    @Test
    @DisplayName("calculateNextStep 当持续时间大于等于 2*SEGMENT_DURATION 且一半小于 minStep 时应保持 step 不变")
    void calculateNextStep_whenHalvedLessThanMinStep_shouldKeepStep() throws Exception {
        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test");
        buffer.setInitOk(true);
        buffer.setStep(100);
        buffer.setMinStep(100);
        buffer.setUpdateTimestamp(System.currentTimeMillis() - 40 * 60 * 1000L);
        getCache().put("test", buffer);

        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setStep(100);

        int result = invokeCalculateNextStep("test", entity);
        assertThat(result).isEqualTo(100);
    }

    // ---------- waitForNextSegment tests ----------

    @Test
    @DisplayName("waitForNextSegment 当 threadRunning 为 true 时应循环等待后退出")
    void waitForNextSegment_whenThreadRunning_shouldSpinAndBreak() throws Exception {
        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test");
        buffer.startThreadRunning();
        assertThat(buffer.isThreadRunning()).isTrue();

        invokeWaitForNextSegment(buffer);
        // Method returns after roll > 10000 break
    }

    @Test
    @DisplayName("waitForNextSegment 当 threadRunning 为 false 时应立即返回")
    void waitForNextSegment_whenThreadNotRunning_shouldReturnImmediately() throws Exception {
        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test");
        assertThat(buffer.isThreadRunning()).isFalse();

        invokeWaitForNextSegment(buffer);
    }

    // ---------- preloadNextSegment tests ----------

    @Test
    @DisplayName("preloadNextSegment 应从数据库加载并更新下一个号段")
    void preloadNextSegment_shouldUpdateNextSegmentFromDb() throws Exception {
        setInitOK(true);

        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test");
        buffer.setInitOk(true);
        buffer.setStep(100);

        LeafSegmentDomainServiceImpl.Segment seg0 = buffer.getSegments()[0];
        seg0.setMax(100);
        seg0.setStep(100);
        seg0.getValue().set(0);

        LeafSegmentDomainServiceImpl.Segment seg1 = buffer.getSegments()[1];
        seg1.setMax(0);
        seg1.setStep(0);
        seg1.getValue().set(0);

        getCache().put("test", buffer);

        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setBizTag("test");
        entity.setStep(100);

        LeafAllocEntity updated = new LeafAllocEntity();
        updated.setBizTag("test");
        updated.setMaxId(1000L);

        when(leafAllocRepository.findByBizTag("test")).thenReturn(Mono.just(entity));
        when(transactionalService.updateMaxIdAndGet("test", 100)).thenReturn(Mono.just(updated));

        StepVerifier.create(invokePreloadNextSegment(buffer))
                .verifyComplete();

        assertThat(seg1.getMax()).isEqualTo(1000L);
        assertThat(seg1.getStep()).isEqualTo(100);
        assertThat(seg1.getValue().get()).isEqualTo(900L);
    }

    // ---------- maybePreload tests ----------

    @Test
    @DisplayName("maybePreload 当 idle 低于阈值时应启动预加载线程")
    void maybePreload_whenIdleBelowThreshold_shouldStartPreloadThread() throws Exception {
        setInitOK(true);

        java.util.concurrent.atomic.AtomicBoolean preloadStarted = new java.util.concurrent.atomic.AtomicBoolean(false);
        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test") {
            @Override
            public boolean startThreadRunning() {
                boolean result = super.startThreadRunning();
                if (result) {
                    preloadStarted.set(true);
                }
                return result;
            }
        };
        buffer.setInitOk(true);
        buffer.setStep(100);

        LeafSegmentDomainServiceImpl.Segment current = buffer.getCurrent();
        current.setMax(100);
        current.setStep(100);
        current.getValue().set(15); // idle = 85 < 90

        getCache().put("test", buffer);

        invokeMaybePreload(buffer, current);

        assertThat(preloadStarted.get()).isTrue();
    }

    @Test
    @DisplayName("maybePreload 当 nextReady 已为 true 时不应触发预加载")
    void maybePreload_whenNextReadyAlreadyTrue_shouldNotTrigger() throws Exception {
        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test");
        buffer.setInitOk(true);
        buffer.setStep(100);
        buffer.setNextReady(true);

        LeafSegmentDomainServiceImpl.Segment current = buffer.getCurrent();
        current.setMax(100);
        current.setStep(100);
        current.getValue().set(15);

        invokeMaybePreload(buffer, current);

        assertThat(buffer.isThreadRunning()).isFalse();
    }

    @Test
    @DisplayName("maybePreload 当 idle 高于阈值时不应触发预加载")
    void maybePreload_whenIdleAboveThreshold_shouldNotTrigger() throws Exception {
        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test");
        buffer.setInitOk(true);
        buffer.setStep(100);

        LeafSegmentDomainServiceImpl.Segment current = buffer.getCurrent();
        current.setMax(100);
        current.setStep(100);
        current.getValue().set(5); // idle = 95 >= 90

        invokeMaybePreload(buffer, current);

        assertThat(buffer.isThreadRunning()).isFalse();
    }

    // ---------- doGetId edge case tests ----------

    @Test
    @DisplayName("doGetId 当两个号段都耗尽且 next 未准备好时应抛出 IllegalStateException")
    void doGetId_whenBothSegmentsExhausted_shouldThrowIllegalState() throws Exception {
        setInitOK(true);

        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test");
        buffer.setInitOk(true);
        buffer.setStep(10);
        buffer.setNextReady(false);

        // Both segments exhausted
        LeafSegmentDomainServiceImpl.Segment seg0 = buffer.getSegments()[0];
        seg0.setMax(10);
        seg0.setStep(10);
        seg0.getValue().set(10);

        LeafSegmentDomainServiceImpl.Segment seg1 = buffer.getSegments()[1];
        seg1.setMax(20);
        seg1.setStep(10);
        seg1.getValue().set(20);

        getCache().put("test", buffer);

        StepVerifier.create(invokeDoGetId(buffer))
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    @DisplayName("doGetId 当当前号段耗尽但在同步块中获取成功时应返回 ID")
    void doGetId_whenExhaustedButSyncRetrySucceeds_shouldReturnId() throws Exception {
        setInitOK(true);

        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test");
        buffer.setInitOk(true);
        buffer.setStep(10);
        buffer.setNextReady(false);

        // Current segment: value will be exhausted after first getAndIncrement
        // But we set it to 9 so first getAndIncrement returns 9 (< 10)
        LeafSegmentDomainServiceImpl.Segment seg0 = buffer.getSegments()[0];
        seg0.setMax(10);
        seg0.setStep(10);
        seg0.getValue().set(9);

        getCache().put("test", buffer);

        StepVerifier.create(invokeDoGetId(buffer))
                .assertNext(id -> assertThat(id).isEqualTo(9L))
                .verifyComplete();
    }

    // ---------- getIdFromSegmentBuffer tests ----------

    @Test
    @DisplayName("getIdFromSegmentBuffer 当 buffer 未初始化时应从 DB 更新并返回 ID")
    void getIdFromSegmentBuffer_whenNotInitOk_shouldUpdateFromDbAndReturnId() throws Exception {
        setInitOK(true);

        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test");
        buffer.setInitOk(false);
        getCache().put("test", buffer);

        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setBizTag("test");
        entity.setStep(100);

        LeafAllocEntity updated = new LeafAllocEntity();
        updated.setBizTag("test");
        updated.setMaxId(1000L);

        when(leafAllocRepository.findByBizTag("test")).thenReturn(Mono.just(entity));
        when(transactionalService.updateMaxIdAndGet("test", 100)).thenReturn(Mono.just(updated));

        StepVerifier.create(invokeGetIdFromSegmentBuffer(buffer))
                .assertNext(id -> assertThat(id).isEqualTo(900L))
                .verifyComplete();

        assertThat(buffer.isInitOk()).isTrue();
    }

    @Test
    @DisplayName("getIdFromSegmentBuffer 当 buffer 已初始化时应直接返回 ID")
    void getIdFromSegmentBuffer_whenInitOk_shouldReturnIdDirectly() throws Exception {
        setInitOK(true);

        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test");
        buffer.setInitOk(true);
        buffer.setStep(100);

        LeafSegmentDomainServiceImpl.Segment seg = buffer.getCurrent();
        seg.setMax(100);
        seg.setStep(100);
        seg.getValue().set(0);

        getCache().put("test", buffer);

        StepVerifier.create(invokeGetIdFromSegmentBuffer(buffer))
                .assertNext(id -> assertThat(id).isEqualTo(0L))
                .verifyComplete();
    }

    @Test
    @DisplayName("getIdFromSegmentBuffer 当 buffer 在同步块中被其他线程初始化后应直接返回 ID")
    void getIdFromSegmentBuffer_whenInitOkChangedInSync_shouldReturnIdDirectly() throws Exception {
        setInitOK(true);

        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test") {
            private int initOkCalls = 0;
            @Override
            public boolean isInitOk() {
                initOkCalls++;
                return initOkCalls > 1;
            }
        };
        buffer.setStep(100);

        LeafSegmentDomainServiceImpl.Segment seg = buffer.getCurrent();
        seg.setMax(100);
        seg.setStep(100);
        seg.getValue().set(0);

        getCache().put("test", buffer);

        StepVerifier.create(invokeGetIdFromSegmentBuffer(buffer))
                .assertNext(id -> assertThat(id).isEqualTo(0L))
                .verifyComplete();
    }

    @Test
    @DisplayName("doGetId 当当前号段耗尽且 nextReady=true 时应切换号段并返回 ID")
    void doGetId_whenSegmentExhaustedAndNextReady_shouldSwitchAndReturnId() throws Exception {
        setInitOK(true);

        LeafSegmentDomainServiceImpl.SegmentBuffer buffer = new LeafSegmentDomainServiceImpl.SegmentBuffer("test");
        buffer.setInitOk(true);
        buffer.setStep(100);
        buffer.setNextReady(true);

        // Current segment (pos 0) is exhausted
        LeafSegmentDomainServiceImpl.Segment seg0 = buffer.getSegments()[0];
        seg0.setMax(10);
        seg0.setStep(10);
        seg0.getValue().set(10);

        // Next segment (pos 1) has available IDs
        LeafSegmentDomainServiceImpl.Segment seg1 = buffer.getSegments()[1];
        seg1.setMax(100);
        seg1.setStep(100);
        seg1.getValue().set(0);

        getCache().put("test", buffer);

        StepVerifier.create(invokeDoGetId(buffer))
                .assertNext(id -> assertThat(id).isEqualTo(0L))
                .verifyComplete();

        assertThat(buffer.getCurrentPos()).isEqualTo(1);
        assertThat(buffer.isNextReady()).isFalse();
        assertThat(seg1.getValue().get()).isEqualTo(1);
    }
}
