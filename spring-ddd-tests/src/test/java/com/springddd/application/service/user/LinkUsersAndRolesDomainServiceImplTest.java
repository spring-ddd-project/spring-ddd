package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserRoleView;
import com.springddd.domain.role.RoleId;
import com.springddd.domain.user.*;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LinkUsersAndRolesDomainServiceImplTest {

    @Mock
    private SysUserRoleQueryService sysUserRoleQueryService;

    @Mock
    private WipeSysUserRoleByIdsDomainService wipeSysUserRoleByIdsDomainService;

    @Mock
    private SysUserRoleDomainRepository sysUserRoleDomainRepository;

    @Mock
    private SysUserRoleDomainFactory sysUserRoleDomainFactory;

    @InjectMocks
    private LinkUsersAndRolesDomainServiceImpl linkUsersAndRolesDomainService;

    private SysUserRoleDomain mockUserRoleDomain;

    @BeforeEach
    void setUp() {
        mockUserRoleDomain = new SysUserRoleDomain();
        mockUserRoleDomain.setUserRoleId(new UserRoleId(1L));
        mockUserRoleDomain.setUserId(new UserId(1L));
        mockUserRoleDomain.setRoleId(new RoleId(10L));
        mockUserRoleDomain.setDeleteStatus(false);
    }

    @Test
    void link_shouldDeleteExistingAndCreateNewLinks() {
        Long userId = 1L;
        List<Long> roleIds = Arrays.asList(10L, 20L, 30L);

        SysUserRoleView existingView = new SysUserRoleView();
        existingView.setId(1L);
        existingView.setUserId(userId);
        existingView.setRoleId(5L);

        when(sysUserRoleQueryService.queryLinkUserAndRole(userId))
                .thenReturn(Mono.just(Collections.singletonList(existingView)));
        when(wipeSysUserRoleByIdsDomainService.deleteByIds(anyList())).thenReturn(Mono.empty());
        when(sysUserRoleDomainFactory.newInstance(any(UserId.class), any(RoleId.class), any()))
                .thenReturn(mockUserRoleDomain);
        when(sysUserRoleDomainRepository.save(any(SysUserRoleDomain.class)))
                .thenReturn(Mono.just(1L));

        StepVerifier.create(linkUsersAndRolesDomainService.link(userId, roleIds))
                .verifyComplete();

        verify(wipeSysUserRoleByIdsDomainService).deleteByIds(Collections.singletonList(1L));
        verify(sysUserRoleDomainRepository, times(3)).save(any(SysUserRoleDomain.class));
    }

    @Test
    void link_shouldHandleEmptyExistingLinks() {
        Long userId = 1L;
        List<Long> roleIds = Arrays.asList(10L, 20L);

        when(sysUserRoleQueryService.queryLinkUserAndRole(userId))
                .thenReturn(Mono.just(Collections.emptyList()));
        when(wipeSysUserRoleByIdsDomainService.deleteByIds(anyList())).thenReturn(Mono.empty());
        when(sysUserRoleDomainFactory.newInstance(any(UserId.class), any(RoleId.class), any()))
                .thenReturn(mockUserRoleDomain);
        when(sysUserRoleDomainRepository.save(any(SysUserRoleDomain.class)))
                .thenReturn(Mono.just(1L));

        StepVerifier.create(linkUsersAndRolesDomainService.link(userId, roleIds))
                .verifyComplete();

        verify(sysUserRoleDomainRepository, times(2)).save(any(SysUserRoleDomain.class));
    }

    @Test
    void link_shouldCreateNewUserRoleDomains() {
        Long userId = 5L;
        List<Long> roleIds = Arrays.asList(100L, 200L);

        when(sysUserRoleQueryService.queryLinkUserAndRole(userId))
                .thenReturn(Mono.just(Collections.emptyList()));
        when(wipeSysUserRoleByIdsDomainService.deleteByIds(anyList())).thenReturn(Mono.empty());
        when(sysUserRoleDomainFactory.newInstance(eq(new UserId(userId)), any(RoleId.class), any()))
                .thenReturn(mockUserRoleDomain);
        when(sysUserRoleDomainRepository.save(any(SysUserRoleDomain.class)))
                .thenReturn(Mono.just(1L));

        StepVerifier.create(linkUsersAndRolesDomainService.link(userId, roleIds))
                .verifyComplete();

        verify(sysUserRoleDomainFactory, times(2)).newInstance(eq(new UserId(userId)), any(RoleId.class), any());
    }

    @Test
    void link_shouldCallWipeWithExistingLinkIds() {
        Long userId = 1L;
        List<Long> roleIds = Arrays.asList(10L, 20L);

        SysUserRoleView existingView1 = new SysUserRoleView();
        existingView1.setId(100L);
        existingView1.setUserId(userId);
        existingView1.setRoleId(1L);

        SysUserRoleView existingView2 = new SysUserRoleView();
        existingView2.setId(200L);
        existingView2.setUserId(userId);
        existingView2.setRoleId(2L);

        when(sysUserRoleQueryService.queryLinkUserAndRole(userId))
                .thenReturn(Mono.just(Arrays.asList(existingView1, existingView2)));
        when(wipeSysUserRoleByIdsDomainService.deleteByIds(anyList())).thenReturn(Mono.empty());
        when(sysUserRoleDomainFactory.newInstance(any(UserId.class), any(RoleId.class), any()))
                .thenReturn(mockUserRoleDomain);
        when(sysUserRoleDomainRepository.save(any(SysUserRoleDomain.class)))
                .thenReturn(Mono.just(1L));

        StepVerifier.create(linkUsersAndRolesDomainService.link(userId, roleIds))
                .verifyComplete();

        verify(wipeSysUserRoleByIdsDomainService).deleteByIds(Arrays.asList(100L, 200L));
    }

    @Test
    void link_shouldHandleSingleRoleLink() {
        Long userId = 1L;
        List<Long> roleIds = Collections.singletonList(10L);

        when(sysUserRoleQueryService.queryLinkUserAndRole(userId))
                .thenReturn(Mono.just(Collections.emptyList()));
        when(wipeSysUserRoleByIdsDomainService.deleteByIds(anyList())).thenReturn(Mono.empty());
        when(sysUserRoleDomainFactory.newInstance(any(UserId.class), any(RoleId.class), any()))
                .thenReturn(mockUserRoleDomain);
        when(sysUserRoleDomainRepository.save(any(SysUserRoleDomain.class)))
                .thenReturn(Mono.just(1L));

        StepVerifier.create(linkUsersAndRolesDomainService.link(userId, roleIds))
                .verifyComplete();

        verify(sysUserRoleDomainRepository, times(1)).save(any(SysUserRoleDomain.class));
    }
}
