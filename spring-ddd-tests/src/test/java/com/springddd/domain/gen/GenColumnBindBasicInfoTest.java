package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.ColumnTypeNullException;
import com.springddd.domain.gen.exception.ComponentTypeNullException;
import com.springddd.domain.gen.exception.JavaTypeNullException;
import com.springddd.domain.gen.exception.TypeScriptTypeNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GenColumnBindBasicInfoTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        GenColumnBindBasicInfo obj = new GenColumnBindBasicInfo("test", "test", (byte) 1, (byte) 1);
        assertThat(obj.columnType()).isEqualTo("test");
        assertThat(obj.entityType()).isEqualTo("test");
        assertThat(obj.componentType()).isEqualTo((byte) 1);
        assertThat(obj.typescriptType()).isEqualTo((byte) 1);
    }

    @Test
    @DisplayName("columnType 为 null 应抛异常")
    void constructor_withNullColumntype_shouldThrowException() {
        assertThatThrownBy(() -> new GenColumnBindBasicInfo(null, "test", (byte) 1, (byte) 1))
                .isInstanceOf(ColumnTypeNullException.class);
    }

    @Test
    @DisplayName("entityType 为 null 应抛异常")
    void constructor_withNullEntitytype_shouldThrowException() {
        assertThatThrownBy(() -> new GenColumnBindBasicInfo("test", null, (byte) 1, (byte) 1))
                .isInstanceOf(JavaTypeNullException.class);
    }

    @Test
    @DisplayName("componentType 为 null 应抛异常")
    void constructor_withNullComponenttype_shouldThrowException() {
        assertThatThrownBy(() -> new GenColumnBindBasicInfo("test", "test", null, (byte) 1))
                .isInstanceOf(ComponentTypeNullException.class);
    }

    @Test
    @DisplayName("typescriptType 为 null 应抛异常")
    void constructor_withNullTypescripttype_shouldThrowException() {
        assertThatThrownBy(() -> new GenColumnBindBasicInfo("test", "test", (byte) 1, null))
                .isInstanceOf(TypeScriptTypeNullException.class);
    }

}