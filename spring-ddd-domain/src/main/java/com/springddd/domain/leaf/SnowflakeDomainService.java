package com.springddd.domain.leaf;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface SnowflakeDomainService {

    Mono<Long> getId();

    Mono<Map<String, Object>> decodeId(long snowflakeId);
}
