package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRoleMenuView;
import com.springddd.domain.role.*;
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
class LinkRoleAndMenusDomainServiceImplTest {

    @Mock
    private SysRoleMenuQueryService sysRoleMenuQueryService;

    @Mock
    private WipeSysRoleMenuByIdsDomainService wipeSysRoleMenuByIdsDomainService;

    @Mock
    private SysRoleMenuDomainRepository sysRoleMenuDomainRepository;

    @Mock
    private SysRoleMenuDomainFactory sysRoleMenuDomainFactory;

    private LinkRoleAndMenusDomainServiceImpl linkRoleAndMenusDomainService;

    @BeforeEach
    void setUp() {
        linkRoleAndMenusDomainService = new LinkRoleAndMenusDomainServiceImpl(
                sysRoleMenuQueryService,
                wipeSysRoleMenuByIdsDomainService,
                sysRoleMenuDomainRepository,
                sysRoleMenuDomainFactory
        );
    }

    @Test
    void link_shouldComplete_whenValidInput() {
        Long roleId = 1L;
        List<Long> menuIds = Arrays.asList(1L, 2L);

        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(roleId)).thenReturn(Mono.just(Collections.emptyList()));
        when(wipeSysRoleMenuByIdsDomainService.deleteByIds(any())).thenReturn(Mono.empty());
        when(sysRoleMenuDomainFactory.newInstance(any(), any(), any())).thenReturn(new SysRoleMenuDomain());
        when(sysRoleMenuDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(linkRoleAndMenusDomainService.link(roleId, menuIds))
                .verifyComplete();
    }

    @Test
    void link_shouldWipeExistingMenus_whenMenusExist() {
        Long roleId = 1L;
        List<Long> menuIds = Arrays.asList(2L);

        SysRoleMenuView view = new SysRoleMenuView();
        view.setId(1L);
        view.setRoleId(roleId);
        view.setMenuId(1L);

        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(roleId)).thenReturn(Mono.just(Collections.singletonList(view)));
        when(wipeSysRoleMenuByIdsDomainService.deleteByIds(any())).thenReturn(Mono.empty());
        when(sysRoleMenuDomainFactory.newInstance(any(), any(), any())).thenReturn(new SysRoleMenuDomain());
        when(sysRoleMenuDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(linkRoleAndMenusDomainService.link(roleId, menuIds))
                .verifyComplete();
    }
}
