package com.springddd.application.service.auth.cache;

import com.springddd.domain.auth.AuthUser;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class UserDetailCacheService {

    private final ReactiveRedisCacheHelper cacheHelper;

    public Mono<Void> save(AuthUser user, int ttlDays) {
        String key = key(user.getUserId().value());
        return cacheHelper.setCache(key, user, Duration.ofDays(ttlDays))
                .then();
    }

    public Mono<AuthUser> get(Long userId) {
        return cacheHelper.getCache(key(userId), AuthUser.class);
    }

    public Mono<Void> delete(Long userId) {
        return cacheHelper.deleteCache(key(userId));
    }

    private String key(Long userId) {
        return "auth:user:detail:" + userId;
    }
}
