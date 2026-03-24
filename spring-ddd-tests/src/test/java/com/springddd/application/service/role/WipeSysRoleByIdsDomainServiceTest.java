package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRoleMenuView;
import com.springddd.application.service.role.dto.SysRoleMenuQuery;
import com.springddd.application.service.user.SysUserRoleCommandService;
import com.springddd.application.service.user.SysUserRoleQueryService;
import com.springddd.application.service.user.dto.SysUserRoleView;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleMenuRepository;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    private WipeSysRoleByIdsDomainServiceImpl wipeSysRoleByIdsDomainService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void deleteByIds_shouldWipeAllRelatedDataAndDeleteRoles() {
        List<Long> ids = Arrays.asList(1L, 2L);
        SysUserRoleView userRoleView = new SysUserRoleView();
        userRoleView.setId(1L);
        SysRoleMenuView roleMenuView = new SysRoleMenuView();
        roleMenuView.setId(1L);

        when(sysUserRoleQueryService.queryLinkUserAndRoleByRoleId(anyLong()))
                .thenReturn(Mono.just(Collections.singletonList(userRoleView)));
        when(sysUserRoleCommandService.wipe(anyList())).thenReturn(Mono.empty());
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(anyLong()))
                .thenReturn(Mono.just(Collections.singletonList(roleMenuView)));
        when(sysRoleMenuCommandService.wipe(anyList())).thenReturn(Mono.empty());
        when(sysRoleRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysRoleByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysRoleRepository).deleteAllById(ids);
    }

    @Test
    void deleteByIds_shouldHandleEmptyUserRoleList() {
        List<Long> ids = Collections.singletonList(1L);
        SysRoleMenuView roleMenuView = new SysRoleMenuView();
        roleMenuView.setId(1L);

        when(sysUserRoleQueryService.queryLinkUserAndRoleByRoleId(anyLong()))
                .thenReturn(Mono.just(Collections.emptyList()));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(anyLong()))
                .thenReturn(Mono.just(Collections.singletonList(roleMenuView)));
        when(sysRoleMenuCommandService.wipe(anyList())).thenReturn(Mono.empty());
        when(sysRoleRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysRoleByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysUserRoleCommandService, never()).wipe(anyList());
        verify(sysRoleRepository).deleteAllById(ids);
    }

    @Test
    void deleteByIds_shouldHandleEmptyRoleMenuList() {
        List<Long> ids = Collections.singletonList(1L);
        SysUserRoleView userRoleView = new SysUserRoleView();
        userRoleView.setId(1L);

        when(sysUserRoleQueryService.queryLinkUserAndRoleByRoleId(anyLong()))
                .thenReturn(Mono.just(Collections.singletonList(userRoleView)));
        when(sysUserRoleCommandService.wipe(anyList())).thenReturn(Mono.empty());
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(anyLong()))
                .thenReturn(Mono.just(Collections.emptyList()));
        when(sysRoleRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysRoleByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysRoleMenuCommandService, never()).wipe(anyList());
        verify(sysRoleRepository).deleteAllById(ids);
    }

    @Test
    void deleteByIds_shouldHandleBothEmptyLists() {
        List<Long> ids = Collections.singletonList(1L);

        when(sysUserRoleQueryService.queryLinkUserAndRoleByRoleId(anyLong()))
                .thenReturn(Mono.just(Collections.emptyList()));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(anyLong()))
                .thenReturn(Mono.just(Collections.emptyList()));
        when(sysRoleRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysRoleByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysUserRoleCommandService, never()).wipe(anyList());
        verify(sysRoleMenuCommandService, never()).wipe(anyList());
        verify(sysRoleRepository).deleteAllById(ids);
    }
}
