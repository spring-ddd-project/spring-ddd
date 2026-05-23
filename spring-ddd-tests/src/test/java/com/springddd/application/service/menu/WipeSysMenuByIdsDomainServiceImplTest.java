package com.springddd.application.service.menu;

import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.application.service.role.SysRoleMenuQueryService;
import com.springddd.application.service.role.dto.SysRoleMenuView;
import com.springddd.domain.role.WipeSysRoleMenuByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeSysMenuByIdsDomainServiceImplTest {

    @Mock
    private SysMenuRepository sysMenuRepository;

    @Mock
    private SysMenuQueryService sysMenuQueryService;

    @Mock
    private WipeSysRoleMenuByIdsDomainService wipeSysRoleMenuByIdsDomainService;

    @Mock
    private SysRoleMenuQueryService sysRoleMenuQueryService;

    @InjectMocks
    private WipeSysMenuByIdsDomainServiceImpl service;

    @Test
    @DisplayName("deleteByIds 应处理空列表")
    void deleteByIds_shouldHandleEmptyList() {
        StepVerifier.create(service.deleteByIds(List.of()))
                .verifyComplete();

        verify(sysMenuQueryService, never()).queryAllMenu();
    }

    @Test
    @DisplayName("deleteByIds 应删除菜单及其子菜单并清理角色关联")
    void deleteByIds_shouldWipeAndCleanRelations() {
        SysMenuView parent = new SysMenuView();
        parent.setId(1L);
        parent.setParentId(null);

        SysMenuView child = new SysMenuView();
        child.setId(2L);
        child.setParentId(1L);

        SysRoleMenuView rm = new SysRoleMenuView();
        rm.setId(10L);
        rm.setRoleId(100L);
        rm.setMenuId(1L);

        when(sysMenuQueryService.queryAllMenu()).thenReturn(Mono.just(List.of(parent, child)));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenusByMenuId(1L)).thenReturn(Mono.just(List.of(rm)));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenusByMenuId(2L)).thenReturn(Mono.just(List.of()));
        when(wipeSysRoleMenuByIdsDomainService.deleteByIds(anyList())).thenReturn(Mono.empty());
        when(sysMenuRepository.deleteAllById(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteByIds(List.of(1L)))
                .verifyComplete();

        verify(sysMenuRepository).deleteAllById(anyList());
    }
}
