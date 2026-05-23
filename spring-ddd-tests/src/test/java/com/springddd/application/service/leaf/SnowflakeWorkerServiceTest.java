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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

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

    // ---------- Reflection helpers ----------

    private String invokeGetLocalIp() throws Exception {
        Method method = SnowflakeWorkerService.class.getDeclaredMethod("getLocalIp");
        method.setAccessible(true);
        return (String) method.invoke(snowflakeWorkerService);
    }

    private void invokeStartHeartbeat() throws Exception {
        Method method = SnowflakeWorkerService.class.getDeclaredMethod("startHeartbeat");
        method.setAccessible(true);
        method.invoke(snowflakeWorkerService);
    }

    private void setWorkerRecordId(Long value) throws Exception {
        Field field = SnowflakeWorkerService.class.getDeclaredField("workerRecordId");
        field.setAccessible(true);
        field.set(snowflakeWorkerService, value);
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

    @Test
    @DisplayName("当存在超出范围的 workerId 时应过滤并分配可用的 workerId")
    void allocateWorker_shouldFilterOutOfRangeWorkerIds() {
        when(properties.getPort()).thenReturn(9090);
        when(leafWorkerRepository.findByIpAndPortAndDeleteStatusFalse(anyString(), eq(9090)))
                .thenReturn(Mono.empty());

        LeafWorkerEntity w0 = new LeafWorkerEntity();
        w0.setWorkerId(0);
        LeafWorkerEntity wOutOfRange = new LeafWorkerEntity();
        wOutOfRange.setWorkerId(100); // Out of valid range 0-31
        when(leafWorkerRepository.findByDeleteStatusFalseOrderByWorkerIdAsc())
                .thenReturn(Flux.just(w0, wOutOfRange));

        LeafWorkerEntity saved = new LeafWorkerEntity();
        saved.setId(2L);
        saved.setWorkerId(1);
        when(leafWorkerRepository.save(any(LeafWorkerEntity.class)))
                .thenReturn(Mono.just(saved));

        snowflakeWorkerService.init();

        assertThat(snowflakeWorkerService.isInitialized()).isTrue();
        assertThat(snowflakeWorkerService.getAssignedWorkerId()).isEqualTo(1);
    }

    // ---------- init() tests ----------

    @Test
    @DisplayName("init 当 allocateWorker 返回 null 时不应初始化")
    void init_whenWorkerAllocationReturnsNull_shouldNotInitialize() {
        when(properties.getPort()).thenReturn(9090);
        when(leafWorkerRepository.findByIpAndPortAndDeleteStatusFalse(anyString(), eq(9090)))
                .thenReturn(Mono.empty());
        when(leafWorkerRepository.findByDeleteStatusFalseOrderByWorkerIdAsc())
                .thenReturn(Flux.empty());
        when(leafWorkerRepository.save(any(LeafWorkerEntity.class)))
                .thenReturn(Mono.empty());

        snowflakeWorkerService.init();

        assertThat(snowflakeWorkerService.isInitialized()).isFalse();
    }

    @Test
    @DisplayName("init 当 allocateWorker 抛出异常时应捕获并记录错误")
    void init_whenExceptionThrown_shouldNotInitialize() {
        when(properties.getPort()).thenReturn(9090);
        when(leafWorkerRepository.findByIpAndPortAndDeleteStatusFalse(anyString(), eq(9090)))
                .thenReturn(Mono.error(new RuntimeException("db error")));

        snowflakeWorkerService.init();

        assertThat(snowflakeWorkerService.isInitialized()).isFalse();
    }

    // ---------- getLocalIp() tests ----------

    @Test
    @DisplayName("getLocalIp 应返回第一个非回环 IPv4 地址")
    void getLocalIp_shouldReturnFirstNonLoopbackIpv4() throws Exception {
        try (var mocked = mockStatic(NetworkInterface.class)) {
            Enumeration<NetworkInterface> ifaceEnum = mock(Enumeration.class);
            when(ifaceEnum.hasMoreElements()).thenReturn(true, false);
            NetworkInterface ni = mock(NetworkInterface.class);
            when(ifaceEnum.nextElement()).thenReturn(ni);

            Enumeration<InetAddress> addrEnum = mock(Enumeration.class);
            when(addrEnum.hasMoreElements()).thenReturn(true, false);
            InetAddress addr = mock(InetAddress.class);
            when(addr.isLoopbackAddress()).thenReturn(false);
            when(addr.getHostAddress()).thenReturn("192.168.1.100");
            when(addrEnum.nextElement()).thenReturn(addr);

            when(ni.getInetAddresses()).thenReturn(addrEnum);
            mocked.when(NetworkInterface::getNetworkInterfaces).thenReturn(ifaceEnum);

            assertThat(invokeGetLocalIp()).isEqualTo("192.168.1.100");
        }
    }

    @Test
    @DisplayName("getLocalIp 当没有网络接口时应返回空字符串")
    void getLocalIp_shouldReturnEmpty_whenNoInterfaces() throws Exception {
        try (var mocked = mockStatic(NetworkInterface.class)) {
            Enumeration<NetworkInterface> ifaceEnum = mock(Enumeration.class);
            when(ifaceEnum.hasMoreElements()).thenReturn(false);
            mocked.when(NetworkInterface::getNetworkInterfaces).thenReturn(ifaceEnum);

            assertThat(invokeGetLocalIp()).isEqualTo("");
        }
    }

    @Test
    @DisplayName("getLocalIp 当只有回环地址时应返回空字符串")
    void getLocalIp_shouldReturnEmpty_whenOnlyLoopback() throws Exception {
        try (var mocked = mockStatic(NetworkInterface.class)) {
            Enumeration<NetworkInterface> ifaceEnum = mock(Enumeration.class);
            when(ifaceEnum.hasMoreElements()).thenReturn(true, false);
            NetworkInterface ni = mock(NetworkInterface.class);
            when(ifaceEnum.nextElement()).thenReturn(ni);

            Enumeration<InetAddress> addrEnum = mock(Enumeration.class);
            when(addrEnum.hasMoreElements()).thenReturn(true, false);
            InetAddress addr = mock(InetAddress.class);
            when(addr.isLoopbackAddress()).thenReturn(true);
            when(addrEnum.nextElement()).thenReturn(addr);

            when(ni.getInetAddresses()).thenReturn(addrEnum);
            mocked.when(NetworkInterface::getNetworkInterfaces).thenReturn(ifaceEnum);

            assertThat(invokeGetLocalIp()).isEqualTo("");
        }
    }

    @Test
    @DisplayName("getLocalIp 当只有 IPv6 地址时应返回空字符串")
    void getLocalIp_shouldReturnEmpty_whenOnlyIpv6() throws Exception {
        try (var mocked = mockStatic(NetworkInterface.class)) {
            Enumeration<NetworkInterface> ifaceEnum = mock(Enumeration.class);
            when(ifaceEnum.hasMoreElements()).thenReturn(true, false);
            NetworkInterface ni = mock(NetworkInterface.class);
            when(ifaceEnum.nextElement()).thenReturn(ni);

            Enumeration<InetAddress> addrEnum = mock(Enumeration.class);
            when(addrEnum.hasMoreElements()).thenReturn(true, false);
            Inet6Address addr = mock(Inet6Address.class);
            when(addr.isLoopbackAddress()).thenReturn(false);
            when(addrEnum.nextElement()).thenReturn(addr);

            when(ni.getInetAddresses()).thenReturn(addrEnum);
            mocked.when(NetworkInterface::getNetworkInterfaces).thenReturn(ifaceEnum);

            assertThat(invokeGetLocalIp()).isEqualTo("");
        }
    }

    @Test
    @DisplayName("getLocalIp 当发生 SocketException 时应返回空字符串")
    void getLocalIp_shouldReturnEmpty_whenSocketException() throws Exception {
        try (var mocked = mockStatic(NetworkInterface.class)) {
            mocked.when(NetworkInterface::getNetworkInterfaces)
                    .thenThrow(new SocketException("test"));

            assertThat(invokeGetLocalIp()).isEqualTo("");
        }
    }

    @Test
    @DisplayName("getLocalIp 应跳过回环和 IPv6 并返回有效的 IPv4")
    void getLocalIp_shouldSkipLoopbackAndIpv6() throws Exception {
        try (var mocked = mockStatic(NetworkInterface.class)) {
            Enumeration<NetworkInterface> ifaceEnum = mock(Enumeration.class);
            when(ifaceEnum.hasMoreElements()).thenReturn(true, false);
            NetworkInterface ni = mock(NetworkInterface.class);
            when(ifaceEnum.nextElement()).thenReturn(ni);

            Enumeration<InetAddress> addrEnum = mock(Enumeration.class);
            when(addrEnum.hasMoreElements()).thenReturn(true, true, true, false);

            InetAddress loopback = mock(InetAddress.class);
            when(loopback.isLoopbackAddress()).thenReturn(true);

            Inet6Address ipv6 = mock(Inet6Address.class);
            when(ipv6.isLoopbackAddress()).thenReturn(false);

            InetAddress ipv4 = mock(InetAddress.class);
            when(ipv4.isLoopbackAddress()).thenReturn(false);
            when(ipv4.getHostAddress()).thenReturn("10.0.0.1");

            when(addrEnum.nextElement()).thenReturn(loopback, ipv6, ipv4);
            when(ni.getInetAddresses()).thenReturn(addrEnum);
            mocked.when(NetworkInterface::getNetworkInterfaces).thenReturn(ifaceEnum);

            assertThat(invokeGetLocalIp()).isEqualTo("10.0.0.1");
        }
    }

    // ---------- startHeartbeat tests ----------

    @Test
    @DisplayName("startHeartbeat 正常时应定期更新 lastTimestamp")
    void startHeartbeat_shouldUpdateTimestamp_whenNormal() throws Exception {
        when(properties.isEnabled()).thenReturn(true);
        setWorkerRecordId(1L);
        when(leafWorkerRepository.updateLastTimestamp(eq(1L), anyLong()))
                .thenReturn(Mono.just(1));

        invokeStartHeartbeat();

        Thread.sleep(3500);

        verify(leafWorkerRepository).updateLastTimestamp(eq(1L), anyLong());
    }

    @Test
    @DisplayName("startHeartbeat 当更新失败时应处理错误并继续")
    void startHeartbeat_shouldHandleError_whenUpdateFails() throws Exception {
        when(properties.isEnabled()).thenReturn(true);
        setWorkerRecordId(1L);
        when(leafWorkerRepository.updateLastTimestamp(eq(1L), anyLong()))
                .thenReturn(Mono.error(new RuntimeException("db error")));

        invokeStartHeartbeat();

        Thread.sleep(3500);

        verify(leafWorkerRepository).updateLastTimestamp(eq(1L), anyLong());
    }

    @Test
    @DisplayName("startHeartbeat 当 workerRecordId 为 null 时不应更新")
    void startHeartbeat_shouldNotUpdate_whenWorkerRecordIdIsNull() throws Exception {
        when(properties.isEnabled()).thenReturn(true);
        // workerRecordId is null by default

        invokeStartHeartbeat();

        Thread.sleep(3500);

        verifyNoInteractions(leafWorkerRepository);
    }
}
