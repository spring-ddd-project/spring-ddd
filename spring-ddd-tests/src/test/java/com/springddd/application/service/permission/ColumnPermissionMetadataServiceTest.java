package com.springddd.application.service.permission;

import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.user.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ColumnPermissionMetadataServiceTest {

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
}
