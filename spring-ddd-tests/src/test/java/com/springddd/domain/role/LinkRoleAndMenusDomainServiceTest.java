package com.springddd.domain.role;

import com.springddd.application.service.role.SysRoleMenuQueryService;
import com.springddd.domain.role.WipeSysRoleMenuByIdsDomainService;
import com.springddd.application.service.role.dto.SysRoleMenuView;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LinkRoleAndMenusDomainServiceTest {

    @Mock
    private SysRoleMenuQueryService sysRoleMenuQueryService;

    @Mock
    private WipeSysRoleMenuByIdsDomainService wipeSysRoleMenuByIdsDomainService;

    @Mock
    private SysRoleMenuDomainRepository sysRoleMenuDomainRepository;

    @Mock
    private SysRoleMenuDomainFactory sysRoleMenuDomainFactory;

    @InjectMocks
    private LinkRoleAndMenusDomainService domainService;

    @Test
    void link_shouldLinkRoleAndMenus() {
        SysRoleMenuView view = new SysRoleMenuView();
        view.setId(1L);

        SysRoleMenuDomain domain = new SysRoleMenuDomain();

        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(anyLong()))
                .thenReturn(Mono.just(Arrays.asList(view)));
        when(wipeSysRoleMenuByIdsDomainService.deleteByIds(anyList())).thenReturn(Mono.empty());
        when(sysRoleMenuDomainFactory.newInstance(any(), any(), any())).thenReturn(domain);
        when(sysRoleMenuDomainRepository.save(any(SysRoleMenuDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(domainService.link(1L, Arrays.asList(10L, 20L)))
                .verifyComplete();

        verify(wipeSysRoleMenuByIdsDomainService).deleteByIds(Arrays.asList(1L));
        verify(sysRoleMenuDomainRepository, times(2)).save(any(SysRoleMenuDomain.class));
    }

    @Test
    void link_shouldHandleEmptyMenuIds() {
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(anyLong()))
                .thenReturn(Mono.just(Arrays.asList()));

        StepVerifier.create(domainService.link(1L, Arrays.asList()))
                .verifyComplete();

        verify(wipeSysRoleMenuByIdsDomainService, never()).deleteByIds(anyList());
        verify(sysRoleMenuDomainRepository, never()).save(any(SysRoleMenuDomain.class));
    }

    @Test
    void link_shouldHandleNoExistingRoleMenus() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();

        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(anyLong()))
                .thenReturn(Mono.just(Arrays.asList()));
        when(sysRoleMenuDomainFactory.newInstance(any(), any(), any())).thenReturn(domain);
        when(sysRoleMenuDomainRepository.save(any(SysRoleMenuDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(domainService.link(1L, Arrays.asList(10L)))
                .verifyComplete();

        verify(wipeSysRoleMenuByIdsDomainService, never()).deleteByIds(anyList());
        verify(sysRoleMenuDomainRepository, times(1)).save(any(SysRoleMenuDomain.class));
    }
}
