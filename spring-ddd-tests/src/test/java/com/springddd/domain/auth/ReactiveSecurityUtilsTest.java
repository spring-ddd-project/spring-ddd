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

    @Test
    @DisplayName("getVisibleColumns userId为null时user维度不应匹配")
    void getVisibleColumns_withNullUserId_shouldNotMatchUserDimension() {
        List<ColumnRule> rules = List.of(
                new ColumnRule("sys_user", "用户", List.of("id"), "user", List.of(1L))
        );
        AuthUser user = createUserWithRules(null, 10L, rules);

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_user").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).isEmpty())
                .verifyComplete();
    }

    @Test
    @DisplayName("getVisibleColumns 当userId对象本身为null时应正常处理")
    void getVisibleColumns_withNullUserIdObject_shouldHandleNullUserId() {
        List<ColumnRule> rules = List.of(
                new ColumnRule("sys_user", "用户", List.of("id"), "user", List.of(1L))
        );
        AuthUser user = new AuthUser();
        user.setUserId(null);
        user.setUsername("test");
        user.setRoles(List.of("ADMIN"));
        user.setPermissions(List.of("sys:user:index"));
        user.setMenuIds(List.of(1L, 2L, 3L));
        user.setDeptId(10L);
        user.setColumnRules(rules);

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_user").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).isEmpty())
                .verifyComplete();
    }

    @Test
    @DisplayName("getVisibleColumns deptId为null时dept维度应正常处理")
    void getVisibleColumns_withNullDeptId_shouldHandleDeptDimension() {
        List<ColumnRule> rules = List.of(
                new ColumnRule("sys_user", "用户", List.of("id"), "dept", List.of(10L))
        );
        AuthUser user = createUserWithRules(1L, null, rules);

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_user").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).isEmpty())
                .verifyComplete();
    }

    @Test
    @DisplayName("getVisibleColumns dimensionIds为null时应正常处理")
    void getVisibleColumns_withNullDimensionIds_shouldHandleNullDimensionIds() {
        List<ColumnRule> rules = List.of(
                new ColumnRule("sys_user", "用户", List.of("id"), "user", null)
        );
        AuthUser user = createUserWithRules(1L, 10L, rules);

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_user").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).isEmpty())
                .verifyComplete();
    }

    @Test
    @DisplayName("getVisibleColumns columns为null时应正常处理")
    void getVisibleColumns_withNullColumns_shouldHandleNullColumns() {
        List<ColumnRule> rules = List.of(
                new ColumnRule("sys_user", "用户", null, "self", null)
        );
        AuthUser user = createUserWithRules(1L, 10L, rules);

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_user").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).isEmpty())
                .verifyComplete();
    }

    @Test
    @DisplayName("getVisibleColumns all维度dimensionType为null时应匹配")
    void getVisibleColumns_withNullDimensionType_shouldMatchAllFallback() {
        List<ColumnRule> rules = List.of(
                new ColumnRule("sys_user", "用户", List.of("id", "username"), null, null)
        );
        AuthUser user = createUserWithRules(1L, 10L, rules);

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_user").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).containsExactlyInAnyOrder("id", "username"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getVisibleColumns columnRules和columnPermissions都为null时应返回空集")
    void getVisibleColumns_whenBothRulesAndPermissionsNull_shouldReturnEmpty() {
        AuthUser user = new AuthUser();
        user.setUserId(new UserId(1L));
        user.setUsername("test");
        user.setColumnRules(null);
        user.setColumnPermissions(null);

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_user").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).isEmpty())
                .verifyComplete();
    }

    @Test
    @DisplayName("getVisibleColumns columnRules为空且columnPermissions为null时应返回空集")
    void getVisibleColumns_whenRulesEmptyAndPermissionsNull_shouldReturnEmpty() {
        AuthUser user = new AuthUser();
        user.setUserId(new UserId(1L));
        user.setUsername("test");
        user.setColumnRules(Collections.emptyList());
        user.setColumnPermissions(null);

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_user").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).isEmpty())
                .verifyComplete();
    }

    @Test
    @DisplayName("getVisibleColumns 使用 columnRules - 所有规则都是其他实体时应返回空集")
    void getVisibleColumns_withAllDifferentEntityCodes_shouldReturnEmpty() {
        List<ColumnRule> rules = List.of(
                new ColumnRule("sys_dept", "部门", List.of("id", "name"), "all", null)
        );
        AuthUser user = createUserWithRules(1L, 10L, rules);

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_user").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).isEmpty())
                .verifyComplete();
    }

    @Test
    @DisplayName("getVisibleColumns 使用 columnRules - 包含其他实体代码的规则应被过滤")
    void getVisibleColumns_withMixedEntityCodes_shouldFilterByEntityCode() {
        List<ColumnRule> rules = List.of(
                new ColumnRule("sys_dept", "部门", List.of("id", "name"), "all", null),
                new ColumnRule("sys_user", "用户", List.of("id", "username"), "all", null)
        );
        AuthUser user = createUserWithRules(1L, 10L, rules);

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_user").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).containsExactlyInAnyOrder("id", "username"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getVisibleColumns 使用 columnRules - dept规则存在但不匹配时应回退到self")
    void getVisibleColumns_withDeptRuleNoMatch_shouldFallbackToSelf() {
        List<ColumnRule> rules = List.of(
                new ColumnRule("sys_user", "用户", List.of("id", "email"), "dept", List.of(999L)),
                new ColumnRule("sys_user", "用户", List.of("id", "phone"), "self", null)
        );
        AuthUser user = createUserWithRules(1L, 10L, rules);

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_user").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).containsExactlyInAnyOrder("id", "phone"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getVisibleColumns 使用 columnRules - user维度columns为null时应返回空集")
    void getVisibleColumns_withUserColumnsNull_shouldReturnEmpty() {
        List<ColumnRule> rules = List.of(
                new ColumnRule("sys_user", "用户", null, "user", List.of(1L))
        );
        AuthUser user = createUserWithRules(1L, 10L, rules);

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_user").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).isEmpty())
                .verifyComplete();
    }

    @Test
    @DisplayName("getVisibleColumns 使用 columnRules - dept维度columns为null时应返回空集")
    void getVisibleColumns_withDeptColumnsNull_shouldReturnEmpty() {
        List<ColumnRule> rules = List.of(
                new ColumnRule("sys_user", "用户", null, "dept", List.of(10L))
        );
        AuthUser user = createUserWithRules(1L, 10L, rules);

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_user").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).isEmpty())
                .verifyComplete();
    }

    @Test
    @DisplayName("getVisibleColumns 使用 columnRules - all维度columns为null时应返回空集")
    void getVisibleColumns_withAllColumnsNull_shouldReturnEmpty() {
        List<ColumnRule> rules = List.of(
                new ColumnRule("sys_user", "用户", null, "all", null)
        );
        AuthUser user = createUserWithRules(1L, 10L, rules);

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_user").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).isEmpty())
                .verifyComplete();
    }

    @Test
    @DisplayName("构造函数应可被调用")
    void constructor_shouldBeCallable() {
        ReactiveSecurityUtils utils = new ReactiveSecurityUtils();
        assertThat(utils).isNotNull();
    }

    @Test
    @DisplayName("getVisibleColumns 使用 columnRules - dept维度dimensionIds为null时应正常处理")
    void getVisibleColumns_withNullDimensionIds_deptDimension_shouldHandleNull() {
        List<ColumnRule> rules = List.of(
                new ColumnRule("sys_user", "用户", List.of("id"), "dept", null)
        );
        AuthUser user = createUserWithRules(1L, 10L, rules);

        StepVerifier.create(ReactiveSecurityUtils.getVisibleColumns("sys_user").contextWrite(withAuthUser(user)))
                .assertNext(cols -> assertThat(cols).isEmpty())
                .verifyComplete();
    }
}
