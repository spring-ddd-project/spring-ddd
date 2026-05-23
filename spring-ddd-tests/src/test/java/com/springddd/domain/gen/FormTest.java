package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.FormComponentNullException;
import com.springddd.domain.gen.exception.FormRequiredNullException;
import com.springddd.domain.gen.exception.FormVisibleNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FormTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        Form form = new Form((byte) 1, true, true);
        assertThat(form.formComponent()).isEqualTo((byte) 1);
        assertThat(form.formVisible()).isTrue();
        assertThat(form.formRequired()).isTrue();
    }

    @Test
    @DisplayName("formComponent 为 null 应抛 FormComponentNullException")
    void constructor_withNullFormComponent_shouldThrowException() {
        assertThatThrownBy(() -> new Form(null, true, true))
                .isInstanceOf(FormComponentNullException.class);
    }

    @Test
    @DisplayName("formVisible 为 null 应抛 FormVisibleNullException")
    void constructor_withNullFormVisible_shouldThrowException() {
        assertThatThrownBy(() -> new Form((byte) 1, null, true))
                .isInstanceOf(FormVisibleNullException.class);
    }

    @Test
    @DisplayName("formRequired 为 null 应抛 FormRequiredNullException")
    void constructor_withNullFormRequired_shouldThrowException() {
        assertThatThrownBy(() -> new Form((byte) 1, true, null))
                .isInstanceOf(FormRequiredNullException.class);
    }
}
