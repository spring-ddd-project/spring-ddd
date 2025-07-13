package com.springddd.domain.auth;

import com.springddd.domain.menu.MenuPermission;
import com.springddd.domain.role.RoleCode;

import java.util.List;

public class SecurityUtils {

    private static Long userId;

    private static String username;

    private static List<RoleCode> roles;

    private static List<MenuPermission> permissions;

    public static void setAuthUserContext(AuthUser user) {
        setUserId(user.getUserId().value());
        setUsername(user.getUsername());
        setRoles(user.getRoles());
        setPermissions(user.getPermissions());
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

    public static void setPermissions(List<MenuPermission> permissions) {
        SecurityUtils.permissions = permissions;
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

    public static List<MenuPermission> getPermissions() {
        return SecurityUtils.permissions;
    }

    public static Integer concurrency() {
        return Runtime.getRuntime().availableProcessors() * 2;
    }

}

