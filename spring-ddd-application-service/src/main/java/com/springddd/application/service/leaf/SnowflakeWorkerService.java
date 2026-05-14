package com.springddd.application.service.leaf;

import com.springddd.infrastructure.persistence.entity.LeafWorkerEntity;
import com.springddd.infrastructure.persistence.r2dbc.LeafWorkerRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnowflakeWorkerService {

    private final LeafWorkerRepository leafWorkerRepository;
    private final SnowflakeProperties properties;

    @Getter
    private volatile long assignedWorkerId = -1;
    @Getter
    private volatile long assignedDatacenterId = -1;
    private volatile Long workerRecordId = null;

    private static final long MAX_WORKER_ID = 31L;
    private static final long MAX_DATACENTER_ID = 31L;
    private static final long HEARTBEAT_INTERVAL_MS = 3000L;
    private static final long WORKER_EXPIRE_MS = 15000L;

    @PostConstruct
    public void init() {
        if (!properties.isEnabled()) {
            log.info("Snowflake service is disabled, skipping worker initialization");
            return;
        }

        String ip = getLocalIp();
        int port = properties.getPort();

        try {
            LeafWorkerEntity worker = allocateWorker(ip, port).block();
            if (worker != null) {
                this.assignedWorkerId = worker.getWorkerId();
                this.assignedDatacenterId = worker.getDatacenterId();
                this.workerRecordId = worker.getId();
                log.info("Snowflake worker allocated: workerId={}, datacenterId={}, ip={}, port={}",
                        assignedWorkerId, assignedDatacenterId, ip, port);
                startHeartbeat();
            } else {
                log.error("Failed to allocate snowflake worker");
            }
        } catch (Exception e) {
            log.error("Failed to initialize snowflake worker", e);
        }
    }

    private Mono<LeafWorkerEntity> allocateWorker(String ip, int port) {
        return leafWorkerRepository.findByIpAndPortAndDeleteStatusFalse(ip, port)
                .flatMap(existing -> {
                    log.info("Reusing existing worker: workerId={}", existing.getWorkerId());
                    return leafWorkerRepository.updateLastTimestamp(existing.getId(), System.currentTimeMillis())
                            .then(Mono.just(existing));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("No existing worker found for ip={}, port={}, allocating new worker", ip, port);
                    return findAvailableWorkerId()
                            .flatMap(workerId -> {
                                LeafWorkerEntity entity = new LeafWorkerEntity();
                                entity.setWorkerId(workerId);
                                entity.setDatacenterId((int) properties.getDatacenterId());
                                entity.setIp(ip);
                                entity.setPort(port);
                                entity.setLastTimestamp(System.currentTimeMillis());
                                entity.setDeleteStatus(false);
                                return leafWorkerRepository.save(entity);
                            });
                }));
    }

    private Mono<Integer> findAvailableWorkerId() {
        return leafWorkerRepository.findByDeleteStatusFalseOrderByWorkerIdAsc()
                .map(LeafWorkerEntity::getWorkerId)
                .collectList()
                .flatMap(usedIds -> {
                    int targetDatacenterId = (int) properties.getDatacenterId();
                    if (targetDatacenterId < 0 || targetDatacenterId > MAX_DATACENTER_ID) {
                        return Mono.error(new IllegalArgumentException(
                                "datacenterId must be between 0 and " + MAX_DATACENTER_ID));
                    }
                    List<Integer> filteredIds = usedIds.stream()
                            .filter(id -> id >= 0 && id <= MAX_WORKER_ID)
                            .sorted()
                            .toList();
                    int newWorkerId = 0;
                    for (int usedId : filteredIds) {
                        if (usedId > newWorkerId) {
                            break;
                        }
                        newWorkerId = usedId + 1;
                    }
                    if (newWorkerId > MAX_WORKER_ID) {
                        return Mono.error(new IllegalStateException(
                                "No available workerId in range 0-" + MAX_WORKER_ID));
                    }
                    return Mono.just(newWorkerId);
                });
    }

    private void startHeartbeat() {
        Flux.interval(Duration.ofMillis(HEARTBEAT_INTERVAL_MS))
                .flatMap(i -> {
                    if (workerRecordId != null && properties.isEnabled() && leafWorkerRepository != null) {
                        Mono<Integer> update = leafWorkerRepository.updateLastTimestamp(workerRecordId, System.currentTimeMillis());
                        if (update != null) {
                            return update.doOnError(e -> log.warn("Heartbeat update failed", e))
                                    .onErrorResume(e -> Mono.empty());
                        }
                    }
                    return Mono.empty();
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }

    public Mono<Void> cleanupExpiredWorkers() {
        long expiredTimestamp = System.currentTimeMillis() - WORKER_EXPIRE_MS;
        return leafWorkerRepository.markExpiredWorkers(expiredTimestamp)
                .doOnNext(count -> log.info("Cleaned up {} expired workers", count))
                .then();
    }

    public boolean isInitialized() {
        return assignedWorkerId >= 0;
    }

    private String getLocalIp() {
        try {
            List<String> ipList = new ArrayList<>();
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address.isLoopbackAddress() || address instanceof Inet6Address) {
                        continue;
                    }
                    ipList.add(address.getHostAddress());
                }
            }
            return ipList.isEmpty() ? "" : ipList.get(0);
        } catch (SocketException e) {
            log.warn("Failed to get local IP", e);
            return "";
        }
    }
}
