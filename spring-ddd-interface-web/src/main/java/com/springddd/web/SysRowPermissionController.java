package com.springddd.web;

import com.springddd.application.service.permission.row.RowPermissionCommandService;
import com.springddd.application.service.permission.row.RowPermissionQueryService;
import com.springddd.application.service.permission.row.dto.RowPermissionSaveCommand;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/sys/row-permission")
@RequiredArgsConstructor
public class SysRowPermissionController {

    private final RowPermissionCommandService rowPermissionCommandService;

    private final RowPermissionQueryService rowPermissionQueryService;

    @GetMapping("/list")
    public Mono<ApiResponse> list(@RequestParam Long roleId, @RequestParam Long menuId) {
        return ApiResponse.ok(rowPermissionQueryService.list(roleId, menuId));
    }

    @PostMapping("/save")
    public Mono<ApiResponse> save(@RequestBody RowPermissionSaveCommand command) {
        return ApiResponse.ok(rowPermissionCommandService.save(command));
    }

    @PostMapping("/delete")
    public Mono<ApiResponse> delete(@RequestParam Long id) {
        return ApiResponse.ok(rowPermissionCommandService.delete(id));
    }
}
