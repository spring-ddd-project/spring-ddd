package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserRoleView;
import com.springddd.domain.user.*;
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
class LinkUsersAndRolesDomainServiceImplTest {

    @Mock
    private SysUserRoleQueryService sysUserRoleQueryService;

    @Mock
    private WipeSysUserRoleByIdsDomainService wipeSysUserRoleByIdsDomainService;

    @Mock
    private SysUserRoleDomainRepository sysUserRoleDomainRepository;

    @Mock
    private SysUserRoleDomainFactory sysUserRoleDomainFactory;

    private LinkUsersAndRolesDomainServiceImpl linkUsersAndRolesDomainService;

    @BeforeEach
    void setUp() {
        linkUsersAndRolesDomainService = new LinkUsersAndRolesDomainServiceImpl(
                sysUserRoleQueryService,
                wipeSysUserRoleByIdsDomainService,
                sysUserRoleDomainRepository,
                sysUserRoleDomainFactory
        );
    }

    @Test
    void link_shouldComplete_whenValidInput() {
        Long userId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L);

        when(sysUserRoleQueryService.queryLinkUserAndRole(userId)).thenReturn(Mono.just(Collections.emptyList()));
        when(wipeSysUserRoleByIdsDomainService.deleteByIds(any())).thenReturn(Mono.empty());
        when(sysUserRoleDomainFactory.newInstance(any(), any(), any())).thenReturn(new SysUserRoleDomain());
        when(sysUserRoleDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(linkUsersAndRolesDomainService.link(userId, roleIds))
                .verifyComplete();
    }

    @Test
    void link_shouldWipeExistingRoles_whenRolesExist() {
        Long userId = 1L;
        List<Long> roleIds = Arrays.asList(2L);

        SysUserRoleView view = new SysUserRoleView();
        view.setId(1L);
        view.setUserId(userId);
        view.setRoleId(1L);

        when(sysUserRoleQueryService.queryLinkUserAndRole(userId)).thenReturn(Mono.just(Collections.singletonList(view)));
        when(wipeSysUserRoleByIdsDomainService.deleteByIds(any())).thenReturn(Mono.empty());
        when(sysUserRoleDomainFactory.newInstance(any(), any(), any())).thenReturn(new SysUserRoleDomain());
        when(sysUserRoleDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(linkUsersAndRolesDomainService.link(userId, roleIds))
                .verifyComplete();
    }
}
