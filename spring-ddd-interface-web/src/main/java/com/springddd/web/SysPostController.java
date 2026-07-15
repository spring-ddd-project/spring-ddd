package com.springddd.web;

import com.springddd.application.service.post.SysPostCommandService;
import com.springddd.application.service.post.SysPostQueryService;
import com.springddd.application.service.post.dto.SysPostCommand;
import com.springddd.application.service.post.dto.SysPostQuery;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/sys/post")
@RequiredArgsConstructor
public class SysPostController {

    private final SysPostQueryService sysPostQueryService;

    private final SysPostCommandService sysPostCommandService;

    @PostMapping("/index")
    public Mono<ApiResponse> index(@RequestHeader(value = "X-Menu-Id", required = false) Long menuId,
                                       @RequestBody Mono<SysPostQuery> query) {
        return ApiResponse.validated(query, q -> sysPostQueryService.index(menuId, q));
    }

    @PostMapping("/recycle")
    public Mono<ApiResponse> recycle(@RequestHeader(value = "X-Menu-Id", required = false) Long menuId,
                                        @RequestBody Mono<SysPostQuery> query) {
        return ApiResponse.validated(query, q -> sysPostQueryService.recycle(menuId, q));
    }

    @PostMapping("/tree")
    public Mono<ApiResponse> tree() {
        return ApiResponse.ok(sysPostQueryService.postTree());
    }

    @PostMapping("/list")
    public Mono<ApiResponse> list() {
        return ApiResponse.ok(sysPostQueryService.queryAllPost());
    }

    @PostMapping("/create")
    public Mono<ApiResponse> create(@RequestBody SysPostCommand command) {
        return ApiResponse.ok(sysPostCommandService.create(command));
    }

    @PutMapping("/update")
    public Mono<ApiResponse> update(@RequestBody SysPostCommand command) {
        return ApiResponse.ok(sysPostCommandService.update(command));
    }

    @PostMapping("/delete")
    public Mono<ApiResponse> delete(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(sysPostCommandService.delete(ids));
    }

    @PostMapping("/restore")
    public Mono<ApiResponse> restore(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(sysPostCommandService.restore(ids));
    }

    @DeleteMapping("/wipe")
    public Mono<ApiResponse> wipe(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(sysPostCommandService.wipe(ids));
    }
}
