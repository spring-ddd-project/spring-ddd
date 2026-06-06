package com.springddd.application.service.auth;

import com.springddd.application.service.menu.SysMenuQueryService;
import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.application.service.role.SysRoleMenuQueryService;
import com.springddd.application.service.role.SysRoleQueryService;
import com.springddd.application.service.role.dto.SysRoleMenuView;
import com.springddd.application.service.role.dto.SysRoleView;
import com.springddd.application.service.user.SysUserQueryService;
import com.springddd.application.service.user.SysUserRoleQueryService;
import com.springddd.application.service.user.dto.SysUserRoleView;
import com.springddd.application.service.user.dto.SysUserView;
import com.springddd.domain.auth.AuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthReactiveUserDetailsServiceTest {

    @Mock
    private SysUserQueryService sysUserQueryService;

    @Mock
    private SysUserRoleQueryService sysUserRoleQueryService;

    @Mock
    private SysRoleMenuQueryService sysRoleMenuQueryService;

    @Mock
    private SysMenuQueryService sysMenuQueryService;

    @Mock
    private SysRoleQueryService sysRoleQueryService;

    private AuthReactiveUserDetailsService service;

    @BeforeEach
    void setUp() {
        service = new AuthReactiveUserDetailsService(
                sysUserQueryService,
                sysUserRoleQueryService,
                sysRoleMenuQueryService,
                sysMenuQueryService,
                sysRoleQueryService
        );
    }

    @Test
    void findByUsername_shouldReturnUserDetails() {
        SysUserView userView = new SysUserView();
        userView.setId(1L);
        userView.setUsername("admin");
        userView.setPassword("encodedPassword");
        userView.setLockStatus(false);

        SysUserRoleView userRole = new SysUserRoleView();
        userRole.setRoleId(1L);

        SysRoleView roleView = new SysRoleView();
        roleView.setId(1L);
        roleView.setRoleCode("ROLE_ADMIN");

        SysRoleMenuView roleMenu = new SysRoleMenuView();
        roleMenu.setMenuId(1L);

        SysMenuView menuView = new SysMenuView();
        menuView.setId(1L);
        menuView.setPermission("sys:user:list");

        when(sysUserQueryService.queryUserByUsername("admin")).thenReturn(Mono.just(userView));
        when(sysUserRoleQueryService.queryLinkUserAndRole(1L)).thenReturn(Mono.just(List.of(userRole)));
        when(sysRoleQueryService.getById(1L)).thenReturn(Mono.just(roleView));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(1L)).thenReturn(Mono.just(List.of(roleMenu)));
        when(sysMenuQueryService.queryByMenuId(1L)).thenReturn(Mono.just(menuView));

        StepVerifier.create(service.findByUsername("admin"))
                .assertNext(userDetails -> {
                    assertTrue(userDetails instanceof AuthUser);
                    AuthUser authUser = (AuthUser) userDetails;
                    assertEquals("admin", authUser.getUsername());
                    assertEquals("encodedPassword", authUser.getPassword());
                    assertEquals(List.of("ROLE_ADMIN"), authUser.getRoles());
                    assertEquals(List.of("sys:user:list"), authUser.getPermissions());
                    assertTrue(authUser.isAccountNonLocked());
                    assertTrue(authUser.isEnabled());
                })
                .verifyComplete();
    }

    @Test
    void findByUsername_shouldReturnEmpty_whenUserNotFound() {
        when(sysUserQueryService.queryUserByUsername("unknown")).thenReturn(Mono.empty());

        StepVerifier.create(service.findByUsername("unknown"))
                .verifyComplete();
    }

    @Test
    void findByUsername_shouldReturnUserWithEmptyRoles_whenNoUserRoles() {
        SysUserView userView = new SysUserView();
        userView.setId(1L);
        userView.setUsername("admin");
        userView.setPassword("encodedPassword");
        userView.setLockStatus(false);

        when(sysUserQueryService.queryUserByUsername("admin")).thenReturn(Mono.just(userView));
        when(sysUserRoleQueryService.queryLinkUserAndRole(1L)).thenReturn(Mono.just(Collections.emptyList()));

        StepVerifier.create(service.findByUsername("admin"))
                .assertNext(userDetails -> {
                    AuthUser authUser = (AuthUser) userDetails;
                    assertEquals("admin", authUser.getUsername());
                    assertTrue(authUser.getRoles().isEmpty());
                    assertTrue(authUser.getMenuIds().isEmpty());
                    assertTrue(authUser.getPermissions().isEmpty());
                })
                .verifyComplete();
    }

    @Test
    void findByUsername_shouldReturnUserWithEmptyMenus_whenNoRoleMenus() {
        SysUserView userView = new SysUserView();
        userView.setId(1L);
        userView.setUsername("admin");
        userView.setPassword("encodedPassword");
        userView.setLockStatus(false);

        SysUserRoleView userRole = new SysUserRoleView();
        userRole.setRoleId(1L);

        SysRoleView roleView = new SysRoleView();
        roleView.setId(1L);
        roleView.setRoleCode("ROLE_ADMIN");

        when(sysUserQueryService.queryUserByUsername("admin")).thenReturn(Mono.just(userView));
        when(sysUserRoleQueryService.queryLinkUserAndRole(1L)).thenReturn(Mono.just(List.of(userRole)));
        when(sysRoleQueryService.getById(1L)).thenReturn(Mono.just(roleView));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(1L)).thenReturn(Mono.just(Collections.emptyList()));

        StepVerifier.create(service.findByUsername("admin"))
                .assertNext(userDetails -> {
                    AuthUser authUser = (AuthUser) userDetails;
                    assertEquals(List.of("ROLE_ADMIN"), authUser.getRoles());
                    assertTrue(authUser.getMenuIds().isEmpty());
                    assertTrue(authUser.getPermissions().isEmpty());
                })
                .verifyComplete();
    }

    @Test
    void findByUsername_shouldFilterEmptyPermissions() {
        SysUserView userView = new SysUserView();
        userView.setId(1L);
        userView.setUsername("admin");
        userView.setPassword("encodedPassword");
        userView.setLockStatus(false);

        SysUserRoleView userRole = new SysUserRoleView();
        userRole.setRoleId(1L);

        SysRoleView roleView = new SysRoleView();
        roleView.setId(1L);
        roleView.setRoleCode("ROLE_ADMIN");

        SysRoleMenuView roleMenu = new SysRoleMenuView();
        roleMenu.setMenuId(1L);

        SysMenuView menuView = new SysMenuView();
        menuView.setId(1L);
        menuView.setPermission("");

        when(sysUserQueryService.queryUserByUsername("admin")).thenReturn(Mono.just(userView));
        when(sysUserRoleQueryService.queryLinkUserAndRole(1L)).thenReturn(Mono.just(List.of(userRole)));
        when(sysRoleQueryService.getById(1L)).thenReturn(Mono.just(roleView));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(1L)).thenReturn(Mono.just(List.of(roleMenu)));
        when(sysMenuQueryService.queryByMenuId(1L)).thenReturn(Mono.just(menuView));

        StepVerifier.create(service.findByUsername("admin"))
                .assertNext(userDetails -> {
                    AuthUser authUser = (AuthUser) userDetails;
                    assertTrue(authUser.getPermissions().isEmpty());
                })
                .verifyComplete();
    }
}
