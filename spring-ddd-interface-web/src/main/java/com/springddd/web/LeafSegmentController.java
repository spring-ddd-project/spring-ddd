package com.springddd.web;

import com.springddd.domain.leaf.LeafSegmentDomainService;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/leaf/segment")
@RequiredArgsConstructor
public class LeafSegmentController {

    private final LeafSegmentDomainService leafSegmentDomainService;

    @PostMapping("/get/{bizTag}")
    public Mono<ApiResponse> getId(@PathVariable("bizTag") String bizTag) {
        return ApiResponse.ok(leafSegmentDomainService.getId(bizTag));
    }

    @GetMapping("/cache")
    public Mono<ApiResponse> cache() {
        return ApiResponse.ok(leafSegmentDomainService.getCacheStatus());
    }
}
