package com.springddd.application.service.gen.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FilePathContextTest {

    @Test
    @DisplayName("getFilePath 应返回匹配策略生成的路径")
    void getFilePath_shouldReturnMatchedStrategyPath() {
        FilePathStrategy strategy = new FilePathStrategy() {
            @Override
            public boolean supports(String templateName) { return "test".equals(templateName); }
            @Override
            public String generatePath(String templateName, Map<String, Object> context, String projectName) { return "/matched/path"; }
        };
        FilePathContext context = new FilePathContext(List.of(strategy));
        String result = context.getFilePath("test", Map.of("className", "User"), "proj");
        assertThat(result).isEqualTo("/matched/path");
    }

    @Test
    @DisplayName("getFilePath 当无匹配策略时应返回默认路径")
    void getFilePath_whenNoMatch_shouldReturnDefault() {
        FilePathContext context = new FilePathContext(List.of());
        String result = context.getFilePath("unknown", Map.of("className", "User"), "proj");
        assertThat(result).isEqualTo("User.txt");
    }
}
