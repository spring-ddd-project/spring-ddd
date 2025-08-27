package com.springddd.web;

import com.springddd.application.service.gen.GenTemplateCommandService;
import com.springddd.application.service.gen.GenTemplateQueryService;
import com.springddd.application.service.gen.dto.GenTemplateCommand;
import com.springddd.application.service.gen.dto.GenTemplatePageQuery;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gen/template")
public class GenTemplateController {

    private final GenTemplateQueryService genTemplateQueryService;

    private final GenTemplateCommandService genTemplateCommandService;

    @PostMapping("/index")
    public Mono<ApiResponse> index(@RequestBody @Validated Mono<GenTemplatePageQuery> query) {
        return ApiResponse.validated(query, genTemplateQueryService::index);
    }

    @PostMapping("/create")
    public Mono<ApiResponse> create(@RequestBody GenTemplateCommand command) {
        return ApiResponse.ok(genTemplateCommandService.create(command));
    }

    @PutMapping("/update")
    public Mono<ApiResponse> update(@RequestBody GenTemplateCommand command) {
        return ApiResponse.ok(genTemplateCommandService.update(command));
    }

    @PostMapping("/delete")
    public Mono<ApiResponse> delete(@RequestParam("ids")List<Long> ids) {
        return ApiResponse.ok(genTemplateCommandService.delete(ids));
    }

    @DeleteMapping("/wipe")
    public Mono<ApiResponse> wipe(@RequestParam("ids")List<Long> ids) {
        return ApiResponse.ok(genTemplateCommandService.wipe(ids));
    }
}
