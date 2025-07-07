package com.springddd.web;


import com.springddd.application.service.role.SysRoleCommandService;
import com.springddd.application.service.role.SysRoleQueryService;
import com.springddd.application.service.role.dto.SysRoleCommand;
import com.springddd.application.service.role.dto.SysRoleQuery;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/sys/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleCommandService sysRoleCommandService;

    private final SysRoleQueryService sysRoleQueryService;

    @PostMapping("/index")
    public Mono<ApiResponse> page(@Validated @RequestBody Mono<SysRoleQuery> query) {
        return ApiResponse.validated(query,sysRoleQueryService::page);
    }

    @PostMapping("/all")
    public Mono<ApiResponse> all() {
        return ApiResponse.ok(sysRoleQueryService.getAllRole());
    }

    @PostMapping("/create")
    public Mono<ApiResponse> create(@RequestBody SysRoleCommand command) {
        return ApiResponse.ok(sysRoleCommandService.createRole(command));
    }

    @PutMapping("/update")
    public Mono<ApiResponse> update(@RequestBody SysRoleCommand command) {
        return ApiResponse.ok(sysRoleCommandService.updateRole(command));
    }

    @PostMapping("/delete")
    public Mono<ApiResponse> delete(@RequestBody SysRoleCommand command) {
        return ApiResponse.ok(sysRoleCommandService.deleteRole(command));
    }

    @DeleteMapping("/wipe")
    public Mono<ApiResponse> wipe(@RequestParam List<Long> ids) {
        return ApiResponse.ok(sysRoleCommandService.wipe(ids));
    }
}
