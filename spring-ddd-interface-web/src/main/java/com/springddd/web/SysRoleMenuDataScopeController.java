package com.springddd.web;

import com.springddd.application.service.role.SysRoleMenuDataScopeCommandService;
import com.springddd.application.service.role.SysRoleMenuDataScopeQueryService;
import com.springddd.application.service.role.dto.SysRoleMenuDataScopeCommand;
import com.springddd.domain.util.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/roleMenuDataScope")
@RequiredArgsConstructor
public class SysRoleMenuDataScopeController {

    private final SysRoleMenuDataScopeCommandService sysRoleMenuDataScopeCommandService;

    private final SysRoleMenuDataScopeQueryService sysRoleMenuDataScopeQueryService;

    private final ObjectMapper objectMapper;

    @GetMapping
    public Mono<ApiResponse> list(@RequestParam Long roleId) {
        return ApiResponse.ok(sysRoleMenuDataScopeQueryService.listByRoleId(roleId));
    }

    @GetMapping("/find")
    public Mono<ApiResponse> find(@RequestParam Long roleId, @RequestParam Long menuId) {
        return ApiResponse.ok(sysRoleMenuDataScopeQueryService.findByRoleIdAndMenuId(roleId, menuId));
    }

    @PostMapping("/batch")
    public Mono<ApiResponse> batchSave(@RequestBody Map<String, Object> body) {
        Long roleId = Long.valueOf(body.get("roleId").toString());
        List<SysRoleMenuDataScopeCommand> items = objectMapper.convertValue(
                body.get("items"), new TypeReference<List<SysRoleMenuDataScopeCommand>>() {}
        );
        return ApiResponse.ok(sysRoleMenuDataScopeCommandService.batchSave(roleId, items));
    }
}
