package com.springddd.web;

import com.springddd.application.service.user.SysUserCommandService;
import com.springddd.application.service.user.SysUserQueryService;
import com.springddd.application.service.user.dto.SysUserCommand;
import com.springddd.application.service.user.dto.SysUserQuery;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/sys")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserCommandService sysUserCommandService;

    private final SysUserQueryService sysUserQueryService;

    @PostMapping("/page")
    public Mono<ApiResponse> page(@RequestBody SysUserQuery query) {
        return ApiResponse.ok(sysUserQueryService.page(query));
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
    public Mono<Void> delete(@RequestBody SysUserCommand command) {
        return sysUserCommandService.deleteUser(command);
    }

    @DeleteMapping("/wipe")
    public Mono<Void> wipe(@RequestBody List<Long> ids) {
        return sysUserCommandService.wipe(ids);
    }
}
