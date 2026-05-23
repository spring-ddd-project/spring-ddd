package com.springddd.application.service.user;

import com.springddd.infrastructure.persistence.r2dbc.SysUserRoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeSysUserRoleByIdsDomainServiceImplTest {

    @Mock
    private SysUserRoleRepository sysUserRoleRepository;

    @InjectMocks
    private WipeSysUserRoleByIdsDomainServiceImpl service;

    @Test
    @DisplayName("deleteByIds 应物理删除用户角色关联")
    void deleteByIds_shouldWipeUserRoles() {
        when(sysUserRoleRepository.deleteAllById(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteByIds(List.of(1L)))
                .verifyComplete();

        verify(sysUserRoleRepository).deleteAllById(List.of(1L));
    }
}
