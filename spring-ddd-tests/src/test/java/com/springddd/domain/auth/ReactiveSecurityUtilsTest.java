package com.springddd.domain.auth;

import com.springddd.domain.role.ColumnRule;
import com.springddd.domain.user.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class ReactiveSecurityUtilsTest {

    private Context withAuthUser(AuthUser user) {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        return ReactiveSecurityContextHolder.withAuthentication(auth);
    }

    private AuthUser createUser(Long userId, String username, Map<String, Set<String>> columnPermissions) {
        AuthUser user = new AuthUser();
        user.setUserId(new UserId(userId));
        user.setUsername(username);
        user.setRoles(List.of("ADMIN"));
        user.setPermissions(List.of("sys:user:index"));
        user.setMenuIds(List.of(1L, 2L, 3L));
        user.setColumnPermissions(columnPermissions);
        return user;
    }

    private AuthUser createUserWithRules(Long userId, Long deptId, List<ColumnRule> columnRules) {
        AuthUser user = new AuthUser();
        user.setUserId(new UserId(userId));
        user.setUsername("test");
        user.setRoles(List.of("ADMIN"));
        user.setPermissions(List.of("sys:user:index"));
        user.setMenuIds(List.of(1L, 2L, 3L));
        user.setDeptId(deptId);
        user.setColumnRules(columnRules);
        return user;
    }

    @Test
    @DisplayName("getCurrentUser 应返回当前认证用户")
    void getCurrentUser_shouldReturnAuthUser() {
        AuthUser expected = createUser(1L, "admin", Collections.emptyMap());

        StepVerifier.create(ReactiveSecurityUtils.getCurrentUser().contextWrite(withAuthUser(expected)))
                .assertNext(user -> {
                    assertThat(user.getUserId().value()).isEqualTo(1L);
                    assertThat(user.getUsername()).isEqualTo("admin");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("getCurrentUserId 应返回当前用户ID")
    void getCurrentUserId_shouldReturnUserId() {
        AuthUser user = createUser(42L, "test", Collections.emptyMap());

        StepVerifier.create(ReactiveSecurityUtils.getCurrentUserId().contextWrite(withAuthUser(user)))
                .assertNext(id -> assertThat(id).isEqualTo(42L))
                .verifyComplete();
    }

    @Test
    @DisplayName("getCurrentUserRoles 应返回用户角色")
    void getCurrentUserRoles_shouldReturnRoles() {
        AuthUser user = createUser(1L, "test", Collections.emptyMap());
        user.setRoles(List.of("ADMIN", "USER"));

        StepVerifier.create(ReactiveSecurityUtils.getCurrentUserRoles().contextWrite(withAuthUser(user)))
                .assertNext(roles -> assertThat(roles).containsExactly("ADMIN", "USER"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getCurrentUserPermissions 应返回用户权限")
    void getCurrentUserPermissions_shouldReturnPermissions() {
        AuthUser user = createUser(1L, "test", Collections.emptyMap());
        user.setPermissions(List.of("sys:user:index", "sys:user:create"));

        StepVerifier.create(ReactiveSecurityUtils.getCurrentUserPermissions().contextWrite(withAuthUser(user)))
                .assertNext(perms -> assertThat(perms).containsExactly("sys:user:index", "sys:user:create"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getCurrentUserMenuIds 应返回用户菜单ID")
    void getCurrentUserMenuIds_shouldReturnMenuIds() {
        AuthUser user = createUser(1L, "test", Collections.emptyMap());
        user.setMenuIds(List.of(10L, 20L, 30L));

        StepVerifier.create(ReactiveSecurityUtils.getCurrentUserMenuIds().contextWrite(withAuthUser(user)))
                .assertNext(menuIds -> assertThat(menuIds).containsExactly(10L, 20L, 30L))
                .verifyComplete();
    }

    @Test
    @DisplayName("getVisibleColumns 对配置实体返回可见列（旧columnPermissions兼容）")
    void getVisibleColumns_shouldReturnColumns() {
        AuthUser user = createUser(1L, "test", Map.of("sys_user", Set.of("username", "phone")));

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_user").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).containsExactlyInAnyOrder("username", "phone"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getVisibleColumns 对未配置实体返回空集合")
    void getVisibleColumns_whenNotConfigured_shouldReturnEmpty() {
        AuthUser user = createUser(1L, "test", Collections.emptyMap());

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_unknown").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).isEmpty())
                .verifyComplete();
    }

    // ==================== columnRules 多维度匹配测试 ====================

    @Test
    @DisplayName("getVisibleColumns 使用 columnRules - user 维度优先匹配")
    void getVisibleColumns_withColumnRules_userDimension_shouldMatch() {
        List<ColumnRule> rules = List.of(
            new ColumnRule("sys_user", "用户", List.of("id", "username"), "user", List.of(1L)),
            new ColumnRule("sys_user", "用户", List.of("id", "phone"), "user", List.of(2L))
        );
        AuthUser user = createUserWithRules(1L, 10L, rules);

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_user").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).containsExactlyInAnyOrder("id", "username"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getVisibleColumns 使用 columnRules - dept 维度匹配")
    void getVisibleColumns_withColumnRules_deptDimension_shouldMatch() {
        List<ColumnRule> rules = List.of(
            new ColumnRule("sys_user", "用户", List.of("id", "email"), "dept", List.of(10L)),
            new ColumnRule("sys_user", "用户", List.of("id", "phone"), "dept", List.of(20L))
        );
        AuthUser user = createUserWithRules(99L, 10L, rules);

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_user").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).containsExactlyInAnyOrder("id", "email"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getVisibleColumns 使用 columnRules - self 维度匹配")
    void getVisibleColumns_withColumnRules_selfDimension_shouldMatch() {
        List<ColumnRule> rules = List.of(
            new ColumnRule("sys_user", "用户", List.of("id", "avatar"), "self", null)
        );
        AuthUser user = createUserWithRules(99L, 10L, rules);

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_user").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).containsExactlyInAnyOrder("id", "avatar"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getVisibleColumns 使用 columnRules - all 维度匹配")
    void getVisibleColumns_withColumnRules_allDimension_shouldMatch() {
        List<ColumnRule> rules = List.of(
            new ColumnRule("sys_user", "用户", List.of("id", "username", "phone"), "all", null)
        );
        AuthUser user = createUserWithRules(99L, 10L, rules);

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_user").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).containsExactlyInAnyOrder("id", "username", "phone"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getVisibleColumns 使用 columnRules - 无匹配规则返回空集")
    void getVisibleColumns_withColumnRules_noMatch_shouldReturnEmpty() {
        List<ColumnRule> rules = List.of(
            new ColumnRule("sys_user", "用户", List.of("id"), "user", List.of(999L))
        );
        AuthUser user = createUserWithRules(1L, 10L, rules);

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_user").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).isEmpty())
                .verifyComplete();
    }

    @Test
    @DisplayName("getVisibleColumns columnRules为空时回退到columnPermissions")
    void getVisibleColumns_whenColumnRulesEmpty_shouldFallbackToColumnPermissions() {
        AuthUser user = createUser(1L, "test", Map.of("sys_user", Set.of("username", "phone")));
        user.setColumnRules(Collections.emptyList());

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_user").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).containsExactlyInAnyOrder("username", "phone"))
                .verifyComplete();
    }
}
