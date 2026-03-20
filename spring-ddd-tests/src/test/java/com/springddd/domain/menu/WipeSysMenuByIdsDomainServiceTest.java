package com.springddd.domain.menu;

import com.springddd.application.service.menu.SysMenuQueryService;
import com.springddd.application.service.menu.WipeSysMenuByIdsDomainServiceImpl;
import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.application.service.role.SysRoleMenuQueryService;
import com.springddd.application.service.role.dto.SysRoleMenuView;
import com.springddd.domain.role.WipeSysRoleMenuByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeSysMenuByIdsDomainServiceTest {

    @Mock
    private SysMenuRepository sysMenuRepository;

    @Mock
    private SysMenuQueryService sysMenuQueryService;

    @Mock
    private WipeSysRoleMenuByIdsDomainService wipeSysRoleMenuByIdsDomainService;

    @Mock
    private SysRoleMenuQueryService sysRoleMenuQueryService;

    @InjectMocks
    private WipeSysMenuByIdsDomainServiceImpl wipeSysMenuByIdsDomainService;

    @Test
    void deleteByIds_shouldDeleteSingleId() {
        List<Long> ids = Collections.singletonList(1L);
        List<SysMenuView> menuViews = Collections.singletonList(createMenuView(1L, null));

        when(sysMenuQueryService.queryAllMenu()).thenReturn(Mono.just(menuViews));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenusByMenuId(1L)).thenReturn(Mono.just(Collections.emptyList()));
        when(sysMenuRepository.deleteAllById(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysMenuByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysMenuRepository).deleteAllById(anyList());
    }

    @Test
    void deleteByIds_shouldDeleteMultipleIds() {
        List<Long> ids = Collections.singletonList(1L);
        List<SysMenuView> menuViews = Arrays.asList(
                createMenuView(1L, null),
                createMenuView(2L, 1L),
                createMenuView(3L, 1L)
        );

        when(sysMenuQueryService.queryAllMenu()).thenReturn(Mono.just(menuViews));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenusByMenuId(anyLong())).thenReturn(Mono.just(Collections.emptyList()));
        when(sysMenuRepository.deleteAllById(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysMenuByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysMenuRepository).deleteAllById(anyList());
    }

    @Test
    void deleteByIds_shouldHandleEmptyList() {
        List<Long> ids = Collections.emptyList();

        StepVerifier.create(wipeSysMenuByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysMenuQueryService, never()).queryAllMenu();
        verify(sysMenuRepository, never()).deleteAllById(anyList());
    }

    @Test
    void deleteByIds_shouldDeleteParentAndAllChildren() {
        List<Long> ids = Collections.singletonList(1L);
        List<SysMenuView> menuViews = Arrays.asList(
                createMenuView(1L, null),
                createMenuView(2L, 1L),
                createMenuView(3L, 1L),
                createMenuView(4L, 2L)
        );

        when(sysMenuQueryService.queryAllMenu()).thenReturn(Mono.just(menuViews));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenusByMenuId(anyLong())).thenReturn(Mono.just(Collections.emptyList()));
        when(sysMenuRepository.deleteAllById(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysMenuByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysMenuRepository).deleteAllById(anyList());
    }

    @Test
    void deleteByIds_shouldDeleteRoleMenuAssociations() {
        List<Long> ids = Collections.singletonList(1L);
        List<SysMenuView> menuViews = Collections.singletonList(createMenuView(1L, null));
        List<SysRoleMenuView> roleMenuViews = Arrays.asList(
                createRoleMenuView(100L, 1L),
                createRoleMenuView(101L, 1L)
        );

        when(sysMenuQueryService.queryAllMenu()).thenReturn(Mono.just(menuViews));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenusByMenuId(1L)).thenReturn(Mono.just(roleMenuViews));
        when(wipeSysRoleMenuByIdsDomainService.deleteByIds(anyList())).thenReturn(Mono.empty());
        when(sysMenuRepository.deleteAllById(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysMenuByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(wipeSysRoleMenuByIdsDomainService).deleteByIds(Arrays.asList(100L, 101L));
    }

    private SysMenuView createMenuView(Long id, Long parentId) {
        SysMenuView view = new SysMenuView();
        view.setId(id);
        view.setParentId(parentId);
        view.setDeleteStatus(false);
        view.setMenuType(1);
        return view;
    }

    private SysRoleMenuView createRoleMenuView(Long id, Long menuId) {
        SysRoleMenuView view = new SysRoleMenuView();
        view.setId(id);
        view.setMenuId(menuId);
        view.setDeleteStatus(false);
        return view;
    }
}
