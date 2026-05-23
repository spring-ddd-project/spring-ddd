package com.springddd.application.service.gen.adapter;

import freemarker.template.Configuration;
import freemarker.template.Version;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FreemarkerTemplateAdapterTest {

    @Test
    @DisplayName("render 应渲染 Freemarker 模板")
    void render_shouldRenderTemplate() {
        Configuration configuration = new Configuration(new Version("2.3.32"));
        FreemarkerTemplateAdapter adapter = new FreemarkerTemplateAdapter(configuration);

        String templateContent = "Hello, ${name}!";
        Map<String, Object> dataModel = Map.of("name", "World");

        StepVerifier.create(adapter.render("test", templateContent, dataModel))
                .assertNext(result -> assertThat(result).isEqualTo("Hello, World!"))
                .verifyComplete();
    }

    @Test
    @DisplayName("render 当模板有语法错误时应返回 error Mono")
    void render_whenInvalidTemplate_shouldReturnError() {
        Configuration configuration = new Configuration(new Version("2.3.32"));
        FreemarkerTemplateAdapter adapter = new FreemarkerTemplateAdapter(configuration);

        String invalidTemplate = "<#broken>";
        Map<String, Object> dataModel = Map.of();

        StepVerifier.create(adapter.render("test", invalidTemplate, dataModel))
                .expectError()
                .verify();
    }
}
