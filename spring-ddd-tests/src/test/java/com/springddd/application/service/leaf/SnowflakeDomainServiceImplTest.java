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

import java.util.Map;

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
}
