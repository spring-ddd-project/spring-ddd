package com.springddd.web;

import com.springddd.application.service.dict.SysDictItemCommandService;
import com.springddd.application.service.dict.SysDictItemQueryService;
import com.springddd.application.service.dict.dto.SysDictItemCommand;
import com.springddd.application.service.dict.dto.SysDictItemPageQuery;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/sys/dict/item")
@RequiredArgsConstructor
public class SysDictItemController {

    private final SysDictItemQueryService sysDictItemQueryService;

    private final SysDictItemCommandService sysDictItemCommandService;

    @PostMapping("/index")
    public Mono<ApiResponse> index(@RequestBody @Validated Mono<SysDictItemPageQuery> query) {
        return ApiResponse.validated(query, sysDictItemQueryService::index);
    }

    @PostMapping("/getItemLabelByCode")
    public Mono<ApiResponse> getItemLabelByCode(@RequestParam("dictCode") String dictCode) {
        return ApiResponse.ok(sysDictItemQueryService.queryItemLabelByDictCode(dictCode));
    }

    @PostMapping("/recycle")
    public Mono<ApiResponse> recycle(@RequestBody @Validated Mono<SysDictItemPageQuery> query) {
        return ApiResponse.validated(query, sysDictItemQueryService::recycle);
    }

    @PostMapping("/create")
    public Mono<ApiResponse> create(@RequestBody SysDictItemCommand command) {
        return ApiResponse.ok(sysDictItemCommandService.create(command));
    }

    @PutMapping("/update")
    public Mono<ApiResponse> update(@RequestBody SysDictItemCommand command) {
        return ApiResponse.ok(sysDictItemCommandService.update(command));
    }

    @PostMapping("/delete")
    public Mono<ApiResponse> delete(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(sysDictItemCommandService.delete(ids));
    }

    @PostMapping("/restore")
    public Mono<ApiResponse> restore(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(sysDictItemCommandService.restore(ids));
    }

    @DeleteMapping("/wipe")
    public Mono<ApiResponse> wipe(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(sysDictItemCommandService.wipe(ids));
    }
}
