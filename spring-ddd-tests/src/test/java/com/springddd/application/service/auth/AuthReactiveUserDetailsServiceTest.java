package com.springddd.application.service.auth;

import com.springddd.application.service.menu.SysMenuQueryService;
import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.application.service.role.SysRoleMenuQueryService;
import com.springddd.application.service.role.dto.SysRoleMenuView;
import com.springddd.application.service.role.SysRoleQueryService;
import com.springddd.application.service.role.dto.SysRoleView;
import com.springddd.application.service.user.SysUserQueryService;
import com.springddd.application.service.user.SysUserRoleQueryService;
import com.springddd.application.service.user.dto.SysUserRoleView;
import com.springddd.application.service.user.dto.SysUserView;
import com.springddd.domain.auth.AuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthReactiveUserDetailsService Tests")
class AuthReactiveUserDetailsServiceTest {

    @InjectMocks
    private AuthReactiveUserDetailsService authReactiveUserDetailsService;

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

    private SysUserView testSysUserView;
    private SysRoleView testRoleView;
    private SysMenuView testMenuView;
    private SysUserRoleView testUserRoleView;
    private SysRoleMenuView testRoleMenuView;

    @BeforeEach
    void setUp() {
        testSysUserView = new SysUserView();
        testSysUserView.setId(1L);
        testSysUserView.setUsername("testuser");
        testSysUserView.setPassword("encoded-password");
        testSysUserView.setDeleteStatus(false);

        testRoleView = new SysRoleView();
        testRoleView.setId(100L);
        testRoleView.setRoleCode("ROLE_ADMIN");
        testRoleView.setDeleteStatus(false);

        testMenuView = new SysMenuView();
        testMenuView.setId(10L);
        testMenuView.setPermission("system:user:read");
        testMenuView.setDeleteStatus(false);

        testUserRoleView = new SysUserRoleView();
        testUserRoleView.setUserId(1L);
        testUserRoleView.setRoleId(100L);
        testUserRoleView.setDeleteStatus(false);

        testRoleMenuView = new SysRoleMenuView();
        testRoleMenuView.setRoleId(100L);
        testRoleMenuView.setMenuId(10L);
        testRoleMenuView.setDeleteStatus(false);
    }

    @Test
    @DisplayName("findByUsername() should return UserDetails with roles and permissions")
    void findByUsername_WithValidUser_ReturnsUserDetailsWithRolesAndPermissions() {
        when(sysUserQueryService.queryUserByUsername("testuser"))
                .thenReturn(Mono.just(testSysUserView));
        when(sysUserRoleQueryService.queryLinkUserAndRole(1L))
                .thenReturn(Mono.just(List.of(testUserRoleView)));
        when(sysRoleQueryService.getById(100L))
                .thenReturn(Mono.just(testRoleView));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(100L))
                .thenReturn(Mono.just(List.of(testRoleMenuView)));
        when(sysMenuQueryService.queryByMenuId(10L))
                .thenReturn(Mono.just(testMenuView));

        StepVerifier.create(authReactiveUserDetailsService.findByUsername("testuser"))
                .assertNext(userDetails -> {
                    assertThat(userDetails).isNotNull();
                    assertThat(userDetails.getUsername()).isEqualTo("testuser");
                    assertThat(userDetails.getPassword()).isEqualTo("encoded-password");
                    assertThat(userDetails.getAuthorities())
                            .extracting("authority")
                            .contains("system:user:read");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("findByUsername() should return empty when user not found")
    void findByUsername_WithNonExistentUser_ReturnsEmpty() {
        when(sysUserQueryService.queryUserByUsername("nonexistent"))
                .thenReturn(Mono.empty());

        StepVerifier.create(authReactiveUserDetailsService.findByUsername("nonexistent"))
                .verifyComplete();
    }

    @Test
    @DisplayName("findByUsername() should handle multiple roles and menus")
    void findByUsername_WithMultipleRolesAndMenus_ReturnsAggregatedPermissions() {
        SysRoleView roleView2 = new SysRoleView();
        roleView2.setId(200L);
        roleView2.setRoleCode("ROLE_USER");
        roleView2.setDeleteStatus(false);

        SysMenuView menuView2 = new SysMenuView();
        menuView2.setId(20L);
        menuView2.setPermission("system:user:write");
        menuView2.setDeleteStatus(false);

        SysUserRoleView userRoleView2 = new SysUserRoleView();
        userRoleView2.setUserId(1L);
        userRoleView2.setRoleId(200L);
        userRoleView2.setDeleteStatus(false);

        SysRoleMenuView roleMenuView2 = new SysRoleMenuView();
        roleMenuView2.setRoleId(200L);
        roleMenuView2.setMenuId(20L);
        roleMenuView2.setDeleteStatus(false);

        when(sysUserQueryService.queryUserByUsername("testuser"))
                .thenReturn(Mono.just(testSysUserView));
        when(sysUserRoleQueryService.queryLinkUserAndRole(1L))
                .thenReturn(Mono.just(List.of(testUserRoleView, userRoleView2)));
        when(sysRoleQueryService.getById(100L))
                .thenReturn(Mono.just(testRoleView));
        when(sysRoleQueryService.getById(200L))
                .thenReturn(Mono.just(roleView2));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(100L))
                .thenReturn(Mono.just(List.of(testRoleMenuView)));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(200L))
                .thenReturn(Mono.just(List.of(roleMenuView2)));
        when(sysMenuQueryService.queryByMenuId(10L))
                .thenReturn(Mono.just(testMenuView));
        when(sysMenuQueryService.queryByMenuId(20L))
                .thenReturn(Mono.just(menuView2));

        StepVerifier.create(authReactiveUserDetailsService.findByUsername("testuser"))
                .assertNext(userDetails -> {
                    assertThat(userDetails).isNotNull();
                    assertThat(userDetails.getAuthorities())
                            .extracting("authority")
                            .containsExactlyInAnyOrder("system:user:read", "system:user:write");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("findByUsername() should handle user with no roles")
    void findByUsername_WithNoRoles_ReturnsUserWithNoPermissions() {
        when(sysUserQueryService.queryUserByUsername("testuser"))
                .thenReturn(Mono.just(testSysUserView));
        when(sysUserRoleQueryService.queryLinkUserAndRole(1L))
                .thenReturn(Mono.just(List.of()));

        StepVerifier.create(authReactiveUserDetailsService.findByUsername("testuser"))
                .assertNext(userDetails -> {
                    assertThat(userDetails).isNotNull();
                    assertThat(userDetails.getUsername()).isEqualTo("testuser");
                    assertThat(userDetails.getAuthorities()).isEmpty();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("findByUsername() should filter out empty permissions")
    void findByUsername_WithEmptyPermission_FiltersOutPermission() {
        SysMenuView menuWithEmptyPermission = new SysMenuView();
        menuWithEmptyPermission.setId(30L);
        menuWithEmptyPermission.setPermission(null);
        menuWithEmptyPermission.setDeleteStatus(false);

        SysRoleMenuView roleMenuViewWithNull = new SysRoleMenuView();
        roleMenuViewWithNull.setRoleId(100L);
        roleMenuViewWithNull.setMenuId(30L);
        roleMenuViewWithNull.setDeleteStatus(false);

        when(sysUserQueryService.queryUserByUsername("testuser"))
                .thenReturn(Mono.just(testSysUserView));
        when(sysUserRoleQueryService.queryLinkUserAndRole(1L))
                .thenReturn(Mono.just(List.of(testUserRoleView)));
        when(sysRoleQueryService.getById(100L))
                .thenReturn(Mono.just(testRoleView));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(100L))
                .thenReturn(Mono.just(List.of(testRoleMenuView, roleMenuViewWithNull)));
        when(sysMenuQueryService.queryByMenuId(10L))
                .thenReturn(Mono.just(testMenuView));
        when(sysMenuQueryService.queryByMenuId(30L))
                .thenReturn(Mono.just(menuWithEmptyPermission));

        StepVerifier.create(authReactiveUserDetailsService.findByUsername("testuser"))
                .assertNext(userDetails -> {
                    assertThat(userDetails).isNotNull();
                    assertThat(userDetails.getAuthorities())
                            .extracting("authority")
                            .containsExactly("system:user:read");
                })
                .verifyComplete();
    }
}
