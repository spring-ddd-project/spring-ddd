package com.springddd.application.service.auth;

import com.springddd.application.service.auth.dto.LoginUserQuery;
import com.springddd.application.service.auth.dto.LoginUserView;
import com.springddd.application.service.auth.dto.UserInfoView;
import com.springddd.application.service.auth.jwt.JwtTemplate;
import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.menu.MenuPermission;
import com.springddd.domain.role.RoleCode;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final ReactiveAuthenticationManager authenticationManager;

    private final JwtTemplate jwtTemplate;

    private final ReactiveRedisCacheHelper reactiveRedisCacheHelper;

    public Mono<LoginUserView> getToken(LoginUserQuery query) {
        UsernamePasswordAuthenticationToken unauthenticated =
                UsernamePasswordAuthenticationToken.unauthenticated(query.getUsername(), query.getPassword());

        return authenticationManager.authenticate(unauthenticated)
                .flatMap(auth -> {
                    AuthUser user = (AuthUser) auth.getPrincipal();
                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", user.getUserId().value());

                    String token = jwtTemplate.generateToken(map);
                    String cacheKey = "user:token:" + user.getUserId().value();

                    Mono<Boolean> cacheOp = Mono.empty();

                    if (!CollectionUtils.isEmpty(user.getPermissions())) {
                        cacheOp = reactiveRedisCacheHelper.deleteCache(cacheKey)
                                .then(
                                        reactiveRedisCacheHelper.setCache(
                                                reactiveRedisCacheHelper.buildKey("user:token", user.getUserId().value().toString()),
                                                token,
                                                Duration.ofDays(100)
                                        )
                                );
                    }

                    return cacheOp.thenReturn(token);
                })
                .map(token -> {
                    LoginUserView view = new LoginUserView();
                    view.setAccessToken(token);
                    return view;
                });
    }

    public Mono<UserInfoView> getUserInfo() {
        UserInfoView view = new UserInfoView();
        view.setRealName(SecurityUtils.getUsername());
        view.setRoles(SecurityUtils.getRoles().stream().map(RoleCode::value).toList());
        return Mono.just(view);
    }

    public Mono<List<String>> getUserPermissions() {
        return Mono.just(
                SecurityUtils.getPermissions()
                        .stream()
                        .map(MenuPermission::value)
                        .toList()
        );
    }

    public Mono<Void> clearCache() {
        return reactiveRedisCacheHelper.deleteCache(reactiveRedisCacheHelper.buildKey("user:token:", SecurityUtils.getUserId().toString()));
    }

}

