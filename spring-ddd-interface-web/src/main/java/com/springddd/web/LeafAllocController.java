package com.springddd.web;

import com.springddd.domain.util.ApiResponse;
import com.springddd.infrastructure.persistence.leaf.LeafSegmentBuffer;
import com.springddd.infrastructure.persistence.leaf.LeafSegmentIdGenerateDomainServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class LeafAllocController {

    private final LeafSegmentIdGenerateDomainServiceImpl leafSegmentIdGenerateDomainServiceImpl;

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
            currentSeg.put("value", buffer.getDisruptorLock().getCursor());
            currentSeg.put("max", buffer.getCurrent().getMax());
            currentSeg.put("step", buffer.getCurrent().getStep());
            map.put("currentSegment", currentSeg);

            Map<String, Object> nextSeg = new HashMap<>();
            nextSeg.put("value", buffer.getNext().getStart());
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
