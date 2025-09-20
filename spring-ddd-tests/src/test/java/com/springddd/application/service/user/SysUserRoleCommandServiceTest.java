package com.springddd.application.service.user;

import com.springddd.domain.user.LinkUsersAndRolesDomainService;
import com.springddd.domain.user.WipeSysUserRoleByIdsDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SysUserRoleCommandServiceTest {

    @Mock
    private LinkUsersAndRolesDomainService linkUsersAndRolesDomainService;

    @Mock
    private WipeSysUserRoleByIdsDomainService wipeSysUserRoleByIdsDomainService;

    @InjectMocks
    private SysUserRoleCommandService sysUserRoleCommandService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void create_shouldCallLinkUsersAndRolesDomainService() {
        Long userId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        when(linkUsersAndRolesDomainService.link(userId, roleIds)).thenReturn(Mono.empty());

        StepVerifier.create(sysUserRoleCommandService.create(userId, roleIds))
                .verifyComplete();

        verify(linkUsersAndRolesDomainService).link(userId, roleIds);
    }

    @Test
    void create_shouldPassCorrectParameters() {
        Long userId = 5L;
        List<Long> roleIds = Arrays.asList(10L, 20L);
        when(linkUsersAndRolesDomainService.link(userId, roleIds)).thenReturn(Mono.empty());

        sysUserRoleCommandService.create(userId, roleIds);

        verify(linkUsersAndRolesDomainService).link(5L, Arrays.asList(10L, 20L));
    }

    @Test
    void wipe_shouldCallWipeSysUserRoleByIdsDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(wipeSysUserRoleByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysUserRoleCommandService.wipe(ids))
                .verifyComplete();

        verify(wipeSysUserRoleByIdsDomainService).deleteByIds(ids);
    }

    @Test
    void wipe_shouldPassCorrectIds() {
        List<Long> ids = Arrays.asList(100L, 200L);
        when(wipeSysUserRoleByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        sysUserRoleCommandService.wipe(ids);

        verify(wipeSysUserRoleByIdsDomainService).deleteByIds(Arrays.asList(100L, 200L));
    }

    @Test
    void wipe_withEmptyList_shouldStillCallService() {
        List<Long> ids = Arrays.asList();
        when(wipeSysUserRoleByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysUserRoleCommandService.wipe(ids))
                .verifyComplete();

        verify(wipeSysUserRoleByIdsDomainService).deleteByIds(ids);
    }
}
