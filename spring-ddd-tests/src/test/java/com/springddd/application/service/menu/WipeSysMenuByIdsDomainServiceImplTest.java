package com.springddd.application.service.menu;

import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.application.service.role.SysRoleMenuQueryService;
import com.springddd.application.service.role.dto.SysRoleMenuView;
import com.springddd.domain.menu.*;
import com.springddd.domain.role.WipeSysRoleMenuByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WipeSysMenuByIdsDomainServiceImplTest {

    @Mock
    private SysMenuRepository sysMenuRepository;

    @Mock
    private SysMenuQueryService sysMenuQueryService;

    @Mock
    private WipeSysRoleMenuByIdsDomainService wipeSysRoleMenuByIdsDomainService;

    @Mock
    private SysRoleMenuQueryService sysRoleMenuQueryService;

    private WipeSysMenuByIdsDomainServiceImpl wipeSysMenuByIdsDomainService;

    @BeforeEach
    void setUp() {
        wipeSysMenuByIdsDomainService = new WipeSysMenuByIdsDomainServiceImpl(
                sysMenuRepository,
                sysMenuQueryService,
                wipeSysRoleMenuByIdsDomainService,
                sysRoleMenuQueryService
        );
    }

    @Test
    void deleteByIds_shouldComplete_whenValidIds() {
        List<Long> ids = Arrays.asList(1L);
        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setParentId(0L);

        when(sysMenuQueryService.queryAllMenu()).thenReturn(Mono.just(Collections.singletonList(view)));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenusByMenuId(any())).thenReturn(Mono.just(Collections.emptyList()));
        when(sysMenuRepository.deleteAllById(any())).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysMenuByIdsDomainService.deleteByIds(ids))
                .verifyComplete();
    }

    @Test
    void deleteByIds_shouldWipeRoleMenus_whenRoleMenusExist() {
        List<Long> ids = Arrays.asList(1L);
        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setParentId(0L);

        SysRoleMenuView roleMenuView = new SysRoleMenuView();
        roleMenuView.setId(1L);
        roleMenuView.setRoleId(1L);
        roleMenuView.setMenuId(1L);

        when(sysMenuQueryService.queryAllMenu()).thenReturn(Mono.just(Collections.singletonList(view)));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenusByMenuId(any())).thenReturn(Mono.just(Collections.singletonList(roleMenuView)));
        when(wipeSysRoleMenuByIdsDomainService.deleteByIds(any())).thenReturn(Mono.empty());
        when(sysMenuRepository.deleteAllById(any())).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysMenuByIdsDomainService.deleteByIds(ids))
                .verifyComplete();
    }

    @Test
    void deleteByIds_shouldReturnEmpty_whenIdsEmpty() {
        StepVerifier.create(wipeSysMenuByIdsDomainService.deleteByIds(Collections.emptyList()))
                .verifyComplete();
    }
}
