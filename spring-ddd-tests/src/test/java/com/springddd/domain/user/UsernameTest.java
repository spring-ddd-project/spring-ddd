package com.springddd.domain.user;

import com.springddd.domain.user.exception.UsernameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UsernameTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        Username username = new Username("admin");
        assertThat(username.value()).isEqualTo("admin");
    }

    @Test
    @DisplayName("value 为 null 应抛 UsernameException")
    void constructor_withNullValue_shouldThrowException() {
        assertThatThrownBy(() -> new Username(null))
                .isInstanceOf(UsernameException.class);
    }

    @Test
    @DisplayName("value 为空字符串应抛 UsernameException")
    void constructor_withEmptyValue_shouldThrowException() {
        assertThatThrownBy(() -> new Username(""))
                .isInstanceOf(UsernameException.class);
    }
}
