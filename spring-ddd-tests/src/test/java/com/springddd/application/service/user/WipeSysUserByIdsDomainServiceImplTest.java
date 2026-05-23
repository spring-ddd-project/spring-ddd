package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserRoleView;
import com.springddd.infrastructure.persistence.r2dbc.SysUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeSysUserByIdsDomainServiceImplTest {

    @Mock
    private SysUserRepository sysUserRepository;

    @Mock
    private SysUserRoleQueryService sysUserRoleQueryService;

    @Mock
    private SysUserRoleCommandService sysUserRoleCommandService;

    @InjectMocks
    private WipeSysUserByIdsDomainServiceImpl service;

    @Test
    @DisplayName("deleteByIds 应删除用户及其角色关联")
    void deleteByIds_shouldWipeUsersAndRelations() {
        SysUserRoleView ur = new SysUserRoleView();
        ur.setId(100L);

        when(sysUserRoleQueryService.queryLinkUserAndRole(1L)).thenReturn(Mono.just(List.of(ur)));
        when(sysUserRoleCommandService.wipe(anyList())).thenReturn(Mono.empty());
        when(sysUserRepository.deleteAllById(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteByIds(List.of(1L)))
                .verifyComplete();

        verify(sysUserRoleCommandService).wipe(List.of(100L));
        verify(sysUserRepository).deleteAllById(List.of(1L));
    }
}
