package com.springddd.web;

import com.springddd.application.service.gen.GenAggregateCommandService;
import com.springddd.application.service.gen.GenAggregateQueryService;
import com.springddd.application.service.gen.dto.GenAggregateCommand;
import com.springddd.application.service.gen.dto.GenAggregatePageQuery;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gen/aggregate")
public class GenAggregateController {

    private final GenAggregateCommandService commandService;

    private final GenAggregateQueryService queryService;

    @PostMapping("/index")
    public Mono<ApiResponse> index(@RequestBody @Validated Mono<GenAggregatePageQuery> query) {
        return ApiResponse.validated(query, queryService::index);
    }

    @PostMapping("/create")
    public Mono<ApiResponse> create(@RequestBody GenAggregateCommand command) {
        return ApiResponse.ok(commandService.create(command));
    }

    @PutMapping("/update")
    public Mono<ApiResponse> update(@RequestBody GenAggregateCommand command) {
        return ApiResponse.ok(commandService.update(command));
    }

    @DeleteMapping("/wipe")
    public Mono<ApiResponse> wipe(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(commandService.wipe(ids));
    }
}
