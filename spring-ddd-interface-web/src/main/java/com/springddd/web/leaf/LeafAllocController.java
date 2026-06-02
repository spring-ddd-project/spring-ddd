package com.springddd.web.leaf;

import com.springddd.application.service.leaf.LeafAllocCommandService;
import com.springddd.application.service.leaf.LeafAllocQueryService;
import com.springddd.application.service.leaf.dto.LeafAllocCommand;
import com.springddd.application.service.leaf.dto.LeafAllocPageQuery;
import com.springddd.domain.util.ApiResponse;
import com.springddd.infrastructure.persistence.leaf.LeafSegmentBuffer;
import com.springddd.infrastructure.persistence.leaf.LeafSegmentIdGenerateDomainServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class LeafAllocController {

    private final LeafAllocCommandService leafAllocCommandService;
    private final LeafAllocQueryService leafAllocQueryService;
    private final LeafSegmentIdGenerateDomainServiceImpl leafSegmentIdGenerateDomainServiceImpl;

    // ===== Admin CRUD endpoints =====

    @PostMapping("/sys/leaf/index")
    public Mono<ApiResponse> page(@RequestBody @Validated Mono<LeafAllocPageQuery> query) {
        return ApiResponse.validated(query, leafAllocQueryService::page);
    }

    @PostMapping("/sys/leaf/recycle")
    public Mono<ApiResponse> recyclePage(@RequestBody @Validated Mono<LeafAllocPageQuery> query) {
        return ApiResponse.validated(query, leafAllocQueryService::recycle);
    }

    @PostMapping("/sys/leaf/create")
    public Mono<ApiResponse> create(@RequestBody LeafAllocCommand command) {
        return ApiResponse.ok(leafAllocCommandService.create(command));
    }

    @PutMapping("/sys/leaf/update")
    public Mono<ApiResponse> update(@RequestBody LeafAllocCommand command) {
        return ApiResponse.ok(leafAllocCommandService.update(command));
    }

    @PostMapping("/sys/leaf/delete")
    public Mono<ApiResponse> delete(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(Mono.when(ids.stream().map(leafAllocCommandService::delete).toArray(Mono[]::new)).then());
    }

    @DeleteMapping("/sys/leaf/wipe")
    public Mono<ApiResponse> wipe(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(Mono.when(ids.stream().map(leafAllocCommandService::wipe).toArray(Mono[]::new)).then());
    }

    @PostMapping("/sys/leaf/restore")
    public Mono<ApiResponse> restore(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(Mono.when(ids.stream().map(leafAllocCommandService::restore).toArray(Mono[]::new)).then());
    }

    // ===== Leaf native API endpoints =====

    @GetMapping("/api/leaf/segment/get/{key}")
    public Mono<String> getSegmentId(@PathVariable("key") String key) {
        return leafSegmentIdGenerateDomainServiceImpl.nextId(key).map(String::valueOf);
    }

    @GetMapping("/api/leaf/segment/cache")
    public Mono<ApiResponse> getCacheStatus() {
        Map<String, Object> result = new HashMap<>();
        Map<String, LeafSegmentBuffer> cache = leafSegmentIdGenerateDomainServiceImpl.getBufferCache();

        List<Map<String, Object>> bufferList = cache.entrySet().stream().map(entry -> {
            Map<String, Object> map = new HashMap<>();
            LeafSegmentBuffer buffer = entry.getValue();
            map.put("bizTag", entry.getKey());
            map.put("currentPos", buffer.getCurrentPos());
            map.put("initOk", buffer.isInitOk());
            map.put("nextReady", buffer.isNextReady());
            map.put("threadRunning", buffer.getThreadRunning().get());

            Map<String, Object> currentSeg = new HashMap<>();
            currentSeg.put("value", buffer.getCurrent().getValue().get());
            currentSeg.put("max", buffer.getCurrent().getMax());
            currentSeg.put("step", buffer.getCurrent().getStep());
            map.put("currentSegment", currentSeg);

            Map<String, Object> nextSeg = new HashMap<>();
            nextSeg.put("value", buffer.getNext().getValue().get());
            nextSeg.put("max", buffer.getNext().getMax());
            nextSeg.put("step", buffer.getNext().getStep());
            map.put("nextSegment", nextSeg);

            return map;
        }).collect(Collectors.toList());

        result.put("buffers", bufferList);
        return Mono.just(ApiResponse.success(result));
    }

    @GetMapping("/api/leaf/segment/db")
    public Mono<ApiResponse> getDbStatus() {
        return leafSegmentIdGenerateDomainServiceImpl.findAllAlloc()
                .map(ApiResponse::success);
    }
}
