package com.springddd.application.service.auth;

import com.springddd.application.service.auth.dto.LoginUserQuery;
import com.springddd.application.service.auth.dto.LoginUserView;
import com.springddd.application.service.auth.dto.UserInfoView;
import com.springddd.application.service.auth.jwt.JwtSecret;
import com.springddd.application.service.auth.jwt.JwtTemplate;
import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.CacheProcessor;
import lombok.RequiredArgsConstructor;
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

    private final com.springddd.application.service.auth.handler.AuthHandler authHandler;

    private final JwtTemplate jwtTemplate;

    private final CacheProcessor cacheProcessor;

    private final JwtSecret jwtSecret;

    public Mono<LoginUserView> getToken(LoginUserQuery query) {
        return authHandler.handle(query)
                .flatMap(user -> {
                    SecurityUtils.setAuthUserContext(user);

                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", user.getUserId().value());

                    String token = jwtTemplate.generateToken(map);

                    Mono<Boolean> cacheOp = Mono.empty();

                    if (!CollectionUtils.isEmpty(user.getPermissions())) {
                        cacheOp = cacheProcessor.deleteCache(CacheKeys.USER_TOKEN.buildKey(user.getUserId().value()))
                                .then(
                                        cacheProcessor.setCache(
                                                CacheKeys.USER_TOKEN.buildKey(user.getUserId().value()),
                                                token,
                                                Duration.ofDays(jwtSecret.getTtl())
                                        )
                                ).then(
                                        cacheProcessor.setCache(
                                                CacheKeys.USER_DETAIL.buildKey(user.getUserId().value()),
                                                user,
                                                Duration.ofDays(jwtSecret.getTtl())
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
        view.setRoles(SecurityUtils.getRoles().stream().toList());
        return Mono.just(view);
    }

    public Mono<List<String>> getUserPermissions() {
        return Mono.just(
                SecurityUtils.getPermissions()
                        .stream()
                        .toList()
        );
    }

    public Mono<Void> clearCache() {
        return cacheProcessor.deleteCache(CacheKeys.USER_ALL.buildKey(SecurityUtils.getUserId()));
    }

}






















