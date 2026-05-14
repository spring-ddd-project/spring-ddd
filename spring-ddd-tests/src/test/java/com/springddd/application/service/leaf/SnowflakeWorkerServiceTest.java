package com.springddd.application.service.leaf;

import com.springddd.infrastructure.persistence.entity.LeafWorkerEntity;
import com.springddd.infrastructure.persistence.r2dbc.LeafWorkerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SnowflakeWorkerServiceTest {

    @Mock
    private LeafWorkerRepository leafWorkerRepository;

    @Mock
    private SnowflakeProperties properties;

    @InjectMocks
    private SnowflakeWorkerService snowflakeWorkerService;

    @BeforeEach
    void setUp() {
        when(properties.isEnabled()).thenReturn(true);
        when(properties.getDatacenterId()).thenReturn(0L);
    }

    @Test
    @DisplayName("当 IP 和 Port 已存在记录时，应复用已有的 workerId")
    void allocateWorker_shouldReuseExistingWorker() {
        LeafWorkerEntity existing = new LeafWorkerEntity();
        existing.setId(1L);
        existing.setWorkerId(5);
        existing.setDatacenterId(0);
        existing.setIp("");
        existing.setPort(9090);

        when(properties.getPort()).thenReturn(9090);
        when(leafWorkerRepository.findByIpAndPortAndDeleteStatusFalse(anyString(), eq(9090)))
                .thenReturn(Mono.just(existing));
        when(leafWorkerRepository.updateLastTimestamp(eq(1L), anyLong()))
                .thenReturn(Mono.just(1));

        snowflakeWorkerService.init();

        assertThat(snowflakeWorkerService.isInitialized()).isTrue();
        assertThat(snowflakeWorkerService.getAssignedWorkerId()).isEqualTo(5);
        verify(leafWorkerRepository).updateLastTimestamp(eq(1L), anyLong());
    }

    @Test
    @DisplayName("当不存在记录时，应分配最小的可用 workerId")
    void allocateWorker_shouldAssignMinAvailableWorkerId() {
        when(properties.getPort()).thenReturn(9090);
        when(leafWorkerRepository.findByIpAndPortAndDeleteStatusFalse(anyString(), eq(9090)))
                .thenReturn(Mono.empty());
        when(leafWorkerRepository.findByDeleteStatusFalseOrderByWorkerIdAsc())
                .thenReturn(Flux.empty());

        LeafWorkerEntity saved = new LeafWorkerEntity();
        saved.setId(1L);
        saved.setWorkerId(0);
        saved.setDatacenterId(0);
        when(leafWorkerRepository.save(any(LeafWorkerEntity.class)))
                .thenReturn(Mono.just(saved));

        snowflakeWorkerService.init();

        assertThat(snowflakeWorkerService.isInitialized()).isTrue();
        assertThat(snowflakeWorkerService.getAssignedWorkerId()).isEqualTo(0);
    }

    @Test
    @DisplayName("当 workerId 0,1,2 已被占用时，应分配 workerId 3")
    void allocateWorker_shouldAssignNextAvailableWorkerId() {
        when(properties.getPort()).thenReturn(9090);
        when(leafWorkerRepository.findByIpAndPortAndDeleteStatusFalse(anyString(), eq(9090)))
                .thenReturn(Mono.empty());

        List<LeafWorkerEntity> usedWorkers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            LeafWorkerEntity w = new LeafWorkerEntity();
            w.setWorkerId(i);
            usedWorkers.add(w);
        }
        when(leafWorkerRepository.findByDeleteStatusFalseOrderByWorkerIdAsc())
                .thenReturn(Flux.fromIterable(usedWorkers));

        LeafWorkerEntity saved = new LeafWorkerEntity();
        saved.setId(4L);
        saved.setWorkerId(3);
        when(leafWorkerRepository.save(any(LeafWorkerEntity.class)))
                .thenReturn(Mono.just(saved));

        snowflakeWorkerService.init();

        assertThat(snowflakeWorkerService.getAssignedWorkerId()).isEqualTo(3);
    }

    @Test
    @DisplayName("当 workerId 0,2 被占用时（1 空闲），应分配 workerId 1")
    void allocateWorker_shouldFillGap() {
        when(properties.getPort()).thenReturn(9090);
        when(leafWorkerRepository.findByIpAndPortAndDeleteStatusFalse(anyString(), eq(9090)))
                .thenReturn(Mono.empty());

        LeafWorkerEntity w0 = new LeafWorkerEntity();
        w0.setWorkerId(0);
        LeafWorkerEntity w2 = new LeafWorkerEntity();
        w2.setWorkerId(2);
        when(leafWorkerRepository.findByDeleteStatusFalseOrderByWorkerIdAsc())
                .thenReturn(Flux.just(w0, w2));

        LeafWorkerEntity saved = new LeafWorkerEntity();
        saved.setId(2L);
        saved.setWorkerId(1);
        when(leafWorkerRepository.save(any(LeafWorkerEntity.class)))
                .thenReturn(Mono.just(saved));

        snowflakeWorkerService.init();

        assertThat(snowflakeWorkerService.getAssignedWorkerId()).isEqualTo(1);
    }

    @Test
    @DisplayName("当 workerId 超过最大值时应抛出异常")
    void allocateWorker_shouldFail_whenNoAvailableWorkerId() {
        when(properties.getPort()).thenReturn(9090);
        when(leafWorkerRepository.findByIpAndPortAndDeleteStatusFalse(anyString(), eq(9090)))
                .thenReturn(Mono.empty());

        List<LeafWorkerEntity> usedWorkers = new ArrayList<>();
        for (int i = 0; i <= 31; i++) {
            LeafWorkerEntity w = new LeafWorkerEntity();
            w.setWorkerId(i);
            usedWorkers.add(w);
        }
        when(leafWorkerRepository.findByDeleteStatusFalseOrderByWorkerIdAsc())
                .thenReturn(Flux.fromIterable(usedWorkers));

        snowflakeWorkerService.init();

        assertThat(snowflakeWorkerService.isInitialized()).isFalse();
    }

    @Test
    @DisplayName("当服务禁用时，不应初始化 worker")
    void init_shouldNotInitialize_whenDisabled() {
        when(properties.isEnabled()).thenReturn(false);

        snowflakeWorkerService.init();

        assertThat(snowflakeWorkerService.isInitialized()).isFalse();
        verifyNoInteractions(leafWorkerRepository);
    }

    @Test
    @DisplayName("cleanupExpiredWorkers 应标记过期的 worker")
    void cleanupExpiredWorkers_shouldMarkExpiredWorkers() {
        when(leafWorkerRepository.markExpiredWorkers(anyLong()))
                .thenReturn(Mono.just(2));

        StepVerifier.create(snowflakeWorkerService.cleanupExpiredWorkers())
                .verifyComplete();

        verify(leafWorkerRepository).markExpiredWorkers(anyLong());
    }

    @Test
    @DisplayName("cleanupExpiredWorkers 应处理无过期 worker 的情况")
    void cleanupExpiredWorkers_shouldHandleNoExpiredWorkers() {
        when(leafWorkerRepository.markExpiredWorkers(anyLong()))
                .thenReturn(Mono.just(0));

        StepVerifier.create(snowflakeWorkerService.cleanupExpiredWorkers())
                .verifyComplete();
    }

    @Test
    @DisplayName("当 datacenterId 超出范围时应抛出异常")
    void allocateWorker_shouldFail_whenDatacenterIdOutOfRange() {
        when(properties.getPort()).thenReturn(9090);
        when(properties.getDatacenterId()).thenReturn(32L);
        when(leafWorkerRepository.findByIpAndPortAndDeleteStatusFalse(anyString(), eq(9090)))
                .thenReturn(Mono.empty());
        when(leafWorkerRepository.findByDeleteStatusFalseOrderByWorkerIdAsc())
                .thenReturn(Flux.empty());

        snowflakeWorkerService.init();

        assertThat(snowflakeWorkerService.isInitialized()).isFalse();
    }
}
