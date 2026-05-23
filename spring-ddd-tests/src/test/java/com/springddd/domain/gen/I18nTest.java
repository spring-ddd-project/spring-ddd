package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.I18nEnNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class I18nTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        I18n i18n = new I18n("test", "en_US");
        assertThat(i18n.en()).isEqualTo("test");
        assertThat(i18n.locale()).isEqualTo("en_US");
    }

    @Test
    @DisplayName("en 为 null 应抛 I18nEnNullException")
    void constructor_withNullEn_shouldThrowException() {
        assertThatThrownBy(() -> new I18n(null, "en_US"))
                .isInstanceOf(I18nEnNullException.class);
    }

    @Test
    @DisplayName("en 为空字符串应抛 I18nEnNullException")
    void constructor_withEmptyEn_shouldThrowException() {
        assertThatThrownBy(() -> new I18n("", "en_US"))
                .isInstanceOf(I18nEnNullException.class);
    }

    @Test
    @DisplayName("locale 为 null 不应抛异常")
    void constructor_withNullLocale_shouldNotThrowException() {
        I18n i18n = new I18n("test", null);
        assertThat(i18n.en()).isEqualTo("test");
        assertThat(i18n.locale()).isNull();
    }
}
