package com.springddd.domain.user;

import com.springddd.application.service.user.SysUserRoleQueryService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LinkUsersAndRolesDomainServiceTest {

    @Mock
    private SysUserRoleQueryService sysUserRoleQueryService;

    @Mock
    private WipeSysUserRoleByIdsDomainService wipeSysUserRoleByIdsDomainService;

    @Mock
    private SysUserRoleDomainRepository sysUserRoleDomainRepository;

    @Mock
    private SysUserRoleDomainFactory sysUserRoleDomainFactory;

    @InjectMocks
    private LinkUsersAndRolesDomainService domainService;

    @Test
    void link_shouldLinkUserToRoles() {
        when(sysUserRoleQueryService.queryLinkUserAndRole(any())).thenReturn(Mono.just(Collections.emptyList()));
        when(wipeSysUserRoleByIdsDomainService.deleteByIds(any())).thenReturn(Mono.empty());

        SysUserRoleDomain mockDomain = new SysUserRoleDomain();
        when(sysUserRoleDomainFactory.newInstance(any(UserId.class), any(), any())).thenReturn(mockDomain);
        when(sysUserRoleDomainRepository.save(any(SysUserRoleDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(domainService.link(1L, Arrays.asList(1L, 2L)))
                .verifyComplete();

        verify(wipeSysUserRoleByIdsDomainService, times(1)).deleteByIds(any());
        verify(sysUserRoleDomainRepository, times(2)).save(any(SysUserRoleDomain.class));
    }

    @Test
    void link_shouldHandleEmptyRoleIds() {
        when(sysUserRoleQueryService.queryLinkUserAndRole(any())).thenReturn(Mono.just(Collections.emptyList()));
        when(wipeSysUserRoleByIdsDomainService.deleteByIds(any())).thenReturn(Mono.empty());

        StepVerifier.create(domainService.link(1L, Arrays.asList()))
                .verifyComplete();

        verify(wipeSysUserRoleByIdsDomainService, times(1)).deleteByIds(any());
        verify(sysUserRoleDomainRepository, never()).save(any(SysUserRoleDomain.class));
    }
}
