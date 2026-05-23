package com.springddd.application.service.user;

import com.springddd.domain.user.LinkUsersAndRolesDomainService;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SysUserRoleCommandServiceTest {

    @Mock
    private LinkUsersAndRolesDomainService linkUsersAndRolesDomainService;

    @Mock
    private WipeSysUserRoleByIdsDomainService wipeSysUserRoleByIdsDomainService;

    @InjectMocks
    private SysUserRoleCommandService sysUserRoleCommandService;

    @Test
    @DisplayName("create 应调用 link domain service")
    void create_shouldCallLinkService() {
        when(linkUsersAndRolesDomainService.link(1L, List.of(1L, 2L))).thenReturn(Mono.empty());

        StepVerifier.create(sysUserRoleCommandService.create(1L, List.of(1L, 2L)))
                .verifyComplete();

        verify(linkUsersAndRolesDomainService).link(1L, List.of(1L, 2L));
    }

    @Test
    @DisplayName("wipe 应调用 wipe domain service")
    void wipe_shouldCallWipeService() {
        when(wipeSysUserRoleByIdsDomainService.deleteByIds(List.of(1L, 2L))).thenReturn(Mono.empty());

        StepVerifier.create(sysUserRoleCommandService.wipe(List.of(1L, 2L)))
                .verifyComplete();

        verify(wipeSysUserRoleByIdsDomainService).deleteByIds(List.of(1L, 2L));
    }
}
