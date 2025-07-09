package com.springddd.web;

import com.springddd.application.service.gen.GenProjectInfoCommandService;
import com.springddd.application.service.gen.GenProjectInfoQueryService;
import com.springddd.application.service.gen.dto.GenProjectInfoCommand;
import com.springddd.application.service.gen.dto.GenProjectInfoQuery;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/gen/projectInfo")
@RequiredArgsConstructor
public class GenProjectInfoController {

    private final GenProjectInfoQueryService genProjectInfoQueryService;

    private final GenProjectInfoCommandService genProjectInfoCommandService;

    @PostMapping("/index")
    public Mono<ApiResponse> index(@RequestBody GenProjectInfoQuery query) {
        return ApiResponse.ok(genProjectInfoQueryService.index(query));
    }

    @PostMapping("/queryInfoByTableName")
    public Mono<ApiResponse> getInfo(@RequestParam("tableName") String tableName) {
        return ApiResponse.ok(genProjectInfoQueryService.queryGenInfoByTableName(tableName));
    }

    @PostMapping("/create")
    public Mono<ApiResponse> create(@RequestBody GenProjectInfoCommand command) {
        return ApiResponse.ok(genProjectInfoCommandService.create(command));
    }

    @PutMapping("/update")
    public Mono<ApiResponse> update(@RequestBody GenProjectInfoCommand command) {
        return ApiResponse.ok(genProjectInfoCommandService.update(command));
    }

    @PostMapping("/delete")
    public Mono<ApiResponse> delete(@RequestBody GenProjectInfoCommand command) {
        return ApiResponse.ok(genProjectInfoCommandService.delete(command));
    }

    @DeleteMapping("/wipe")
    public Mono<ApiResponse> wipe(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(genProjectInfoCommandService.wipeByIds(ids));
    }
}
