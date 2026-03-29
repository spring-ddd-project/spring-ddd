package com.springddd.domain.role;

import com.springddd.infrastructure.persistence.r2dbc.SysRoleMenuRepository;
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
class WipeSysRoleMenuByIdsDomainServiceTest {

    @Mock
    private SysRoleMenuRepository sysRoleMenuRepository;

    @InjectMocks
    private WipeSysRoleMenuByIdsDomainServiceImpl domainService;

    @Test
    void deleteByIds_shouldDeleteEntitiesPermanently() {
        when(sysRoleMenuRepository.deleteAllById(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(domainService.deleteByIds(Arrays.asList(1L, 2L, 3L)))
                .verifyComplete();

        verify(sysRoleMenuRepository, times(1)).deleteAllById(Arrays.asList(1L, 2L, 3L));
    }

    @Test
    void deleteByIds_shouldHandleEmptyList() {
        StepVerifier.create(domainService.deleteByIds(Arrays.asList()))
                .verifyComplete();

        verify(sysRoleMenuRepository, never()).deleteAllById(anyList());
    }
}
