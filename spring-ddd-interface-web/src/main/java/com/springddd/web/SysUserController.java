package com.springddd.web;

import com.springddd.application.service.user.SysUserCommandService;
import com.springddd.application.service.user.SysUserQueryService;
import com.springddd.application.service.user.SysUserRoleCommandService;
import com.springddd.application.service.user.SysUserRoleQueryService;
import com.springddd.application.service.user.dto.SysUserCommand;
import com.springddd.application.service.user.dto.SysUserQuery;
import com.springddd.application.service.user.dto.SysUserRoleCommand;
import com.springddd.application.service.user.dto.SysUserRoleQuery;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/sys/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserCommandService sysUserCommandService;

    private final SysUserQueryService sysUserQueryService;

    private final SysUserRoleCommandService sysUserRoleCommandService;
    private final SysUserRoleQueryService sysUserRoleQueryService;

    @PostMapping("/page")
    public Mono<ApiResponse> page(@RequestBody @Validated Mono<SysUserQuery> query) {
        return ApiResponse.ok(query, sysUserQueryService::page);
    }

    @PostMapping("/create")
    public Mono<ApiResponse> create(@RequestBody SysUserCommand command) {
        return ApiResponse.ok(sysUserCommandService.createUser(command));
    }

    @PostMapping("/update")
    public Mono<ApiResponse> update(@RequestBody SysUserCommand command) {
        return ApiResponse.ok(sysUserCommandService.updateUser(command));
    }

    @DeleteMapping("/delete")
    public Mono<ApiResponse> delete(@RequestBody SysUserCommand command) {
        return ApiResponse.ok(sysUserCommandService.deleteUser(command));
    }

    @DeleteMapping("/wipe")
    public Mono<ApiResponse> wipe(@RequestParam List<Long> ids) {
        return ApiResponse.ok(sysUserCommandService.wipe(ids));
    }

    @PostMapping("/queryRolesByUserId")
    public Mono<ApiResponse> queryRolesByUserId(@RequestParam("userId") Long userId) {
        return ApiResponse.ok(sysUserRoleQueryService.queryLinkUserAndRole(userId));
    }

    @PostMapping("/linkUserAndRole")
    public Mono<ApiResponse> linkUserAndRole(@RequestBody SysUserRoleCommand command) {
        return ApiResponse.ok(sysUserRoleCommandService.create(command));
    }

    @PutMapping("/updateLinkUserAndRole")
    public Mono<ApiResponse> updateLinkUserAndRole(@RequestBody SysUserRoleCommand command) {
        return ApiResponse.ok(sysUserRoleCommandService.update(command));
    }

    @DeleteMapping("/deleteLinkUserAndRole")
    public Mono<ApiResponse> deleteLinkUserAndRole(@RequestBody SysUserRoleCommand command) {
        return ApiResponse.ok(sysUserRoleCommandService.delete(command));
    }

    @DeleteMapping("/wipeLinkUserAndRole")
    public Mono<ApiResponse> wipeLinkUserAndRole(@RequestParam List<Long> ids) {
        return ApiResponse.ok(sysUserRoleCommandService.wipe(ids));
    }
}
