package com.springddd.web;

import com.springddd.application.service.user.SysUserRoleCommandService;
import com.springddd.application.service.user.SysUserRoleQueryService;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/sys/user")
@RequiredArgsConstructor
public class SysUserRoleController {

    private final SysUserRoleCommandService sysUserRoleCommandService;

    private final SysUserRoleQueryService sysUserRoleQueryService;

    @PostMapping("/queryRolesByUserId")
    public Mono<ApiResponse> queryRolesByUserId(@RequestParam("userId") Long userId) {
        return ApiResponse.ok(sysUserRoleQueryService.queryLinkUserAndRole(userId));
    }

    @PostMapping("/linkUserAndRole")
    public Mono<ApiResponse> linkUserAndRole(@RequestParam("userId") Long userId, @RequestParam("roleIds") List<Long> roleIds) {
        return ApiResponse.ok(sysUserRoleCommandService.create(userId, roleIds));
    }

    @DeleteMapping("/wipeLinkUserAndRole")
    public Mono<ApiResponse> wipeLinkUserAndRole(@RequestParam List<Long> ids) {
        return ApiResponse.ok(sysUserRoleCommandService.wipe(ids));
    }
}
