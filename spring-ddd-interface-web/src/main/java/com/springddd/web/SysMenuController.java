package com.springddd.web;

import com.springddd.application.service.menu.SysMenuCommandService;
import com.springddd.application.service.menu.SysMenuQueryService;
import com.springddd.application.service.menu.dto.SysMenuCommand;
import com.springddd.application.service.menu.dto.SysMenuQuery;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/menu")
public class SysMenuController {

    private final SysMenuQueryService sysMenuQueryService;

    private final SysMenuCommandService sysMenuCommandService;

    @PostMapping("/index")
    public Mono<ApiResponse> index(@RequestBody Mono<SysMenuQuery> query) {
        return ApiResponse.validated(query, sysMenuQueryService::index);
    }

    @PostMapping("/all")
    public Mono<ApiResponse> all() {
        return ApiResponse.ok(sysMenuQueryService.queryByPermissions());
    }

    @PostMapping("/getMenuTreeWithoutPermission")
    public Mono<ApiResponse> getMenuTreeWithoutPermission() {
        return ApiResponse.ok(sysMenuQueryService.getMenuTreeWithoutPermission());
    }

    @PostMapping("/getMenuTreeWithPermission")
    public Mono<ApiResponse> getMenuTreeWithPermission() {
        return ApiResponse.ok(sysMenuQueryService.getMenuTreeWithPermission());
    }

    @PostMapping("/getByMenuId")
    public Mono<ApiResponse> getByMenuId(@RequestParam("menuId") Long id) {
        return ApiResponse.ok(sysMenuQueryService.queryByMenuId(id));
    }

    @PostMapping("/create")
    public Mono<ApiResponse> create(@RequestBody SysMenuCommand command) {
        return ApiResponse.ok(sysMenuCommandService.create(command));
    }

    @PutMapping("/update")
    public Mono<ApiResponse> update(@RequestBody SysMenuCommand command) {
        return ApiResponse.ok(sysMenuCommandService.update(command));
    }

    @PostMapping("/delete")
    public Mono<ApiResponse> delete(@RequestBody SysMenuCommand command) {
        return ApiResponse.ok(sysMenuCommandService.delete(command));
    }

    @DeleteMapping("/wipe")
    public Mono<ApiResponse> wipe(@RequestParam("ids") List<Long> ids) {
        return ApiResponse.ok(sysMenuCommandService.wipe(ids));
    }
}
