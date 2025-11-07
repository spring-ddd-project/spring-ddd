package com.springddd.web;

import com.springddd.application.service.leaf.LeafAllocCommandService;
import com.springddd.application.service.leaf.LeafAllocQueryService;
import com.springddd.application.service.leaf.dto.LeafAllocCommand;
import com.springddd.application.service.leaf.dto.LeafAllocPageQuery;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/leaf/leafAlloc")
public class LeafAllocController {

    private final LeafAllocQueryService queryService;

    private final LeafAllocCommandService commandService;

    @PostMapping("/index")
    public Mono<ApiResponse> index(@RequestBody @Validated Mono<LeafAllocPageQuery> query) {
        return ApiResponse.validated(query, queryService::index);
    }

    @PostMapping("/recycle")
    public Mono<ApiResponse> recycle(@RequestBody @Validated Mono<LeafAllocPageQuery> query) {
        return ApiResponse.validated(query, queryService::recycle);
    }

    @PostMapping("/create")
    public Mono<ApiResponse> create(@RequestBody LeafAllocCommand command) {
        return ApiResponse.ok(commandService.create(command));
    }

    @PutMapping("/update")
    public Mono<ApiResponse> update(@RequestBody LeafAllocCommand command) {
        return ApiResponse.ok(commandService.update(command));
    }

    @PostMapping("/delete")
    public Mono<ApiResponse> delete(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(commandService.delete(ids));
    }

    @PostMapping("/restore")
    public Mono<ApiResponse> restore(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(commandService.restore(ids));
    }

    @DeleteMapping("/wipe")
    public Mono<ApiResponse> wipe(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(commandService.wipe(ids));
    }
}
