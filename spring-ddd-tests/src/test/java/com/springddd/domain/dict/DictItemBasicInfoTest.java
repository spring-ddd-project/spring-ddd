package com.springddd.domain.dict;

import com.springddd.domain.dict.exception.DictItemLabelNullException;
import com.springddd.domain.dict.exception.DictItemValueNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DictItemBasicInfoTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        DictItemBasicInfo obj = new DictItemBasicInfo("test", 1, true);
        assertThat(obj.itemLabel()).isEqualTo("test");
        assertThat(obj.itemValue()).isEqualTo(1);
        assertThat(obj.itemStatus()).isEqualTo(true);
    }

    @Test
    @DisplayName("自定义构造器正常构造")
    void customConstructor0_withValidValue_shouldCreate() {
        DictItemBasicInfo obj = new DictItemBasicInfo("test", 1);
        assertThat(obj).isNotNull();
    }

    @Test
    @DisplayName("自定义构造器 itemLabel 为 null 应抛异常")
    void customConstructor0_withNullItemlabel_shouldThrowException() {
        assertThatThrownBy(() -> new DictItemBasicInfo(null, 1))
                .isInstanceOf(DictItemLabelNullException.class);
    }

    @Test
    @DisplayName("自定义构造器 itemValue 为 null 应抛异常")
    void customConstructor0_withNullItemvalue_shouldThrowException() {
        assertThatThrownBy(() -> new DictItemBasicInfo("test", null))
                .isInstanceOf(DictItemValueNullException.class);
    }

}