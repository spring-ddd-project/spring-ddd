package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.SnowflakeDomainService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SnowflakeDomainServiceImpl implements SnowflakeDomainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnowflakeDomainServiceImpl.class);

    private final SnowflakeProperties properties;
    private final SnowflakeWorkerService snowflakeWorkerService;

    private static final long WORKER_ID_BITS = 5L;
    private static final long DATACENTER_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;

    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    private static final Random RANDOM = new Random();

    private long twepoch;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    @PostConstruct
    public void init() {
        if (!properties.isEnabled()) {
            LOGGER.info("Snowflake service is disabled");
            return;
        }
        this.twepoch = properties.getTwepoch();
        LOGGER.info("Snowflake domain service initialized. twepoch={}", twepoch);
    }

    @Override
    public Mono<Long> getId() {
        if (!properties.isEnabled()) {
            return Mono.error(new IllegalStateException("Snowflake service is not enabled"));
        }
        if (!snowflakeWorkerService.isInitialized()) {
            return Mono.error(new IllegalStateException("Snowflake worker is not initialized yet"));
        }
        return Mono.fromCallable(this::nextId)
                .subscribeOn(Schedulers.single());
    }

    private synchronized long nextId() {
        long workerId = snowflakeWorkerService.getAssignedWorkerId();
        long datacenterId = snowflakeWorkerService.getAssignedDatacenterId();

        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            long offset = lastTimestamp - timestamp;
            if (offset <= 5) {
                try {
                    wait(offset << 1);
                    timestamp = timeGen();
                    if (timestamp < lastTimestamp) {
                        throw new RuntimeException("Clock moved backwards. Refusing to generate id");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Wait interrupted", e);
                }
            } else {
                throw new RuntimeException("Clock moved backwards. Refusing to generate id");
            }
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                sequence = RANDOM.nextInt(100);
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = RANDOM.nextInt(100);
        }

        lastTimestamp = timestamp;
        return ((timestamp - twepoch) << TIMESTAMP_LEFT_SHIFT)
                | (datacenterId << DATACENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    @Override
    public Mono<Map<String, Object>> decodeId(long snowflakeId) {
        return Mono.fromCallable(() -> {
            Map<String, Object> map = new LinkedHashMap<>();
            long originTimestamp = (snowflakeId >> 22) + twepoch;
            Date date = new Date(originTimestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            map.put("timestamp", originTimestamp + "(" + sdf.format(date) + ")");
            map.put("datacenterId", String.valueOf((snowflakeId >> 17) & 0x1F));
            map.put("workerId", String.valueOf((snowflakeId >> 12) & 0x1F));
            map.put("sequenceId", String.valueOf(snowflakeId & 0xFFF));
            return map;
        });
    }
}
