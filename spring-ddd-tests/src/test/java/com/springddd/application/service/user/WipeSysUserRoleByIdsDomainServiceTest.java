package com.springddd.application.service.user;

import com.springddd.domain.user.WipeSysUserRoleByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysUserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeSysUserRoleByIdsDomainServiceTest {

    @Mock
    private SysUserRoleRepository sysUserRoleRepository;

    @InjectMocks
    private WipeSysUserRoleByIdsDomainServiceImpl wipeSysUserRoleByIdsDomainService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void deleteByIds_shouldDeleteUserRoles() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(sysUserRoleRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysUserRoleByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysUserRoleRepository).deleteAllById(ids);
    }

    @Test
    void deleteByIds_shouldHandleEmptyIdsList() {
        List<Long> ids = Collections.emptyList();
        when(sysUserRoleRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysUserRoleByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysUserRoleRepository).deleteAllById(ids);
    }

    @Test
    void deleteByIds_shouldPassCorrectIds() {
        List<Long> ids = Arrays.asList(100L, 200L, 300L);
        when(sysUserRoleRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        wipeSysUserRoleByIdsDomainService.deleteByIds(ids);

        verify(sysUserRoleRepository).deleteAllById(Arrays.asList(100L, 200L, 300L));
    }
}
