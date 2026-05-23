package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRoleMenuView;
import com.springddd.domain.menu.MenuId;
import com.springddd.domain.role.RoleId;
import com.springddd.domain.role.SysRoleMenuDomain;
import com.springddd.domain.role.SysRoleMenuDomainFactory;
import com.springddd.domain.role.SysRoleMenuDomainRepository;
import com.springddd.domain.role.WipeSysRoleMenuByIdsDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
    private LinkRoleAndMenusDomainServiceImpl service;

    @Test
    @DisplayName("link 应为指定角色和菜单创建关联")
    void link_shouldCreateAssociations() {
        SysRoleMenuView existing = new SysRoleMenuView();
        existing.setId(100L);

        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(1L)).thenReturn(Mono.just(List.of(existing)));
        when(wipeSysRoleMenuByIdsDomainService.deleteByIds(anyList())).thenReturn(Mono.empty());

        SysRoleMenuDomain domain = mock(SysRoleMenuDomain.class);
        when(sysRoleMenuDomainFactory.newInstance(any(RoleId.class), any(MenuId.class), any())).thenReturn(domain);
        when(sysRoleMenuDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(service.link(1L, List.of(10L, 20L)))
                .verifyComplete();

        verify(sysRoleMenuDomainFactory, times(2)).newInstance(any(RoleId.class), any(MenuId.class), any());
        verify(sysRoleMenuDomainRepository, times(2)).save(any());
    }
}
