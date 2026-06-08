package com.springddd.web;

import com.springddd.application.service.role.SysRoleMenuCommandService;
import com.springddd.application.service.role.SysRoleMenuQueryService;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/sys/role")
@RequiredArgsConstructor
public class SysRoleMenuController {

    private final SysRoleMenuQueryService sysRoleMenuQueryService;

    private final SysRoleMenuCommandService sysRoleMenuCommandService;

    @PostMapping("/linkRoleAndMenus")
    public Mono<ApiResponse> linkRoleAndMenus(@RequestParam("roleId") Long roleId,
                                              @RequestParam("menuIds") List<Long> menuIds) {
        return ApiResponse.ok(sysRoleMenuCommandService.create(roleId, menuIds));
    }

    @PostMapping("/queryMenusByRoleId")
    public Mono<ApiResponse> queryMenusByRoleId(@RequestParam("roleId") Long roleId) {
        return ApiResponse.ok(sysRoleMenuQueryService.queryLinkRoleAndMenus(roleId));
    }

    @DeleteMapping("/wipeLinkRoleAndMenus")
    public Mono<ApiResponse> wipeLinkRoleAndMenus(@RequestParam List<Long> ids) {
        return ApiResponse.ok(sysRoleMenuCommandService.wipe(ids));
    }

}
