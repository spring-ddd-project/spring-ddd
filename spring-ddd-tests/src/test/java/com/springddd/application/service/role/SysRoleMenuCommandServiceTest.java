package com.springddd.application.service.role;

import com.springddd.domain.role.LinkRoleAndMenusDomainService;
import com.springddd.domain.role.WipeSysRoleMenuByIdsDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SysRoleMenuCommandServiceTest {

    @Mock
    private LinkRoleAndMenusDomainService linkRoleAndMenusDomainService;

    @Mock
    private WipeSysRoleMenuByIdsDomainService wipeSysRoleMenuByIdsDomainService;

    @InjectMocks
    private SysRoleMenuCommandService service;

    @Test
    @DisplayName("create 应调用 link 领域服务")
    void create_shouldCallDomainService() {
        Long roleId = 1L;
        List<Long> menuIds = List.of(1L, 2L);
        when(linkRoleAndMenusDomainService.link(roleId, menuIds)).thenReturn(Mono.empty());

        StepVerifier.create(service.create(roleId, menuIds))
                .verifyComplete();

        verify(linkRoleAndMenusDomainService).link(roleId, menuIds);
    }

    @Test
    @DisplayName("wipe 应调用 wipe 领域服务")
    void wipe_shouldCallDomainService() {
        List<Long> ids = List.of(1L, 2L);
        when(wipeSysRoleMenuByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(service.wipe(ids))
                .verifyComplete();

        verify(wipeSysRoleMenuByIdsDomainService).deleteByIds(ids);
    }
}
