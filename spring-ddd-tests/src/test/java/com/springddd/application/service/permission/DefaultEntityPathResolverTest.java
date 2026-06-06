package com.springddd.application.service.permission;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultEntityPathResolverTest {

    private DefaultEntityPathResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new DefaultEntityPathResolver();
        resolver.init();
    }

    @Test
    @DisplayName("resolveEntityCode(/sys/user) 应返回 sys_user")
    void resolveEntityCode_forSysUserPath_shouldReturnSysUser() {
        Optional<String> result = resolver.resolveEntityCode("/sys/user");
        assertThat(result).hasValue("sys_user");
    }

    @Test
    @DisplayName("resolveEntityCode(/gen/aggregate) 应返回 gen_aggregate")
    void resolveEntityCode_forGenAggregatePath_shouldReturnGenAggregate() {
        Optional<String> result = resolver.resolveEntityCode("/gen/aggregate");
        assertThat(result).hasValue("gen_aggregate");
    }

    @Test
    @DisplayName("resolveEntityCode(/sys/dict/item) 应返回 sys_dict_item")
    void resolveEntityCode_forSysDictItemPath_shouldReturnSysDictItem() {
        Optional<String> result = resolver.resolveEntityCode("/sys/dict/item");
        assertThat(result).hasValue("sys_dict_item");
    }

    @Test
    @DisplayName("resolveEntityCode(空字符串) 应返回 empty")
    void resolveEntityCode_forEmptyString_shouldReturnEmpty() {
        Optional<String> result = resolver.resolveEntityCode("");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("resolveEntityCode(null) 应返回 empty")
    void resolveEntityCode_forNull_shouldReturnEmpty() {
        Optional<String> result = resolver.resolveEntityCode(null);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("resolveEntityCode(/unknown/path) 应通过约定推导返回 unknown_path")
    void resolveEntityCode_forUnknownPath_shouldDeriveByConvention() {
        Optional<String> result = resolver.resolveEntityCode("/unknown/path");
        assertThat(result).hasValue("unknown_path");
    }

    @Test
    @DisplayName("resolveEntityCode 当存在显式映射时应返回映射值")
    void resolveEntityCode_withExplicitMapping_shouldReturnMappedValue() throws Exception {
        java.lang.reflect.Field field = DefaultEntityPathResolver.class.getDeclaredField("explicitMappings");
        field.setAccessible(true);
        DefaultEntityPathResolver explicitResolver = new DefaultEntityPathResolver();
        field.set(explicitResolver, Map.of("/api/custom", "custom_entity"));
        explicitResolver.init();

        Optional<String> result = explicitResolver.resolveEntityCode("/api/custom");
        assertThat(result).hasValue("custom_entity");
    }

    @Test
    @DisplayName("resolveEntityCode 当显式映射为前缀匹配时应返回映射值")
    void resolveEntityCode_withExplicitMappingPrefix_shouldReturnMappedValue() throws Exception {
        java.lang.reflect.Field field = DefaultEntityPathResolver.class.getDeclaredField("explicitMappings");
        field.setAccessible(true);
        DefaultEntityPathResolver explicitResolver = new DefaultEntityPathResolver();
        field.set(explicitResolver, Map.of("/api/custom", "custom_entity"));
        explicitResolver.init();

        Optional<String> result = explicitResolver.resolveEntityCode("/api/custom/sub");
        assertThat(result).hasValue("custom_entity");
    }

    @Test
    @DisplayName("resolveEntityCode 当路径无前导斜杠时应通过约定推导")
    void resolveEntityCode_withoutLeadingSlash_shouldDeriveByConvention() {
        Optional<String> result = resolver.resolveEntityCode("sys/user");
        assertThat(result).hasValue("sys_user");
    }

    @Test
    @DisplayName("resolveEntityCode 当路径为单斜杠时应返回 empty")
    void resolveEntityCode_forSingleSlash_shouldReturnEmpty() {
        Optional<String> result = resolver.resolveEntityCode("/");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("deriveByConvention 当路径无前导斜杠时应正确推导")
    void deriveByConvention_withoutLeadingSlash_shouldReturnCorrectValue() throws Exception {
        Method method = DefaultEntityPathResolver.class.getDeclaredMethod("deriveByConvention", String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(resolver, "sys/user");
        assertThat(result).isEqualTo("sys_user");
    }

    @Test
    @DisplayName("deriveByConvention 当路径为单段时应返回该段")
    void deriveByConvention_singleSegment_shouldReturnSegment() throws Exception {
        Method method = DefaultEntityPathResolver.class.getDeclaredMethod("deriveByConvention", String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(resolver, "user");
        assertThat(result).isEqualTo("user");
    }

    @Test
    @DisplayName("deriveByConvention 当路径含短横线时应保留短横线")
    void deriveByConvention_kebabCase_shouldPreserve() throws Exception {
        Method method = DefaultEntityPathResolver.class.getDeclaredMethod("deriveByConvention", String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(resolver, "sys/user-profile");
        assertThat(result).isEqualTo("sys_user-profile");
    }

    @Test
    @DisplayName("deriveByConvention 当路径为空字符串时应返回空字符串")
    void deriveByConvention_emptyPath_shouldReturnEmptyString() throws Exception {
        Method method = DefaultEntityPathResolver.class.getDeclaredMethod("deriveByConvention", String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(resolver, "");
        assertThat(result).isEqualTo("");
    }

    @Test
    @DisplayName("init 当 explicitMappings 为 null 时不应抛出异常")
    void init_withNullExplicitMappings_shouldNotThrow() throws Exception {
        java.lang.reflect.Field field = DefaultEntityPathResolver.class.getDeclaredField("explicitMappings");
        field.setAccessible(true);
        DefaultEntityPathResolver nullMappingResolver = new DefaultEntityPathResolver();
        field.set(nullMappingResolver, null);
        nullMappingResolver.init();

        Optional<String> result = nullMappingResolver.resolveEntityCode("/sys/user");
        assertThat(result).hasValue("sys_user");
    }

    @Test
    @DisplayName("resolveEntityCode 当显式映射存在但不匹配时应通过约定推导")
    void resolveEntityCode_withExplicitMappingNoMatch_shouldDeriveByConvention() throws Exception {
        java.lang.reflect.Field field = DefaultEntityPathResolver.class.getDeclaredField("explicitMappings");
        field.setAccessible(true);
        DefaultEntityPathResolver explicitResolver = new DefaultEntityPathResolver();
        field.set(explicitResolver, Map.of("/api/other", "other_entity"));
        explicitResolver.init();

        Optional<String> result = explicitResolver.resolveEntityCode("/sys/user");
        assertThat(result).hasValue("sys_user");
    }

    @Test
    @DisplayName("deriveByConvention 当路径为双斜杠时应返回 null")
    void deriveByConvention_withDoubleSlash_shouldReturnNull() throws Exception {
        Method method = DefaultEntityPathResolver.class.getDeclaredMethod("deriveByConvention", String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(resolver, "//");
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("resolveEntityCode 当路径为双斜杠时应返回 empty")
    void resolveEntityCode_withDoubleSlash_shouldReturnEmpty() {
        Optional<String> result = resolver.resolveEntityCode("//");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("resolveEntityCode 当显式映射值为空字符串时应返回空字符串")
    void resolveEntityCode_withExplicitMappingEmptyValue_shouldReturnEmptyString() throws Exception {
        java.lang.reflect.Field field = DefaultEntityPathResolver.class.getDeclaredField("explicitMappings");
        field.setAccessible(true);
        DefaultEntityPathResolver explicitResolver = new DefaultEntityPathResolver();
        field.set(explicitResolver, Map.of("/api/empty", ""));
        explicitResolver.init();

        Optional<String> result = explicitResolver.resolveEntityCode("/api/empty");
        assertThat(result).hasValue("");
    }
}
