package com.springddd.application.service.permission;

import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.permission.EntityColumnMetadata;
import com.springddd.domain.role.ColumnRule;
import com.springddd.domain.user.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ColumnPermissionMetadataServiceTest {

    @Mock
    private ColumnPermissionMetadataProvider metadataProvider;

    @InjectMocks
    private ColumnPermissionMetadataService service;

    private Context withAuthUser(Map<String, Set<String>> columnPermissions) {
        AuthUser user = new AuthUser();
        user.setUserId(new UserId(1L));
        user.setUsername("test");
        user.setColumnPermissions(columnPermissions);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        return ReactiveSecurityContextHolder.withAuthentication(auth);
    }

    @Test
    @DisplayName("获取当前用户的所有列权限配置")
    void getCurrentUserColumnPermissions_shouldReturnMap() {
        Map<String, Set<String>> permissions = new HashMap<>();
        permissions.put("sys_user", Set.of("username", "phone"));
        permissions.put("sys_role", Set.of("roleName"));

        StepVerifier.create(
                        service.getCurrentUserColumnPermissions()
                                .contextWrite(withAuthUser(permissions))
                )
                .assertNext(result -> {
                    assertThat(result).containsKeys("sys_user", "sys_role");
                    assertThat(result.get("sys_user")).containsExactlyInAnyOrder("username", "phone");
                    assertThat(result.get("sys_role")).containsExactly("roleName");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("当用户没有任何列权限时，返回空 Map")
    void getCurrentUserColumnPermissions_whenEmpty_shouldReturnEmpty() {
        StepVerifier.create(
                        service.getCurrentUserColumnPermissions()
                                .contextWrite(withAuthUser(Collections.emptyMap()))
                )
                .assertNext(result -> assertThat(result).isEmpty())
                .verifyComplete();
    }

    @Test
    @DisplayName("获取指定实体的可见列")
    void getVisibleColumns_shouldReturnList() {
        Map<String, Set<String>> permissions = Map.of("sys_user", Set.of("username", "phone", "email"));

        StepVerifier.create(
                        service.getVisibleColumns("sys_user")
                                .contextWrite(withAuthUser(permissions))
                )
                .assertNext(result -> {
                    assertThat(result).containsExactlyInAnyOrder("username", "phone", "email");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("对未配置实体返回空列表")
    void getVisibleColumns_whenNotConfigured_shouldReturnEmpty() {
        StepVerifier.create(
                        service.getVisibleColumns("sys_unknown")
                                .contextWrite(withAuthUser(Collections.emptyMap()))
                )
                .assertNext(result -> assertThat(result).isEmpty())
                .verifyComplete();
    }

    @Test
    @DisplayName("getAllEntityMetadata 应返回所有注册的实体元数据")
    void getAllEntityMetadata_shouldReturnAllMetadata() {
        List<EntityColumnMetadata> metadataList = List.of(
                new EntityColumnMetadata("sys_user", "用户", List.of()),
                new EntityColumnMetadata("sys_role", "角色", List.of())
        );
        when(metadataProvider.getAllMetadata()).thenReturn(metadataList);

        StepVerifier.create(service.getAllEntityMetadata())
                .assertNext(result -> {
                    assertThat(result).hasSize(2);
                    assertThat(result.get(0).entityCode()).isEqualTo("sys_user");
                    assertThat(result.get(1).entityCode()).isEqualTo("sys_role");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("getCurrentUserColumnPermissions 当用户有 columnRules 时应返回规则映射")
    void getCurrentUserColumnPermissions_whenColumnRulesExist_shouldReturnRuleMap() {
        ColumnRule rule1 = new ColumnRule("sys_user", "用户", List.of("username", "phone"), "role", List.of(1L));
        ColumnRule rule2 = new ColumnRule("sys_role", "角色", List.of("roleName"), "role", List.of(1L));
        ColumnRule rule3 = new ColumnRule(null, "无实体", List.of("col"), "role", List.of(1L));
        ColumnRule rule4 = new ColumnRule("sys_dept", "部门", null, "role", List.of(1L));

        AuthUser user = new AuthUser();
        user.setUserId(new UserId(1L));
        user.setUsername("test");
        user.setColumnRules(List.of(rule1, rule2, rule3, rule4));
        user.setColumnPermissions(Collections.emptyMap());

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        Context ctx = ReactiveSecurityContextHolder.withAuthentication(auth);

        StepVerifier.create(service.getCurrentUserColumnPermissions().contextWrite(ctx))
                .assertNext(result -> {
                    assertThat(result).containsKeys("sys_user", "sys_role");
                    assertThat(result.get("sys_user")).containsExactly("username", "phone");
                    assertThat(result.get("sys_role")).containsExactly("roleName");
                    assertThat(result).doesNotContainKey("sys_dept");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("getCurrentUserColumnPermissions 当 columnRules 为空且 columnPermissions 为 null 时应返回空 Map")
    void getCurrentUserColumnPermissions_whenRulesEmptyAndPermsNull_shouldReturnEmptyMap() {
        AuthUser user = new AuthUser();
        user.setUserId(new UserId(1L));
        user.setUsername("test");
        user.setColumnRules(Collections.emptyList());
        user.setColumnPermissions(null);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        Context ctx = ReactiveSecurityContextHolder.withAuthentication(auth);

        StepVerifier.create(service.getCurrentUserColumnPermissions().contextWrite(ctx))
                .assertNext(result -> assertThat(result).isEmpty())
                .verifyComplete();
    }
}
