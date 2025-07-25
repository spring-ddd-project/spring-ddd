package com.springddd.web;

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

    @PostMapping("/index")
    public Mono<ApiResponse> tableIndex(@RequestBody @Validated Mono<GenTableInfoPageQuery> query) {
        return ApiResponse.validated(query, genTableInfoQueryService::index);
    }
}
