package com.springddd.web;

import com.springddd.application.service.dict.SysDictCommandService;
import com.springddd.application.service.dict.SysDictQueryService;
import com.springddd.application.service.dict.dto.SysDictCommand;
import com.springddd.application.service.dict.dto.SysDictPageQuery;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/sys/dict")
@RequiredArgsConstructor
public class SysDictController {

    private final SysDictCommandService sysDictCommandService;

    private final SysDictQueryService sysDictQueryService;

    @PostMapping("/index")
    public Mono<ApiResponse> index(@RequestBody @Validated Mono<SysDictPageQuery> query) {
        return ApiResponse.validated(query, sysDictQueryService::index);
    }

    @PostMapping("/create")
    public Mono<ApiResponse> create(@RequestBody SysDictCommand command) {
        return ApiResponse.ok(sysDictCommandService.create(command));
    }

    @PutMapping("/update")
    public Mono<ApiResponse> update(@RequestBody SysDictCommand command) {
        return ApiResponse.ok(sysDictCommandService.update(command));
    }

    @PostMapping("/delete")
    public Mono<ApiResponse> delete(@RequestBody SysDictCommand command) {
        return ApiResponse.ok(sysDictCommandService.delete(command));
    }

    @DeleteMapping("/wipe")
    public Mono<ApiResponse> wipe(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(sysDictCommandService.wipe(ids));
    }
}
