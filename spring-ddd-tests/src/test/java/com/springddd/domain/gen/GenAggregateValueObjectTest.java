package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.ObjectNameNullException;
import com.springddd.domain.gen.exception.ObjectTypeNullException;
import com.springddd.domain.gen.exception.ValueObjectNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GenAggregateValueObjectTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        GenAggregateValueObject vo = new GenAggregateValueObject("testName", "testValue", (byte) 1);
        assertThat(vo.objectName()).isEqualTo("testName");
        assertThat(vo.objectValue()).isEqualTo("testValue");
        assertThat(vo.objectType()).isEqualTo((byte) 1);
    }

    @Test
    @DisplayName("objectName 为 null 应抛 ObjectNameNullException")
    void constructor_withNullObjectName_shouldThrowException() {
        assertThatThrownBy(() -> new GenAggregateValueObject(null, "testValue", (byte) 1))
                .isInstanceOf(ObjectNameNullException.class);
    }

    @Test
    @DisplayName("objectName 为空字符串应抛 ObjectNameNullException")
    void constructor_withEmptyObjectName_shouldThrowException() {
        assertThatThrownBy(() -> new GenAggregateValueObject("", "testValue", (byte) 1))
                .isInstanceOf(ObjectNameNullException.class);
    }

    @Test
    @DisplayName("objectValue 为 null 应抛 ValueObjectNullException")
    void constructor_withNullObjectValue_shouldThrowException() {
        assertThatThrownBy(() -> new GenAggregateValueObject("testName", null, (byte) 1))
                .isInstanceOf(ValueObjectNullException.class);
    }

    @Test
    @DisplayName("objectValue 为空字符串应抛 ValueObjectNullException")
    void constructor_withEmptyObjectValue_shouldThrowException() {
        assertThatThrownBy(() -> new GenAggregateValueObject("testName", "", (byte) 1))
                .isInstanceOf(ValueObjectNullException.class);
    }

    @Test
    @DisplayName("objectType 为 null 应抛 ObjectTypeNullException")
    void constructor_withNullObjectType_shouldThrowException() {
        assertThatThrownBy(() -> new GenAggregateValueObject("testName", "testValue", null))
                .isInstanceOf(ObjectTypeNullException.class);
    }
}
