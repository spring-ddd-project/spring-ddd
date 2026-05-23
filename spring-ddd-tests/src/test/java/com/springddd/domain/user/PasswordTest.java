package com.springddd.domain.user;

import com.springddd.domain.user.exception.PasswordNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        Password password = new Password("secret123");
        assertThat(password.value()).isEqualTo("secret123");
    }

    @Test
    @DisplayName("value 为 null 应抛 PasswordNullException")
    void constructor_withNullValue_shouldThrowException() {
        assertThatThrownBy(() -> new Password(null))
                .isInstanceOf(PasswordNullException.class);
    }

    @Test
    @DisplayName("value 为空字符串应抛 PasswordNullException")
    void constructor_withEmptyValue_shouldThrowException() {
        assertThatThrownBy(() -> new Password(""))
                .isInstanceOf(PasswordNullException.class);
    }
}
