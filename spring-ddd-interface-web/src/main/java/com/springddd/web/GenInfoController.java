package com.springddd.web;

import com.springddd.application.service.gen.GenInfoCommandService;
import com.springddd.application.service.gen.GenInfoQueryService;
import com.springddd.application.service.gen.dto.GenInfoCommand;
import com.springddd.application.service.gen.dto.GenInfoPageQuery;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/gen/info")
@RequiredArgsConstructor
public class GenInfoController {

    private final GenInfoQueryService genInfoQueryService;

    private final GenInfoCommandService genInfoCommandService;

    @PostMapping("/info")
    public Mono<ApiResponse> index(@RequestBody @Validated Mono<GenInfoPageQuery> query) {
        return ApiResponse.validated(query, genInfoQueryService::index);
    }

    @PostMapping("/queryInfoByTableName")
    public Mono<ApiResponse> getInfo(@RequestParam("tableName") String tableName) {
        return ApiResponse.ok(genInfoQueryService.queryGenInfoByTableName(tableName));
    }

    @PostMapping("/create")
    public Mono<ApiResponse> create(@RequestBody GenInfoCommand command) {
        return ApiResponse.ok(genInfoCommandService.create(command));
    }

    @PutMapping("/update")
    public Mono<ApiResponse> update(@RequestBody GenInfoCommand command) {
        return ApiResponse.ok(genInfoCommandService.update(command));
    }

    @PostMapping("/delete")
    public Mono<ApiResponse> delete(@RequestBody GenInfoCommand command) {
        return ApiResponse.ok(genInfoCommandService.delete(command));
    }

    @DeleteMapping("/wipe")
    public Mono<ApiResponse> wipe(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(genInfoCommandService.wipeByIds(ids));
    }
}
