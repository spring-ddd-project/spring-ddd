package com.springddd.application.service.user;

import com.springddd.domain.user.LinkUsersAndRolesDomainService;
import com.springddd.domain.user.WipeSysUserRoleByIdsDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SysUserRoleCommandServiceTest {

    @Mock
    private LinkUsersAndRolesDomainService linkUsersAndRolesDomainService;

    @Mock
    private WipeSysUserRoleByIdsDomainService wipeSysUserRoleByIdsDomainService;

    private SysUserRoleCommandService sysUserRoleCommandService;

    @BeforeEach
    void setUp() {
        sysUserRoleCommandService = new SysUserRoleCommandService(
                linkUsersAndRolesDomainService,
                wipeSysUserRoleByIdsDomainService
        );
    }

    @Test
    void create_shouldDelegateToDomainService() {
        Long userId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L, 3L);

        when(linkUsersAndRolesDomainService.link(userId, roleIds)).thenReturn(Mono.empty());

        Mono<Void> result = sysUserRoleCommandService.create(userId, roleIds);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipe_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);

        when(wipeSysUserRoleByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = sysUserRoleCommandService.wipe(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
