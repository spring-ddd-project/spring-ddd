package com.springddd.domain.role;

import com.springddd.application.service.role.SysRoleMenuCommandService;
import com.springddd.application.service.role.SysRoleMenuQueryService;
import com.springddd.application.service.user.SysUserRoleCommandService;
import com.springddd.application.service.user.SysUserRoleQueryService;
import com.springddd.application.service.user.dto.SysUserRoleView;
import com.springddd.application.service.role.dto.SysRoleMenuView;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeSysRoleByIdsDomainServiceTest {

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
    private WipeSysRoleByIdsDomainService domainService;

    @Test
    void deleteByIds_shouldDeleteEntitiesWithDependencies() {
        SysUserRoleView userRoleView = new SysUserRoleView();
        userRoleView.setId(1L);
        SysRoleMenuView roleMenuView = new SysRoleMenuView();
        roleMenuView.setId(1L);

        when(sysUserRoleQueryService.queryLinkUserAndRoleByRoleId(anyLong()))
                .thenReturn(Mono.just(Arrays.asList(userRoleView)));
        when(sysUserRoleCommandService.wipe(anyList())).thenReturn(Mono.empty());
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(anyLong()))
                .thenReturn(Mono.just(Arrays.asList(roleMenuView)));
        when(sysRoleMenuCommandService.wipe(anyList())).thenReturn(Mono.empty());
        when(sysRoleRepository.deleteAllById(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(domainService.deleteByIds(Arrays.asList(1L)))
                .verifyComplete();

        verify(sysUserRoleQueryService).queryLinkUserAndRoleByRoleId(1L);
        verify(sysRoleMenuQueryService).queryLinkRoleAndMenus(1L);
        verify(sysRoleRepository).deleteAllById(Arrays.asList(1L));
    }

    @Test
    void deleteByIds_shouldHandleEmptyList() {
        StepVerifier.create(domainService.deleteByIds(Arrays.asList()))
                .verifyComplete();

        verify(sysUserRoleQueryService, never()).queryLinkUserAndRoleByRoleId(anyLong());
        verify(sysRoleRepository, never()).deleteAllById(anyList());
    }

    @Test
    void deleteByIds_shouldHandleNoUserRoles() {
        when(sysUserRoleQueryService.queryLinkUserAndRoleByRoleId(anyLong()))
                .thenReturn(Mono.just(Arrays.asList()));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(anyLong()))
                .thenReturn(Mono.just(Arrays.asList()));
        when(sysRoleRepository.deleteAllById(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(domainService.deleteByIds(Arrays.asList(1L)))
                .verifyComplete();

        verify(sysUserRoleCommandService, never()).wipe(anyList());
        verify(sysRoleMenuCommandService, never()).wipe(anyList());
    }

    @Test
    void deleteByIds_shouldHandleNoRoleMenus() {
        SysUserRoleView userRoleView = new SysUserRoleView();
        userRoleView.setId(1L);

        when(sysUserRoleQueryService.queryLinkUserAndRoleByRoleId(anyLong()))
                .thenReturn(Mono.just(Arrays.asList(userRoleView)));
        when(sysUserRoleCommandService.wipe(anyList())).thenReturn(Mono.empty());
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(anyLong()))
                .thenReturn(Mono.just(Arrays.asList()));
        when(sysRoleRepository.deleteAllById(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(domainService.deleteByIds(Arrays.asList(1L)))
                .verifyComplete();

        verify(sysRoleMenuCommandService, never()).wipe(anyList());
    }
}
