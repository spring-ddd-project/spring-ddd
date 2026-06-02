package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRoleMenuView;
import com.springddd.application.service.user.SysUserRoleCommandService;
import com.springddd.application.service.user.SysUserRoleQueryService;
import com.springddd.application.service.user.dto.SysUserRoleView;
import com.springddd.domain.role.*;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    private WipeSysRoleByIdsDomainServiceImpl wipeSysRoleByIdsDomainService;

    @BeforeEach
    void setUp() {
        wipeSysRoleByIdsDomainService = new WipeSysRoleByIdsDomainServiceImpl(
                sysRoleRepository,
                sysUserRoleQueryService,
                sysUserRoleCommandService,
                sysRoleMenuQueryService,
                sysRoleMenuCommandService
        );
    }

    @Test
    void deleteByIds_shouldComplete_whenValidIds() {
        List<Long> ids = Arrays.asList(1L);
        when(sysUserRoleQueryService.queryLinkUserAndRoleByRoleId(any())).thenReturn(Mono.just(Collections.emptyList()));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(any())).thenReturn(Mono.just(Collections.emptyList()));
        when(sysRoleRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysRoleByIdsDomainService.deleteByIds(ids))
                .verifyComplete();
    }

    @Test
    void deleteByIds_shouldWipeRoleMenus_whenMenusExist() {
        List<Long> ids = Arrays.asList(1L);
        SysRoleMenuView view = new SysRoleMenuView();
        view.setId(1L);
        view.setRoleId(1L);
        view.setMenuId(2L);

        when(sysUserRoleQueryService.queryLinkUserAndRoleByRoleId(any())).thenReturn(Mono.just(Collections.emptyList()));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(1L)).thenReturn(Mono.just(Collections.singletonList(view)));
        when(sysRoleMenuCommandService.wipe(any())).thenReturn(Mono.empty());
        when(sysRoleRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysRoleByIdsDomainService.deleteByIds(ids))
                .verifyComplete();
    }

    @Test
    void deleteByIds_shouldWipeUserRoles_whenUserRolesExist() {
        List<Long> ids = Arrays.asList(1L);
        SysUserRoleView urView = new SysUserRoleView();
        urView.setId(1L);
        urView.setRoleId(1L);
        urView.setUserId(2L);

        when(sysUserRoleQueryService.queryLinkUserAndRoleByRoleId(1L)).thenReturn(Mono.just(Collections.singletonList(urView)));
        when(sysUserRoleCommandService.wipe(any())).thenReturn(Mono.empty());
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(1L)).thenReturn(Mono.just(Collections.emptyList()));
        when(sysRoleRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysRoleByIdsDomainService.deleteByIds(ids))
                .verifyComplete();
    }
}
