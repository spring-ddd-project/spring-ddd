package com.springddd.web;

import com.springddd.application.service.gen.GenColumnsCommandService;
import com.springddd.application.service.gen.GenColumnsQueryService;
import com.springddd.application.service.gen.dto.GenColumnsCommand;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/gen/columns")
@RequiredArgsConstructor
public class GenColumnsController {

    private final GenColumnsQueryService genColumnsQueryService;

    private final GenColumnsCommandService genColumnsCommandService;

    @PostMapping("/queryJavaEntityInfoByInfoId")
    public Mono<ApiResponse> getJavaEntityInfoByInfoId(@RequestParam("infoId") Long infoId) {
        return ApiResponse.ok(genColumnsQueryService.queryJavaEntityInfoByInfoId(infoId));
    }

    @PostMapping("/queryByInfoId")
    public Mono<ApiResponse> getByInfoId(@RequestParam("infoId") Long infoId,
                                         @RequestParam("databaseName") String databaseName) {
        return ApiResponse.ok(genColumnsQueryService.queryColumnsByGenInfoId(infoId, databaseName));
    }

    @PostMapping("/create")
    public Mono<ApiResponse> create(@RequestBody GenColumnsCommand command) {
        return ApiResponse.ok(genColumnsCommandService.create(command));
    }

    @PostMapping("/batchCreate")
    public Mono<ApiResponse> batchCreate(@RequestBody List<GenColumnsCommand> commands) {
        return ApiResponse.ok(genColumnsCommandService.batchSave(commands));
    }

    @PutMapping("/batchUpdate")
    public Mono<ApiResponse> batchUpdate(@RequestBody List<GenColumnsCommand> commands) {
        return ApiResponse.ok(genColumnsCommandService.batchUpdate(commands));
    }

    @PutMapping("/update")
    public Mono<ApiResponse> update(@RequestBody GenColumnsCommand command) {
        return ApiResponse.ok(genColumnsCommandService.update(command));
    }

    @PostMapping("/delete")
    public Mono<ApiResponse> delete(@RequestBody GenColumnsCommand command) {
        return ApiResponse.ok(genColumnsCommandService.delete(command));
    }

    @DeleteMapping("/wipe")
    public Mono<ApiResponse> wipe(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(genColumnsCommandService.wipe(ids));
    }
}
