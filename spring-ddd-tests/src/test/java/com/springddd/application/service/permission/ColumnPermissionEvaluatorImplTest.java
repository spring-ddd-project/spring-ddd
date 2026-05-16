package com.springddd.application.service.permission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.permission.MaskStrategy;
import com.springddd.domain.user.UserId;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.*;

class ColumnPermissionEvaluatorImplTest {

    private ColumnPermissionEvaluatorImpl evaluator;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        evaluator = new ColumnPermissionEvaluatorImpl(objectMapper);
    }

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
    @DisplayName("当用户没有任何列权限限制时，对象应原样返回")
    void filter_whenNoRestrictions_shouldReturnOriginal() {
        TestUser user = new TestUser("zhangsan", "13800138000", "zhangsan@example.com");

        StepVerifier.create(
                        evaluator.filter(user, "sys_user", MaskStrategy.NULL)
                                .contextWrite(withAuthUser(Collections.emptyMap()))
                )
                .assertNext(result -> {
                    assertThat(result.getUsername()).isEqualTo("zhangsan");
                    assertThat(result.getPhone()).isEqualTo("13800138000");
                    assertThat(result.getEmail()).isEqualTo("zhangsan@example.com");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("当用户有列权限限制时，不可见列应被置为 null")
    void filter_withNullStrategy_shouldMaskInvisibleColumns() {
        TestUser user = new TestUser("zhangsan", "13800138000", "zhangsan@example.com");
        Map<String, Set<String>> permissions = Map.of("sys_user", Set.of("username"));

        StepVerifier.create(
                        evaluator.filter(user, "sys_user", MaskStrategy.NULL)
                                .contextWrite(withAuthUser(permissions))
                )
                .assertNext(result -> {
                    assertThat(result.getUsername()).isEqualTo("zhangsan");
                    assertThat(result.getPhone()).isNull();
                    assertThat(result.getEmail()).isNull();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("当用户有列权限限制时，不可见列应被替换为 ***")
    void filter_withMaskStrategy_shouldReplaceInvisibleColumns() {
        TestUser user = new TestUser("zhangsan", "13800138000", "zhangsan@example.com");
        Map<String, Set<String>> permissions = Map.of("sys_user", Set.of("username", "email"));

        StepVerifier.create(
                        evaluator.filter(user, "sys_user", MaskStrategy.MASK)
                                .contextWrite(withAuthUser(permissions))
                )
                .assertNext(result -> {
                    assertThat(result.getUsername()).isEqualTo("zhangsan");
                    assertThat(result.getPhone()).isEqualTo("***");
                    assertThat(result.getEmail()).isEqualTo("zhangsan@example.com");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("当实体没有配置权限时，应返回原始对象")
    void filter_whenEntityNotConfigured_shouldReturnOriginal() {
        TestUser user = new TestUser("zhangsan", "13800138000", "zhangsan@example.com");
        Map<String, Set<String>> permissions = Map.of("sys_role", Set.of("roleName"));

        StepVerifier.create(
                        evaluator.filter(user, "sys_user", MaskStrategy.NULL)
                                .contextWrite(withAuthUser(permissions))
                )
                .assertNext(result -> {
                    assertThat(result.getUsername()).isEqualTo("zhangsan");
                    assertThat(result.getPhone()).isEqualTo("13800138000");
                    assertThat(result.getEmail()).isEqualTo("zhangsan@example.com");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("过滤 null 对象应返回 empty Mono")
    void filter_whenNull_shouldReturnEmpty() {
        StepVerifier.create(
                        evaluator.filter(null, "sys_user", MaskStrategy.NULL)
                                .contextWrite(withAuthUser(Collections.emptyMap()))
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("getVisibleColumns 应返回当前用户对该实体的可见列")
    void getVisibleColumns_shouldReturnColumns() {
        Map<String, Set<String>> permissions = Map.of("sys_user", Set.of("username", "phone"));

        StepVerifier.create(
                        evaluator.getVisibleColumns("sys_user")
                                .contextWrite(withAuthUser(permissions))
                )
                .assertNext(cols -> {
                    assertThat(cols).containsExactlyInAnyOrder("username", "phone");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("getVisibleColumns 对未配置实体应返回空集合")
    void getVisibleColumns_whenNotConfigured_shouldReturnEmpty() {
        StepVerifier.create(
                        evaluator.getVisibleColumns("sys_unknown")
                                .contextWrite(withAuthUser(Collections.emptyMap()))
                )
                .assertNext(cols -> assertThat(cols).isEmpty())
                .verifyComplete();
    }

    static class TestUser {
        private String username;
        private String phone;
        private String email;

        TestUser() {}

        TestUser(String username, String phone, String email) {
            this.username = username;
            this.phone = phone;
            this.email = email;
        }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}
