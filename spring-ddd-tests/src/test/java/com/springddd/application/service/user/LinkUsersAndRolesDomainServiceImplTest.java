package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserRoleView;
import com.springddd.domain.role.RoleId;
import com.springddd.domain.user.SysUserRoleDomain;
import com.springddd.domain.user.SysUserRoleDomainFactory;
import com.springddd.domain.user.SysUserRoleDomainRepository;
import com.springddd.domain.user.UserId;
import com.springddd.domain.user.WipeSysUserRoleByIdsDomainService;
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
class LinkUsersAndRolesDomainServiceImplTest {

    @Mock
    private SysUserRoleQueryService sysUserRoleQueryService;

    @Mock
    private WipeSysUserRoleByIdsDomainService wipeSysUserRoleByIdsDomainService;

    @Mock
    private SysUserRoleDomainRepository sysUserRoleDomainRepository;

    @Mock
    private SysUserRoleDomainFactory sysUserRoleDomainFactory;

    @InjectMocks
    private LinkUsersAndRolesDomainServiceImpl service;

    @Test
    @DisplayName("link 应为指定用户和角色创建关联")
    void link_shouldCreateAssociations() {
        SysUserRoleView existing = new SysUserRoleView();
        existing.setId(100L);

        when(sysUserRoleQueryService.queryLinkUserAndRole(1L)).thenReturn(Mono.just(List.of(existing)));
        when(wipeSysUserRoleByIdsDomainService.deleteByIds(anyList())).thenReturn(Mono.empty());

        SysUserRoleDomain domain = mock(SysUserRoleDomain.class);
        when(sysUserRoleDomainFactory.newInstance(any(UserId.class), any(RoleId.class), any())).thenReturn(domain);
        when(sysUserRoleDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(service.link(1L, List.of(10L, 20L)))
                .verifyComplete();

        verify(sysUserRoleDomainFactory, times(2)).newInstance(any(UserId.class), any(RoleId.class), any());
        verify(sysUserRoleDomainRepository, times(2)).save(any());
    }
}
