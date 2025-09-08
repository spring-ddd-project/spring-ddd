package com.springddd.domain.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springddd.domain.user.UserId;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
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

    private String lockStatus;

    private List<String> roles;

    private List<String> permissions;

    private List<Long> menuIds;

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
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
