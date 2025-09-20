package com.springddd.application.service.role;

import com.springddd.domain.role.WipeSysRoleMenuByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleMenuRepository;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeSysRoleMenuByIdsDomainServiceTest {

    @Mock
    private SysRoleMenuRepository sysRoleMenuRepository;

    @InjectMocks
    private WipeSysRoleMenuByIdsDomainServiceImpl wipeSysRoleMenuByIdsDomainService;

    @Test
    void deleteByIds_shouldDeleteFromRepository() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(sysRoleMenuRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysRoleMenuByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysRoleMenuRepository).deleteAllById(ids);
    }

    @Test
    void deleteByIds_shouldHandleEmptyIdsList() {
        List<Long> ids = Collections.emptyList();
        when(sysRoleMenuRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysRoleMenuByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysRoleMenuRepository).deleteAllById(ids);
    }

    @Test
    void deleteByIds_shouldHandleSingleId() {
        List<Long> ids = Collections.singletonList(1L);
        when(sysRoleMenuRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysRoleMenuByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysRoleMenuRepository).deleteAllById(ids);
    }

    @Test
    void deleteByIds_shouldPassCorrectIds() {
        List<Long> ids = Arrays.asList(100L, 200L, 300L);
        when(sysRoleMenuRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        wipeSysRoleMenuByIdsDomainService.deleteByIds(ids);

        verify(sysRoleMenuRepository).deleteAllById(ids);
    }
}
