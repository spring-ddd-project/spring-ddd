package com.springddd.application.service.auth;

import com.springddd.application.service.auth.dto.LoginUserQuery;
import com.springddd.application.service.auth.dto.LoginUserView;
import com.springddd.application.service.auth.dto.UserInfoView;
import com.springddd.application.service.auth.jwt.JwtTemplate;
import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.menu.MenuPermission;
import com.springddd.domain.role.RoleCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final ReactiveAuthenticationManager authenticationManager;

    private final JwtTemplate jwtTemplate;

    public Mono<LoginUserView> getToken(LoginUserQuery query) {
        UsernamePasswordAuthenticationToken unauthenticated =
                UsernamePasswordAuthenticationToken.unauthenticated(query.getUsername(), query.getPassword());

        return authenticationManager.authenticate(unauthenticated)
                .map(auth -> {
                    AuthUser user = (AuthUser) auth.getPrincipal();
                    Map<String, Object> map = new HashMap<>();
                    map.put("username", user.getUsername());
                    String token = jwtTemplate.generateToken(map);
                    LoginUserView view = new LoginUserView();
                    view.setAccessToken(token);
                    return view;
                });
    }

    public Mono<UserInfoView> getUserInfo() {
        return SecurityContextUtils.getCurrentUser().map(user -> {
            UserInfoView view = new UserInfoView();
            view.setRealName(user.getUsername());
            view.setRoles(user.getRoles().stream().map(RoleCode::value).toList());
            return view;
        });
    }

    public Mono<List<String>> getUserPermissions() {
        return SecurityContextUtils.getCurrentUser().map(user -> user.getPermissions().stream().map(MenuPermission::value).toList());
    }
}

