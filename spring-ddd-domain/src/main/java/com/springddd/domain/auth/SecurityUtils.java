package com.springddd.domain.auth;

import com.springddd.domain.role.RoleCode;

import java.util.List;

public class SecurityUtils {

    private static Long userId;

    private static String username;

    private static List<RoleCode> roles;

    private static List<String> permissions;

    private static List<Long> menuIds;

    public static void setAuthUserContext(AuthUser user) {
        setUserId(user.getUserId().value());
        setUsername(user.getUsername());
        setRoles(user.getRoles());
        setPermissions(user.getPermissions());
        setMenuIds(user.getMenuIds());
    }

    public static void setUserId(Long userId) {
        SecurityUtils.userId = userId;
    }

    public static void setUsername(String username) {
        SecurityUtils.username = username;
    }

    public static void setRoles(List<RoleCode> roles) {
        SecurityUtils.roles = roles;
    }

    public static void setPermissions(List<String> permissions) {
        SecurityUtils.permissions = permissions;
    }

    public static void setMenuIds(List<Long> menuIds) {
        SecurityUtils.menuIds = menuIds;
    }

    public static Long getUserId() {
        return SecurityUtils.userId;
    }

    public static String getUsername() {
        return SecurityUtils.username;
    }

    public static List<RoleCode> getRoles() {
        return SecurityUtils.roles;
    }

    public static List<String> getPermissions() {
        return SecurityUtils.permissions;
    }

    public static List<Long> getMenuIds() {
        return SecurityUtils.menuIds;
    }

    public static Integer concurrency() {
        return Runtime.getRuntime().availableProcessors() * 2;
    }

}

