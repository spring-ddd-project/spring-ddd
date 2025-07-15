package com.springddd.infrastructure.cache.keys;

import com.springddd.infrastructure.cache.util.CacheDefinition;

import java.time.Duration;

public class CacheKeys {

    // USER
    public static final CacheDefinition USER_ALL = CacheDefinition.of("user:%s:*");
    public static final CacheDefinition USER_TOKEN = CacheDefinition.of("user:%s:token");
    public static final CacheDefinition USER_DETAIL = CacheDefinition.of("user:%s:detail");

    // MENU
    public static final CacheDefinition MENU_WITH_PERMISSIONS = CacheDefinition.of("user:%s:menuWithPermissions", Duration.ofDays(7));
    public static final CacheDefinition MENU_WITHOUT_PERMISSIONS = CacheDefinition.of("user:%s:menuWithoutPermissions", Duration.ofDays(7));

    // GEN
    public static final CacheDefinition GEN_TEMPLATE = CacheDefinition.of("user:%s:template", Duration.ofMinutes(1));
}
