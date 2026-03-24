package com.springddd.application.service.menu;

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

import static org.mockito.ArgumentMatchers.any;
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

    private SysMenuView mockView;

    @BeforeEach
    void setUp() {
        mockView = new SysMenuView();
        mockView.setId(1L);
        mockView.setParentId(0L);
        mockView.setName("Test Menu");
        mockView.setDeleteStatus(false);
    }

    @Test
    void deleteByIds_shouldHandleEmptyIdsList() {
        List<Long> ids = Collections.emptyList();

        StepVerifier.create(wipeSysMenuByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysMenuQueryService, never()).queryAllMenu();
        verify(sysMenuRepository, never()).deleteAllById(anyList());
    }

    @Test
    void deleteByIds_shouldHandleNoMenuFound() {
        List<Long> ids = Arrays.asList(999L);
        List<SysMenuView> emptyMenus = Collections.emptyList();

        when(sysMenuQueryService.queryAllMenu()).thenReturn(Mono.just(emptyMenus));

        StepVerifier.create(wipeSysMenuByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysMenuQueryService).queryAllMenu();
        verify(sysMenuRepository, never()).deleteAllById(anyList());
    }

    @Test
    void deleteByIds_shouldDeleteMultipleMenus() {
        SysMenuView childView = new SysMenuView();
        childView.setId(2L);
        childView.setParentId(1L);
        childView.setName("Child Menu");
        childView.setDeleteStatus(false);

        List<Long> ids = Arrays.asList(1L);
        List<SysMenuView> allMenus = Arrays.asList(mockView, childView);

        when(sysMenuQueryService.queryAllMenu()).thenReturn(Mono.just(allMenus));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenusByMenuId(anyLong())).thenReturn(Mono.just(Collections.emptyList()));
        when(sysMenuRepository.deleteAllById(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysMenuByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysMenuQueryService).queryAllMenu();
        verify(sysMenuRepository).deleteAllById(anyList());
    }
}
