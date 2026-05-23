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
import com.springddd.domain.role.ColumnRule;
import com.springddd.domain.role.DataPermission;
import com.springddd.domain.role.RowScope;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Method;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
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

    @InjectMocks
    private AuthReactiveUserDetailsService service;

    // ---------- Reflection helpers ----------

    @SuppressWarnings("unchecked")
    private Map<String, Set<String>> invokeMergeColumnPermissions(List<SysRoleView> roleViews) throws Exception {
        Method method = AuthReactiveUserDetailsService.class.getDeclaredMethod("mergeColumnPermissions", List.class);
        method.setAccessible(true);
        return (Map<String, Set<String>>) method.invoke(service, roleViews);
    }

    @SuppressWarnings("unchecked")
    private DataPermission invokeMergeDataPermission(List<SysRoleView> roleViews) throws Exception {
        Method method = AuthReactiveUserDetailsService.class.getDeclaredMethod("mergeDataPermission", List.class);
        method.setAccessible(true);
        return (DataPermission) method.invoke(service, roleViews);
    }

    // ---------- findByUsername tests ----------

    @Test
    @DisplayName("findByUsername 应返回包含权限的 AuthUser")
    void findByUsername_shouldReturnAuthUserWithPermissions() {
        SysUserView userView = new SysUserView();
        userView.setId(1L);
        userView.setUsername("admin");
        userView.setPassword("pass");
        userView.setDeptId(1L);

        SysUserRoleView userRole = new SysUserRoleView();
        userRole.setRoleId(1L);

        SysRoleView roleView = new SysRoleView();
        roleView.setId(1L);
        roleView.setRoleCode("admin");
        roleView.setDataPermission(new DataPermission(null, List.of(), 1, List.of()));

        SysRoleMenuView roleMenu = new SysRoleMenuView();
        roleMenu.setMenuId(1L);

        SysMenuView menu = new SysMenuView();
        menu.setId(1L);
        menu.setPermission("sys:user:list");

        when(sysUserQueryService.queryUserByUsername("admin")).thenReturn(Mono.just(userView));
        when(sysUserRoleQueryService.queryLinkUserAndRole(1L)).thenReturn(Mono.just(List.of(userRole)));
        when(sysRoleQueryService.getById(1L)).thenReturn(Mono.just(roleView));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(1L)).thenReturn(Mono.just(List.of(roleMenu)));
        when(sysMenuQueryService.queryByMenuId(1L)).thenReturn(Mono.just(menu));

        StepVerifier.create(service.findByUsername("admin"))
                .assertNext(userDetails -> {
                    assertThat(userDetails.getUsername()).isEqualTo("admin");
                    assertThat(userDetails.getAuthorities()).hasSize(1);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("findByUsername 完整流程：多角色、多菜单、列权限与数据权限合并")
    void findByUsername_fullFlow_shouldMergePermissionsAndDataPermission() {
        SysUserView userView = new SysUserView();
        userView.setId(1L);
        userView.setUsername("admin");
        userView.setPassword("pass");
        userView.setLockStatus(false);
        userView.setDeptId(1L);

        SysUserRoleView userRole1 = new SysUserRoleView();
        userRole1.setRoleId(1L);
        SysUserRoleView userRole2 = new SysUserRoleView();
        userRole2.setRoleId(2L);

        ColumnRule colRule1 = new ColumnRule("sys_user", "用户", List.of("username", "email"), "dept", List.of(1L));
        ColumnRule colRule2 = new ColumnRule("sys_user", "用户", List.of("phone"), "dept", List.of(2L));
        ColumnRule colRule3 = new ColumnRule("sys_role", "角色", List.of("role_name"), "dept", List.of(3L));

        RowScope rowScope1 = new RowScope(List.of(1L, 2L), List.of(10L), List.of(100L), true);
        DataPermission dp1 = new DataPermission(rowScope1, List.of(colRule1, colRule3), 5, List.of(1L));

        RowScope rowScope2 = new RowScope(List.of(2L, 3L), List.of(20L), List.of(200L), false);
        DataPermission dp2 = new DataPermission(rowScope2, List.of(colRule2), 3, List.of(2L));

        SysRoleView roleView1 = new SysRoleView();
        roleView1.setId(1L);
        roleView1.setRoleCode("admin");
        roleView1.setDataPermission(dp1);

        SysRoleView roleView2 = new SysRoleView();
        roleView2.setId(2L);
        roleView2.setRoleCode("user");
        roleView2.setDataPermission(dp2);

        SysRoleMenuView roleMenu1 = new SysRoleMenuView();
        roleMenu1.setMenuId(1L);
        SysRoleMenuView roleMenu2 = new SysRoleMenuView();
        roleMenu2.setMenuId(2L);
        SysRoleMenuView roleMenu3 = new SysRoleMenuView();
        roleMenu3.setMenuId(3L);
        SysRoleMenuView roleMenu4 = new SysRoleMenuView();
        roleMenu4.setMenuId(4L);

        SysMenuView menu1 = new SysMenuView();
        menu1.setId(1L);
        menu1.setPermission("sys:user:list");
        SysMenuView menu2 = new SysMenuView();
        menu2.setId(2L);
        menu2.setPermission("sys:role:list");
        SysMenuView menu3 = new SysMenuView();
        menu3.setId(3L);
        menu3.setPermission("");
        SysMenuView menu4 = new SysMenuView();
        menu4.setId(4L);
        menu4.setPermission(null);

        when(sysUserQueryService.queryUserByUsername("admin")).thenReturn(Mono.just(userView));
        when(sysUserRoleQueryService.queryLinkUserAndRole(1L)).thenReturn(Mono.just(List.of(userRole1, userRole2)));
        when(sysRoleQueryService.getById(1L)).thenReturn(Mono.just(roleView1));
        when(sysRoleQueryService.getById(2L)).thenReturn(Mono.just(roleView2));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(1L)).thenReturn(Mono.just(List.of(roleMenu1, roleMenu2)));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(2L)).thenReturn(Mono.just(List.of(roleMenu3, roleMenu4)));
        when(sysMenuQueryService.queryByMenuId(1L)).thenReturn(Mono.just(menu1));
        when(sysMenuQueryService.queryByMenuId(2L)).thenReturn(Mono.just(menu2));
        when(sysMenuQueryService.queryByMenuId(3L)).thenReturn(Mono.just(menu3));
        when(sysMenuQueryService.queryByMenuId(4L)).thenReturn(Mono.just(menu4));

        StepVerifier.create(service.findByUsername("admin"))
                .assertNext(userDetails -> {
                    AuthUser user = (AuthUser) userDetails;
                    assertThat(user.getUsername()).isEqualTo("admin");
                    assertThat(user.getRoles()).containsExactly("admin", "user");
                    // Permissions should filter out empty/null
                    assertThat(user.getPermissions()).containsExactly("sys:user:list", "sys:role:list");
                    assertThat(user.getMenuIds()).containsExactly(1L, 2L, 3L, 4L);

                    // Column permissions merged
                    assertThat(user.getColumnPermissions()).containsKeys("sys_user", "sys_role");
                    assertThat(user.getColumnPermissions().get("sys_user")).containsExactlyInAnyOrder("username", "email", "phone");
                    assertThat(user.getColumnPermissions().get("sys_role")).containsExactly("role_name");

                    // Column rules merged (all rules concatenated)
                    assertThat(user.getColumnRules()).hasSize(3);

                    // Data permission merged
                    DataPermission dp = user.getDataPermission();
                    assertThat(dp).isNotNull();
                    assertThat(dp.dataScope()).isEqualTo(5); // strictest
                    assertThat(dp.deptIds()).containsExactlyInAnyOrder(1L, 2L, 3L);
                    assertThat(dp.rowScope().deptIds()).containsExactlyInAnyOrder(1L, 2L, 3L);
                    assertThat(dp.rowScope().postIds()).containsExactlyInAnyOrder(10L, 20L);
                    assertThat(dp.rowScope().userIds()).containsExactlyInAnyOrder(100L, 200L);
                    assertThat(dp.rowScope().self()).isTrue();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("findByUsername 当菜单权限为空或null时应过滤掉")
    void findByUsername_shouldFilterEmptyAndNullPermissions() {
        SysUserView userView = new SysUserView();
        userView.setId(2L);
        userView.setUsername("test");
        userView.setPassword("pass");
        userView.setDeptId(2L);

        SysUserRoleView userRole = new SysUserRoleView();
        userRole.setRoleId(3L);

        SysRoleView roleView = new SysRoleView();
        roleView.setId(3L);
        roleView.setRoleCode("test");
        roleView.setDataPermission(null);

        SysRoleMenuView roleMenu1 = new SysRoleMenuView();
        roleMenu1.setMenuId(5L);
        SysRoleMenuView roleMenu2 = new SysRoleMenuView();
        roleMenu2.setMenuId(6L);

        SysMenuView menu1 = new SysMenuView();
        menu1.setId(5L);
        menu1.setPermission("valid:perm");
        SysMenuView menu2 = new SysMenuView();
        menu2.setId(6L);
        menu2.setPermission(null);

        when(sysUserQueryService.queryUserByUsername("test")).thenReturn(Mono.just(userView));
        when(sysUserRoleQueryService.queryLinkUserAndRole(2L)).thenReturn(Mono.just(List.of(userRole)));
        when(sysRoleQueryService.getById(3L)).thenReturn(Mono.just(roleView));
        when(sysRoleMenuQueryService.queryLinkRoleAndMenus(3L)).thenReturn(Mono.just(List.of(roleMenu1, roleMenu2)));
        when(sysMenuQueryService.queryByMenuId(5L)).thenReturn(Mono.just(menu1));
        when(sysMenuQueryService.queryByMenuId(6L)).thenReturn(Mono.just(menu2));

        StepVerifier.create(service.findByUsername("test"))
                .assertNext(userDetails -> {
                    AuthUser user = (AuthUser) userDetails;
                    assertThat(user.getPermissions()).containsExactly("valid:perm");
                    assertThat(user.getColumnPermissions()).isEmpty();
                    assertThat(user.getDataPermission()).isNotNull();
                    assertThat(user.getDataPermission().dataScope()).isEqualTo(1);
                })
                .verifyComplete();
    }

    // ---------- mergeColumnPermissions tests ----------

    @Test
    @DisplayName("mergeColumnPermissions 当 roleViews 为 null 时应返回空 map")
    void mergeColumnPermissions_nullRoleViews_shouldReturnEmptyMap() throws Exception {
        Map<String, Set<String>> result = invokeMergeColumnPermissions(null);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("mergeColumnPermissions 当 roleViews 为空时应返回空 map")
    void mergeColumnPermissions_emptyRoleViews_shouldReturnEmptyMap() throws Exception {
        Map<String, Set<String>> result = invokeMergeColumnPermissions(List.of());
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("mergeColumnPermissions 应跳过 null dataPermission 和 null columnRules")
    void mergeColumnPermissions_nullDataPermission_shouldSkip() throws Exception {
        SysRoleView roleView = new SysRoleView();
        roleView.setDataPermission(null);

        Map<String, Set<String>> result = invokeMergeColumnPermissions(List.of(roleView));
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("mergeColumnPermissions 应跳过 null/empty entityCode 和 null/empty columns")
    void mergeColumnPermissions_invalidRules_shouldSkip() throws Exception {
        ColumnRule rule1 = new ColumnRule(null, "Name", List.of("col1"), "type", List.of());
        ColumnRule rule2 = new ColumnRule("entity1", "Name", null, "type", List.of());
        ColumnRule rule3 = new ColumnRule("entity1", "Name", List.of(), "type", List.of());
        ColumnRule rule4 = new ColumnRule("entity2", "Name", List.of("col2"), "type", List.of());

        DataPermission dp = new DataPermission(null, List.of(rule1, rule2, rule3, rule4), 1, List.of());
        SysRoleView roleView = new SysRoleView();
        roleView.setDataPermission(dp);

        Map<String, Set<String>> result = invokeMergeColumnPermissions(List.of(roleView));
        assertThat(result).containsOnlyKeys("entity2");
        assertThat(result.get("entity2")).containsExactly("col2");
    }

    @Test
    @DisplayName("mergeColumnPermissions 多个角色重叠 entityCode 时应合并列集合")
    void mergeColumnPermissions_overlappingKeys_shouldMergeSets() throws Exception {
        ColumnRule rule1 = new ColumnRule("user", "用户", List.of("name", "email"), "dept", List.of(1L));
        ColumnRule rule2 = new ColumnRule("user", "用户", List.of("phone", "email"), "dept", List.of(2L));
        ColumnRule rule3 = new ColumnRule("role", "角色", List.of("roleName"), "dept", List.of(3L));

        DataPermission dp1 = new DataPermission(null, List.of(rule1, rule3), 1, List.of());
        DataPermission dp2 = new DataPermission(null, List.of(rule2), 1, List.of());

        SysRoleView roleView1 = new SysRoleView();
        roleView1.setDataPermission(dp1);
        SysRoleView roleView2 = new SysRoleView();
        roleView2.setDataPermission(dp2);

        Map<String, Set<String>> result = invokeMergeColumnPermissions(List.of(roleView1, roleView2));
        assertThat(result).containsKeys("user", "role");
        assertThat(result.get("user")).containsExactlyInAnyOrder("name", "email", "phone");
        assertThat(result.get("role")).containsExactly("roleName");
    }

    // ---------- mergeDataPermission tests ----------

    @Test
    @DisplayName("mergeDataPermission 当 roleViews 为 null 时应返回 null")
    void mergeDataPermission_nullRoleViews_shouldReturnNull() throws Exception {
        DataPermission result = invokeMergeDataPermission(null);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("mergeDataPermission 当 roleViews 为空时应返回 null")
    void mergeDataPermission_emptyRoleViews_shouldReturnNull() throws Exception {
        DataPermission result = invokeMergeDataPermission(List.of());
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("mergeDataPermission 应跳过 null dataPermission 并返回默认空权限")
    void mergeDataPermission_nullDataPermission_shouldReturnDefaultEmpty() throws Exception {
        SysRoleView roleView = new SysRoleView();
        roleView.setDataPermission(null);

        DataPermission result = invokeMergeDataPermission(List.of(roleView));
        assertThat(result).isNotNull();
        assertThat(result.dataScope()).isEqualTo(1);
        assertThat(result.columnRules()).isEmpty();
        assertThat(result.deptIds()).isEmpty();
        assertThat(result.rowScope().self()).isFalse();
    }

    @Test
    @DisplayName("mergeDataPermission 应取最严格的数据权限范围")
    void mergeDataPermission_shouldTakeStrictestDataScope() throws Exception {
        DataPermission dp1 = new DataPermission(null, List.of(), 1, List.of());
        DataPermission dp2 = new DataPermission(null, List.of(), 5, List.of());
        DataPermission dp3 = new DataPermission(null, List.of(), 3, List.of());

        SysRoleView rv1 = new SysRoleView();
        rv1.setDataPermission(dp1);
        SysRoleView rv2 = new SysRoleView();
        rv2.setDataPermission(dp2);
        SysRoleView rv3 = new SysRoleView();
        rv3.setDataPermission(dp3);

        DataPermission result = invokeMergeDataPermission(List.of(rv1, rv2, rv3));
        assertThat(result.dataScope()).isEqualTo(5);
    }

    @Test
    @DisplayName("mergeDataPermission 应合并不同角色的 deptIds、postIds、userIds 和 self 标志")
    void mergeDataPermission_differentScopes_shouldMergeAll() throws Exception {
        RowScope rs1 = new RowScope(List.of(1L, 2L), List.of(10L), List.of(100L), true);
        DataPermission dp1 = new DataPermission(rs1, List.of(), 2, List.of(1L));

        RowScope rs2 = new RowScope(List.of(2L, 3L), List.of(20L), List.of(200L), false);
        DataPermission dp2 = new DataPermission(rs2, List.of(), 3, List.of(2L, 3L));

        SysRoleView rv1 = new SysRoleView();
        rv1.setDataPermission(dp1);
        SysRoleView rv2 = new SysRoleView();
        rv2.setDataPermission(dp2);

        DataPermission result = invokeMergeDataPermission(List.of(rv1, rv2));
        assertThat(result.dataScope()).isEqualTo(3);
        assertThat(result.deptIds()).containsExactlyInAnyOrder(1L, 2L, 3L);
        assertThat(result.rowScope().deptIds()).containsExactlyInAnyOrder(1L, 2L, 3L);
        assertThat(result.rowScope().postIds()).containsExactlyInAnyOrder(10L, 20L);
        assertThat(result.rowScope().userIds()).containsExactlyInAnyOrder(100L, 200L);
        assertThat(result.rowScope().self()).isTrue();
    }

    @Test
    @DisplayName("mergeDataPermission 应合并 columnRules 并处理 null RowScope")
    void mergeDataPermission_nullRowScope_shouldHandleCorrectly() throws Exception {
        ColumnRule rule1 = new ColumnRule("user", "用户", List.of("name"), "dept", List.of(1L));
        DataPermission dp1 = new DataPermission(null, List.of(rule1), 1, List.of(5L));

        SysRoleView rv1 = new SysRoleView();
        rv1.setDataPermission(dp1);

        DataPermission result = invokeMergeDataPermission(List.of(rv1));
        assertThat(result.dataScope()).isEqualTo(1);
        assertThat(result.columnRules()).hasSize(1);
        assertThat(result.deptIds()).containsExactly(5L);
        assertThat(result.rowScope().deptIds()).containsExactly(5L);
        assertThat(result.rowScope().postIds()).isEmpty();
        assertThat(result.rowScope().userIds()).isEmpty();
        assertThat(result.rowScope().self()).isFalse();
    }
}
