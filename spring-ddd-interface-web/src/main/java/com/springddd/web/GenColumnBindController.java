package com.springddd.web;

import com.springddd.application.service.gen.GenColumnBindCommandService;
import com.springddd.application.service.gen.GenColumnBindQueryService;
import com.springddd.application.service.gen.dto.GenColumnBindCommand;
import com.springddd.application.service.gen.dto.GenColumnBindPageQuery;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/gen/column/bind")
@RequiredArgsConstructor
public class GenColumnBindController {

    private final GenColumnBindQueryService queryService;

    private final GenColumnBindCommandService commandService;

    @PostMapping("/index")
    public Mono<ApiResponse> index(@RequestBody @Validated Mono<GenColumnBindPageQuery> query) {
        return ApiResponse.validated(query, queryService::index);
    }

    @PostMapping("/recycle")
    public Mono<ApiResponse> recycle(@RequestBody @Validated Mono<GenColumnBindPageQuery> query) {
        return ApiResponse.validated(query, queryService::recycle);
    }

    @PostMapping("/create")
    public Mono<ApiResponse> create(@RequestBody GenColumnBindCommand command) {
        return ApiResponse.ok(commandService.create(command));
    }

    @PutMapping("/update")
    public Mono<ApiResponse> update(@RequestBody GenColumnBindCommand command) {
        return ApiResponse.ok(commandService.update(command));
    }

    @PostMapping("/delete")
    public Mono<ApiResponse> delete(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(commandService.delete(ids));
    }

    @DeleteMapping("/wipe")
    public Mono<ApiResponse> wipe(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(commandService.wipe(ids));
    }

    @PostMapping("/restore")
    public Mono<ApiResponse> restore(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(commandService.restore(ids));
    }
}
