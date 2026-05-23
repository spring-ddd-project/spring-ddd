package com.springddd.application.service.gen.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class VueFilePathStrategyTest {

    private final VueFilePathStrategy strategy = new VueFilePathStrategy();
    private final String projectName = "spring-ddd";
    private final Map<String, Object> context = Map.of(
            "moduleName", "user",
            "requestName", "sys-user"
    );

    @Test
    @DisplayName("supports should return true for known templates")
    void supports_knownTemplates_shouldReturnTrue() {
        assertThat(strategy.supports("index.vue")).isTrue();
        assertThat(strategy.supports("recycle.vue")).isTrue();
        assertThat(strategy.supports("form.vue")).isTrue();
        assertThat(strategy.supports("api.ts")).isTrue();
        assertThat(strategy.supports("i18n.en.json")).isTrue();
        assertThat(strategy.supports("i18n.locale.json")).isTrue();
    }

    @Test
    @DisplayName("supports should return false for unknown templates")
    void supports_unknownTemplates_shouldReturnFalse() {
        assertThat(strategy.supports("unknown")).isFalse();
        assertThat(strategy.supports("controller")).isFalse();
        assertThat(strategy.supports("entity")).isFalse();
    }

    @Test
    @DisplayName("generatePath should return correct path for index.vue template")
    void generatePath_indexVue_shouldReturnCorrectPath() {
        String path = strategy.generatePath("index.vue", context, projectName);
        assertThat(path).isEqualTo("apps/web-ele/src/views/user/sys-user/index.vue");
    }

    @Test
    @DisplayName("generatePath should return correct path for recycle.vue template")
    void generatePath_recycleVue_shouldReturnCorrectPath() {
        String path = strategy.generatePath("recycle.vue", context, projectName);
        assertThat(path).isEqualTo("apps/web-ele/src/views/user/sys-user/recycle.vue");
    }

    @Test
    @DisplayName("generatePath should return correct path for form.vue template")
    void generatePath_formVue_shouldReturnCorrectPath() {
        String path = strategy.generatePath("form.vue", context, projectName);
        assertThat(path).isEqualTo("apps/web-ele/src/views/user/sys-user/form.vue");
    }

    @Test
    @DisplayName("generatePath should return correct path for api.ts template")
    void generatePath_apiTs_shouldReturnCorrectPath() {
        String path = strategy.generatePath("api.ts", context, projectName);
        assertThat(path).isEqualTo("apps/web-ele/src/api/user/sys-user/index.ts");
    }

    @Test
    @DisplayName("generatePath should return correct path for i18n.en.json template")
    void generatePath_i18nEnJson_shouldReturnCorrectPath() {
        String path = strategy.generatePath("i18n.en.json", context, projectName);
        assertThat(path).isEqualTo("apps/web-ele/src/locales/langs/en-US/sys-user.json");
    }

    @Test
    @DisplayName("generatePath should return correct path for i18n.locale.json template")
    void generatePath_i18nLocaleJson_shouldReturnCorrectPath() {
        String path = strategy.generatePath("i18n.locale.json", context, projectName);
        assertThat(path).isEqualTo("apps/web-ele/src/locales/langs/zh-CN/sys-user.json");
    }

    @Test
    @DisplayName("generatePath should return null for unsupported template")
    void generatePath_unsupported_shouldReturnNull() {
        String path = strategy.generatePath("unsupported", context, projectName);
        assertThat(path).isNull();
    }
}
