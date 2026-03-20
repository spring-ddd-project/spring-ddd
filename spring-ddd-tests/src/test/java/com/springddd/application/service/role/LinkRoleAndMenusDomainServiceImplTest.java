package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRoleMenuView;
import com.springddd.domain.menu.MenuId;
import com.springddd.domain.role.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LinkRoleAndMenusDomainServiceImplTest {

    @Mock
    private SysRoleMenuQueryService sysRoleMenuQueryService;

    @Mock
    private WipeSysRoleMenuByIdsDomainService wipeSysRoleMenuByIdsDomainService;

    @Mock
    private SysRoleMenuDomainRepository sysRoleMenuDomainRepository;

    @Mock
    private SysRoleMenuDomainFactory sysRoleMenuDomainFactory;

    @InjectMocks
    private LinkRoleAndMenusDomainServiceImpl linkRoleAndMenusDomainService;

    private SysRoleMenuDomain mockRoleMenuDomain;

    @BeforeEach
    void setUp() {
        mockRoleMenuDomain = new SysRoleMenuDomain();
        mockRoleMenuDomain.setRoleMenuId(new RoleMenuId(1L));
        mockRoleMenuDomain.setRoleId(new RoleId(1L));
        mockRoleMenuDomain.setMenuId(new MenuId(10L));
        mockRoleMenuDomain.setDeleteStatus(false);
    }

    @Test
    void link_shouldDeleteExistingAndCreateNewLinks() {
        Long roleId = 1L;
        List<Long> menuIds = Arrays.asList(10L, 20L, 30L);

        SysRoleMenuView existingView = new SysRoleMenuView();
        existingView.setId(1L);
        existingView.setRoleId(roleId);
        existingView.setMenuId(5L);

        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(roleId))
                .thenReturn(Mono.just(Collections.singletonList(existingView)));
        when(wipeSysRoleMenuByIdsDomainService.deleteByIds(anyList())).thenReturn(Mono.empty());
        when(sysRoleMenuDomainFactory.newInstance(any(RoleId.class), any(MenuId.class), any()))
                .thenReturn(mockRoleMenuDomain);
        when(sysRoleMenuDomainRepository.save(any(SysRoleMenuDomain.class)))
                .thenReturn(Mono.just(1L));

        StepVerifier.create(linkRoleAndMenusDomainService.link(roleId, menuIds))
                .verifyComplete();

        verify(wipeSysRoleMenuByIdsDomainService).deleteByIds(Collections.singletonList(1L));
        verify(sysRoleMenuDomainRepository, times(3)).save(any(SysRoleMenuDomain.class));
    }

    @Test
    void link_shouldHandleEmptyExistingLinks() {
        Long roleId = 1L;
        List<Long> menuIds = Arrays.asList(10L, 20L);

        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(roleId))
                .thenReturn(Mono.just(Collections.emptyList()));
        when(wipeSysRoleMenuByIdsDomainService.deleteByIds(anyList())).thenReturn(Mono.empty());
        when(sysRoleMenuDomainFactory.newInstance(any(RoleId.class), any(MenuId.class), any()))
                .thenReturn(mockRoleMenuDomain);
        when(sysRoleMenuDomainRepository.save(any(SysRoleMenuDomain.class)))
                .thenReturn(Mono.just(1L));

        StepVerifier.create(linkRoleAndMenusDomainService.link(roleId, menuIds))
                .verifyComplete();

        verify(sysRoleMenuDomainRepository, times(2)).save(any(SysRoleMenuDomain.class));
    }

    @Test
    void link_shouldCreateNewRoleMenuDomains() {
        Long roleId = 5L;
        List<Long> menuIds = Arrays.asList(100L, 200L);

        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(roleId))
                .thenReturn(Mono.just(Collections.emptyList()));
        when(wipeSysRoleMenuByIdsDomainService.deleteByIds(anyList())).thenReturn(Mono.empty());
        when(sysRoleMenuDomainFactory.newInstance(eq(new RoleId(roleId)), any(MenuId.class), any()))
                .thenReturn(mockRoleMenuDomain);
        when(sysRoleMenuDomainRepository.save(any(SysRoleMenuDomain.class)))
                .thenReturn(Mono.just(1L));

        StepVerifier.create(linkRoleAndMenusDomainService.link(roleId, menuIds))
                .verifyComplete();

        verify(sysRoleMenuDomainFactory, times(2)).newInstance(eq(new RoleId(roleId)), any(MenuId.class), any());
    }
}
