package com.springddd.web;

import com.springddd.domain.leaf.SnowflakeDomainService;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/leaf/snowflake")
@RequiredArgsConstructor
public class SnowflakeController {

    private final SnowflakeDomainService snowflakeDomainService;

    @PostMapping("/get")
    public Mono<ApiResponse> getId() {
        return ApiResponse.ok(snowflakeDomainService.getId());
    }

    @PostMapping("/decode")
    public Mono<ApiResponse> decodeId(@RequestParam("snowflakeId") long snowflakeId) {
        return ApiResponse.ok(snowflakeDomainService.decodeId(snowflakeId));
    }
}
