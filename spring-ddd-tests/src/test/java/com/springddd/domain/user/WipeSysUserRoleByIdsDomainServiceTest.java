package com.springddd.domain.user;

import com.springddd.infrastructure.persistence.r2dbc.SysUserRoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeSysUserRoleByIdsDomainServiceTest {

    @Mock
    private SysUserRoleRepository sysUserRoleRepository;

    @InjectMocks
    private WipeSysUserRoleByIdsDomainService domainService;

    @Test
    void deleteByIds_shouldDeleteEntitiesPermanently() {
        when(sysUserRoleRepository.deleteAllById(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(domainService.deleteByIds(Arrays.asList(1L, 2L)))
                .verifyComplete();

        verify(sysUserRoleRepository, times(1)).deleteAllById(Arrays.asList(1L, 2L));
    }

    @Test
    void deleteByIds_shouldHandleEmptyList() {
        when(sysUserRoleRepository.deleteAllById(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(domainService.deleteByIds(Arrays.asList()))
                .verifyComplete();

        verify(sysUserRoleRepository, times(1)).deleteAllById(Arrays.asList());
    }
}
