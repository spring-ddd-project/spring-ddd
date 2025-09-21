package com.springddd.application.service.role;

import com.springddd.domain.role.LinkRoleAndMenusDomainService;
import com.springddd.domain.role.WipeSysRoleMenuByIdsDomainService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SysRoleMenuCommandServiceTest {

    @Mock
    private LinkRoleAndMenusDomainService linkRoleAndMenusDomainService;

    @Mock
    private WipeSysRoleMenuByIdsDomainService wipeSysRoleMenuByIdsDomainService;

    @InjectMocks
    private SysRoleMenuCommandService sysRoleMenuCommandService;

    @Test
    void create_shouldLinkRoleAndMenus() {
        Long roleId = 1L;
        List<Long> menuIds = Arrays.asList(1L, 2L, 3L);
        when(linkRoleAndMenusDomainService.link(roleId, menuIds)).thenReturn(Mono.empty());

        StepVerifier.create(sysRoleMenuCommandService.create(roleId, menuIds))
                .verifyComplete();

        verify(linkRoleAndMenusDomainService).link(roleId, menuIds);
    }

    @Test
    void wipe_shouldDeleteRoleMenuLinks() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(wipeSysRoleMenuByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysRoleMenuCommandService.wipe(ids))
                .verifyComplete();

        verify(wipeSysRoleMenuByIdsDomainService).deleteByIds(ids);
    }

    @Test
    void create_shouldPassCorrectParameters() {
        Long roleId = 5L;
        List<Long> menuIds = Arrays.asList(10L, 20L, 30L);
        when(linkRoleAndMenusDomainService.link(roleId, menuIds)).thenReturn(Mono.empty());

        sysRoleMenuCommandService.create(roleId, menuIds);

        verify(linkRoleAndMenusDomainService).link(roleId, menuIds);
    }

    @Test
    void wipe_shouldPassCorrectIds() {
        List<Long> ids = Arrays.asList(100L, 200L);
        when(wipeSysRoleMenuByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        sysRoleMenuCommandService.wipe(ids);

        verify(wipeSysRoleMenuByIdsDomainService).deleteByIds(ids);
    }
}
