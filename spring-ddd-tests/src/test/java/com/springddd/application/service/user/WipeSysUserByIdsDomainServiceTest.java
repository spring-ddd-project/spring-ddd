package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserRoleView;
import com.springddd.infrastructure.persistence.r2dbc.SysUserRepository;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeSysUserByIdsDomainServiceTest {

    @Mock
    private SysUserRepository sysUserRepository;

    @Mock
    private SysUserRoleQueryService sysUserRoleQueryService;

    @Mock
    private SysUserRoleCommandService sysUserRoleCommandService;

    @InjectMocks
    private WipeSysUserByIdsDomainServiceImpl wipeSysUserByIdsDomainService;

    @Test
    void deleteByIds_shouldWipeAllRelatedDataAndDeleteUsers() {
        List<Long> ids = Arrays.asList(1L, 2L);
        SysUserRoleView userRoleView = new SysUserRoleView();
        userRoleView.setId(1L);

        when(sysUserRoleQueryService.queryLinkUserAndRole(anyLong()))
                .thenReturn(Mono.just(Collections.singletonList(userRoleView)));
        when(sysUserRoleCommandService.wipe(anyList())).thenReturn(Mono.empty());
        when(sysUserRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysUserByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysUserRepository).deleteAllById(ids);
    }

    @Test
    void deleteByIds_shouldHandleEmptyUserRoleList() {
        List<Long> ids = Collections.singletonList(1L);

        when(sysUserRoleQueryService.queryLinkUserAndRole(anyLong()))
                .thenReturn(Mono.just(Collections.emptyList()));
        when(sysUserRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysUserByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysUserRoleCommandService, never()).wipe(anyList());
        verify(sysUserRepository).deleteAllById(ids);
    }

    @Test
    void deleteByIds_shouldHandleEmptyIdsList() {
        List<Long> ids = Collections.emptyList();

        when(sysUserRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysUserByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysUserRepository).deleteAllById(ids);
    }

    @Test
    void deleteByIds_shouldDeleteMultipleUsersWithUserRoles() {
        List<Long> ids = Arrays.asList(1L, 2L);
        SysUserRoleView userRoleView1 = new SysUserRoleView();
        userRoleView1.setId(1L);
        SysUserRoleView userRoleView2 = new SysUserRoleView();
        userRoleView2.setId(2L);

        when(sysUserRoleQueryService.queryLinkUserAndRole(1L))
                .thenReturn(Mono.just(Collections.singletonList(userRoleView1)));
        when(sysUserRoleQueryService.queryLinkUserAndRole(2L))
                .thenReturn(Mono.just(Collections.singletonList(userRoleView2)));
        when(sysUserRoleCommandService.wipe(anyList())).thenReturn(Mono.empty());
        when(sysUserRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysUserByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysUserRoleQueryService).queryLinkUserAndRole(1L);
        verify(sysUserRoleQueryService).queryLinkUserAndRole(2L);
        verify(sysUserRepository).deleteAllById(ids);
    }

    @Test
    void deleteByIds_shouldPassCorrectIdsToDelete() {
        List<Long> ids = Arrays.asList(100L, 200L, 300L);

        when(sysUserRoleQueryService.queryLinkUserAndRole(anyLong()))
                .thenReturn(Mono.just(Collections.emptyList()));
        when(sysUserRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysUserByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysUserRepository).deleteAllById(Arrays.asList(100L, 200L, 300L));
    }
}
