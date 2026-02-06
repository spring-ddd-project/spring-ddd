package com.springddd.web;

import com.springddd.application.service.gen.GenTableInfoCommandService;
import com.springddd.application.service.gen.GenTableInfoQueryService;
import com.springddd.application.service.gen.dto.GenTableInfoPageQuery;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/gen/table")
@RequiredArgsConstructor
public class GenTableController {

    private final GenTableInfoQueryService genTableInfoQueryService;

    private final GenTableInfoCommandService genTableInfoCommandService;

    @PostMapping("/index")
    public Mono<ApiResponse> tableIndex(@RequestBody @Validated Mono<GenTableInfoPageQuery> query) {
        return ApiResponse.validated(query, genTableInfoQueryService::index);
    }

    @DeleteMapping("/wipe")
    public Mono<ApiResponse> wipe() {
        return ApiResponse.ok(genTableInfoCommandService.wipe());
    }

    @PostMapping("/generate")
    public Mono<ApiResponse> generate(@RequestParam("tableName") String tableName) {
        return ApiResponse.ok(genTableInfoQueryService.generate(tableName));
    }

}
