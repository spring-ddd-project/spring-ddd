package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.TemplateContentNullException;
import com.springddd.domain.gen.exception.TemplateNameNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TemplateInfoTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        TemplateInfo obj = new TemplateInfo("test", "test");
        assertThat(obj.templateName()).isEqualTo("test");
        assertThat(obj.templateContent()).isEqualTo("test");
    }

    @Test
    @DisplayName("templateName 为 null 应抛异常")
    void constructor_withNullTemplatename_shouldThrowException() {
        assertThatThrownBy(() -> new TemplateInfo(null, "test"))
                .isInstanceOf(TemplateNameNullException.class);
    }

    @Test
    @DisplayName("templateContent 为 null 应抛异常")
    void constructor_withNullTemplatecontent_shouldThrowException() {
        assertThatThrownBy(() -> new TemplateInfo("test", null))
                .isInstanceOf(TemplateContentNullException.class);
    }

}