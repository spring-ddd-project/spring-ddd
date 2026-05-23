package com.springddd.application.service.role;

import com.springddd.infrastructure.persistence.r2dbc.SysRoleMenuRepository;
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
class WipeSysRoleMenuByIdsDomainServiceImplTest {

    @Mock
    private SysRoleMenuRepository sysRoleMenuRepository;

    @InjectMocks
    private WipeSysRoleMenuByIdsDomainServiceImpl service;

    @Test
    @DisplayName("deleteByIds 应物理删除角色菜单关联")
    void deleteByIds_shouldWipeRoleMenus() {
        when(sysRoleMenuRepository.deleteAllById(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteByIds(List.of(1L)))
                .verifyComplete();

        verify(sysRoleMenuRepository).deleteAllById(List.of(1L));
    }
}
