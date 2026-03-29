package com.springddd.domain.user;

import com.springddd.infrastructure.persistence.r2dbc.SysUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeSysUserByIdsDomainServiceTest {

    @Mock
    private SysUserRepository sysUserRepository;

    @InjectMocks
    private WipeSysUserByIdsDomainService domainService;

    @Test
    void deleteByIds_shouldDeleteEntitiesPermanently() {
        when(sysUserRepository.deleteAllById(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(domainService.deleteByIds(Arrays.asList(1L, 2L)))
                .verifyComplete();

        verify(sysUserRepository, times(1)).deleteAllById(anyList());
    }

    @Test
    void deleteByIds_shouldHandleEmptyList() {
        when(sysUserRepository.deleteAllById(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(domainService.deleteByIds(Arrays.asList()))
                .verifyComplete();

        verify(sysUserRepository, times(1)).deleteAllById(anyList());
    }
}
