package com.springddd.application.service.leaf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.test.StepVerifier;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.mockito.MockedStatic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SnowflakeDomainServiceImplTest {

    @Mock
    private SnowflakeProperties properties;

    @Mock
    private SnowflakeWorkerService snowflakeWorkerService;

    @InjectMocks
    private SnowflakeDomainServiceImpl snowflakeDomainService;

    @BeforeEach
    void setUp() {
        when(properties.isEnabled()).thenReturn(true);
        when(properties.getTwepoch()).thenReturn(1288834974657L);
        when(snowflakeWorkerService.isInitialized()).thenReturn(true);
        when(snowflakeWorkerService.getAssignedWorkerId()).thenReturn(0L);
        when(snowflakeWorkerService.getAssignedDatacenterId()).thenReturn(0L);
        snowflakeDomainService.init();
    }

    // ---------- getId tests ----------

    @Test
    @DisplayName("当服务启用且 worker 初始化完成时，应成功生成 ID")
    void getId_shouldReturnId_whenEnabledAndInitialized() {
        StepVerifier.create(snowflakeDomainService.getId())
                .assertNext(id -> {
                    assertThat(id).isNotNull();
                    assertThat(id).isPositive();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("当服务禁用时，应返回错误")
    void getId_shouldReturnError_whenDisabled() {
        when(properties.isEnabled()).thenReturn(false);

        StepVerifier.create(snowflakeDomainService.getId())
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    @DisplayName("当 worker 未初始化时，应返回错误")
    void getId_shouldReturnError_whenWorkerNotInitialized() {
        when(snowflakeWorkerService.isInitialized()).thenReturn(false);

        StepVerifier.create(snowflakeDomainService.getId())
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    @DisplayName("连续生成多个 ID 应保持唯一性和递增趋势")
    void getId_shouldGenerateUniqueAndIncreasingIds() {
        long id1 = snowflakeDomainService.getId().block();
        long id2 = snowflakeDomainService.getId().block();
        long id3 = snowflakeDomainService.getId().block();

        assertThat(id1).isNotNull();
        assertThat(id2).isNotNull();
        assertThat(id3).isNotNull();
        assertThat(id1).isNotEqualTo(id2);
        assertThat(id2).isNotEqualTo(id3);
        assertThat(id3).isGreaterThanOrEqualTo(id1);
    }

    // ---------- decodeId tests ----------

    @Test
    @DisplayName("decodeId 应正确解析 Snowflake ID 的各部分")
    void decodeId_shouldReturnCorrectComponents() {
        long snowflakeId = snowflakeDomainService.getId().block();

        StepVerifier.create(snowflakeDomainService.decodeId(snowflakeId))
                .assertNext(result -> {
                    assertThat(result).containsKeys("timestamp", "datacenterId", "workerId", "sequenceId");
                    assertThat(result.get("datacenterId")).isEqualTo("0");
                    assertThat(result.get("workerId")).isEqualTo("0");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("decodeId 应能解析已知的 Snowflake ID")
    void decodeId_shouldDecodeKnownId() {
        // 已知: timestamp=1577836800000(2020-01-01), datacenterId=1, workerId=2, sequence=3
        long timestamp = 1577836800000L;
        long datacenterId = 1;
        long workerId = 2;
        long sequence = 3;
        long knownId = ((timestamp - 1288834974657L) << 22)
                | (datacenterId << 17)
                | (workerId << 12)
                | sequence;

        StepVerifier.create(snowflakeDomainService.decodeId(knownId))
                .assertNext(result -> {
                    assertThat(result.get("datacenterId")).isEqualTo("1");
                    assertThat(result.get("workerId")).isEqualTo("2");
                    assertThat(result.get("sequenceId")).isEqualTo("3");
                    String timestampStr = (String) result.get("timestamp");
                    assertThat(timestampStr).startsWith("1577836800000");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("生成的 ID 应包含正确的时间戳部分")
    void getId_shouldContainCorrectTimestamp() {
        long before = System.currentTimeMillis();
        long id = snowflakeDomainService.getId().block();
        long after = System.currentTimeMillis();

        long timestamp = (id >> 22) + 1288834974657L;
        assertThat(timestamp).isGreaterThanOrEqualTo(before - 1);
        assertThat(timestamp).isLessThanOrEqualTo(after + 1);
    }

    @Test
    @DisplayName("不同 workerId 生成的 ID 在相同时间应不同")
    void getId_shouldDifferWithDifferentWorkerIds() {
        long id1 = snowflakeDomainService.getId().block();

        when(snowflakeWorkerService.getAssignedWorkerId()).thenReturn(5L);
        long id2 = snowflakeDomainService.getId().block();

        assertThat(id1).isNotEqualTo(id2);

        long workerId1 = (id1 >> 12) & 0x1F;
        long workerId2 = (id2 >> 12) & 0x1F;
        assertThat(workerId1).isEqualTo(0);
        assertThat(workerId2).isEqualTo(5);
    }

    // ---------- init tests ----------

    @Test
    @DisplayName("init 当服务禁用时不应设置 twepoch")
    void init_whenDisabled_shouldNotSetTwepoch() throws Exception {
        SnowflakeDomainServiceImpl freshService = new SnowflakeDomainServiceImpl(properties, snowflakeWorkerService);
        when(properties.isEnabled()).thenReturn(false);

        freshService.init();

        Field twepochField = SnowflakeDomainServiceImpl.class.getDeclaredField("twepoch");
        twepochField.setAccessible(true);
        long twepoch = (long) twepochField.get(freshService);
        assertThat(twepoch).isEqualTo(0L);
    }

    // ---------- nextId edge case tests ----------

    @Test
    @DisplayName("nextId 当序列溢出时应重新随机序列并等待下一毫秒")
    void nextId_whenSequenceOverflow_shouldRegenerateSequence() throws Exception {
        Field lastTimestampField = SnowflakeDomainServiceImpl.class.getDeclaredField("lastTimestamp");
        lastTimestampField.setAccessible(true);
        Field sequenceField = SnowflakeDomainServiceImpl.class.getDeclaredField("sequence");
        sequenceField.setAccessible(true);
        Method nextIdMethod = SnowflakeDomainServiceImpl.class.getDeclaredMethod("nextId");
        nextIdMethod.setAccessible(true);

        long now = System.currentTimeMillis();
        lastTimestampField.setLong(snowflakeDomainService, now);
        sequenceField.setLong(snowflakeDomainService, 4095L); // SEQUENCE_MASK

        long id = (long) nextIdMethod.invoke(snowflakeDomainService);

        assertThat(id).isPositive();
        // Verify sequence was reset to something < 100 (random nextInt(100))
        long seq = id & 0xFFF;
        assertThat(seq).isLessThan(100);
    }

    @Test
    @DisplayName("nextId 当时钟回拨超过 5ms 时应抛出异常")
    void nextId_whenClockMovedBackwardsMoreThanFiveMs_shouldThrowException() throws Exception {
        Field lastTimestampField = SnowflakeDomainServiceImpl.class.getDeclaredField("lastTimestamp");
        lastTimestampField.setAccessible(true);
        Method nextIdMethod = SnowflakeDomainServiceImpl.class.getDeclaredMethod("nextId");
        nextIdMethod.setAccessible(true);

        long future = System.currentTimeMillis() + 10;
        lastTimestampField.setLong(snowflakeDomainService, future);

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> {
            try {
                nextIdMethod.invoke(snowflakeDomainService);
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw e.getCause();
            }
        })
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Clock moved backwards");
    }

    @Test
    @DisplayName("nextId 当时钟回拨不超过 5ms 时应等待后恢复")
    void nextId_whenClockMovedBackwardsWithinFiveMs_shouldRecover() throws Exception {
        Field lastTimestampField = SnowflakeDomainServiceImpl.class.getDeclaredField("lastTimestamp");
        lastTimestampField.setAccessible(true);
        Method nextIdMethod = SnowflakeDomainServiceImpl.class.getDeclaredMethod("nextId");
        nextIdMethod.setAccessible(true);

        long future = System.currentTimeMillis() + 3;
        lastTimestampField.setLong(snowflakeDomainService, future);

        long id = (long) nextIdMethod.invoke(snowflakeDomainService);
        assertThat(id).isPositive();
    }

    @Test
    @DisplayName("nextId 当等待被中断时应抛出异常")
    void nextId_whenWaitInterrupted_shouldThrowException() throws Exception {
        Field lastTimestampField = SnowflakeDomainServiceImpl.class.getDeclaredField("lastTimestamp");
        lastTimestampField.setAccessible(true);
        Method nextIdMethod = SnowflakeDomainServiceImpl.class.getDeclaredMethod("nextId");
        nextIdMethod.setAccessible(true);

        long future = System.currentTimeMillis() + 2;
        lastTimestampField.setLong(snowflakeDomainService, future);

        Thread.currentThread().interrupt();
        try {
            org.assertj.core.api.Assertions.assertThatThrownBy(() -> {
                try {
                    nextIdMethod.invoke(snowflakeDomainService);
                } catch (java.lang.reflect.InvocationTargetException e) {
                    throw e.getCause();
                }
            })
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Wait interrupted");
        } finally {
            Thread.interrupted(); // clear interrupt flag
        }
    }

    // ---------- tilNextMillis tests ----------

    @Test
    @DisplayName("tilNextMillis 应返回大于 lastTimestamp 的时间戳")
    void tilNextMillis_shouldReturnTimestampGreaterThanLast() throws Exception {
        Method tilNextMillisMethod = SnowflakeDomainServiceImpl.class.getDeclaredMethod("tilNextMillis", long.class);
        tilNextMillisMethod.setAccessible(true);

        long pastTimestamp = System.currentTimeMillis() - 1000;
        long result = (long) tilNextMillisMethod.invoke(snowflakeDomainService, pastTimestamp);

        assertThat(result).isGreaterThan(pastTimestamp);
    }

    @Test
    @DisplayName("nextId 当时钟回拨不超过5ms且sleep后仍然回拨时应抛出异常")
    void nextId_whenClockMovedBackwardsWithinFiveMs_andStillBackwardsAfterSleep_shouldThrowException() throws Exception {
        SnowflakeDomainServiceImpl customService = new SnowflakeDomainServiceImpl(properties, snowflakeWorkerService) {
            private int callCount = 0;

            @Override
            long timeGen() {
                callCount++;
                if (callCount == 1) return 1000L;
                if (callCount == 2) return 1004L;
                return super.timeGen();
            }
        };
        customService.init();

        Field lastTimestampField = SnowflakeDomainServiceImpl.class.getDeclaredField("lastTimestamp");
        lastTimestampField.setAccessible(true);
        lastTimestampField.setLong(customService, 1005L);

        Method nextIdMethod = SnowflakeDomainServiceImpl.class.getDeclaredMethod("nextId");
        nextIdMethod.setAccessible(true);

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> {
            try {
                nextIdMethod.invoke(customService);
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw e.getCause();
            }
        })
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Clock moved backwards");
    }
}
