package com.springddd.domain.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springddd.domain.role.ColumnRule;
import com.springddd.domain.role.DataPermission;
import com.springddd.domain.user.UserId;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class AuthUser implements Serializable, UserDetails {

    private UserId userId;

    private String username;

    private String password;

    private String phone;

    private String avatar;

    private String email;

    private String sex;

    private Boolean lockStatus;

    private Long deptId;

    private List<String> roles;

    private List<String> permissions;

    private List<Long> menuIds;

    /**
     * 列权限预计算映射（向后兼容）
     */
    private Map<String, Set<String>> columnPermissions = new java.util.HashMap<>();

    /**
     * 原始列权限规则列表（支持多维度匹配）
     */
    private List<ColumnRule> columnRules = new ArrayList<>();

    /**
     * 数据权限（行级+列级合并后）
     */
    private DataPermission dataPermission;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return CollectionUtils.isEmpty(permissions)
                ? List.of()
                : permissions.stream()
                .filter(StringUtils::hasText)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !Boolean.TRUE.equals(lockStatus);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return !Boolean.TRUE.equals(lockStatus);
    }
}
