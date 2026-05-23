package com.springddd.application.service.role;

import com.springddd.application.service.user.SysUserRoleCommandService;
import com.springddd.application.service.user.SysUserRoleQueryService;
import com.springddd.application.service.user.dto.SysUserRoleView;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeSysRoleByIdsDomainServiceImplTest {

    @Mock
    private SysRoleRepository sysRoleRepository;

    @Mock
    private SysUserRoleQueryService sysUserRoleQueryService;

    @Mock
    private SysUserRoleCommandService sysUserRoleCommandService;

    @Mock
    private SysRoleMenuQueryService sysRoleMenuQueryService;

    @Mock
    private SysRoleMenuCommandService sysRoleMenuCommandService;

    @InjectMocks
    private WipeSysRoleByIdsDomainServiceImpl service;

    @Test
    @DisplayName("deleteByIds 应删除角色及其关联")
    void deleteByIds_shouldWipeRolesAndRelations() {
        SysUserRoleView ur = new SysUserRoleView();
        ur.setId(100L);

        when(sysUserRoleQueryService.queryLinkUserAndRoleByRoleId(1L)).thenReturn(Mono.just(List.of(ur)));
        when(sysUserRoleCommandService.wipe(anyList())).thenReturn(Mono.empty());
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(1L)).thenReturn(Mono.just(List.of()));
        when(sysRoleRepository.deleteAllById(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteByIds(List.of(1L)))
                .verifyComplete();

        verify(sysUserRoleCommandService).wipe(List.of(100L));
        verify(sysRoleRepository).deleteAllById(List.of(1L));
    }

    @Test
    @DisplayName("deleteByIds 当角色关联菜单时应同时删除菜单关联")
    void deleteByIds_whenRoleHasMenus_shouldWipeMenuRelations() {
        SysUserRoleView ur = new SysUserRoleView();
        ur.setId(100L);

        com.springddd.application.service.role.dto.SysRoleMenuView rm = new com.springddd.application.service.role.dto.SysRoleMenuView();
        rm.setId(200L);

        when(sysUserRoleQueryService.queryLinkUserAndRoleByRoleId(1L)).thenReturn(Mono.just(List.of(ur)));
        when(sysUserRoleCommandService.wipe(anyList())).thenReturn(Mono.empty());
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(1L)).thenReturn(Mono.just(List.of(rm)));
        when(sysRoleMenuCommandService.wipe(anyList())).thenReturn(Mono.empty());
        when(sysRoleRepository.deleteAllById(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteByIds(List.of(1L)))
                .verifyComplete();

        verify(sysUserRoleCommandService).wipe(List.of(100L));
        verify(sysRoleMenuCommandService).wipe(List.of(200L));
        verify(sysRoleRepository).deleteAllById(List.of(1L));
    }
}
