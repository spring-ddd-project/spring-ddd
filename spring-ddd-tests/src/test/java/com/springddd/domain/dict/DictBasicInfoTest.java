package com.springddd.domain.dict;

import com.springddd.domain.dict.exception.DictCodeNullException;
import com.springddd.domain.dict.exception.DictNameNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DictBasicInfoTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        DictBasicInfo obj = new DictBasicInfo("test", "test");
        assertThat(obj.dictName()).isEqualTo("test");
        assertThat(obj.dictCode()).isEqualTo("test");
    }

    @Test
    @DisplayName("dictName 为 null 应抛异常")
    void constructor_withNullDictname_shouldThrowException() {
        assertThatThrownBy(() -> new DictBasicInfo(null, "test"))
                .isInstanceOf(DictNameNullException.class);
    }

    @Test
    @DisplayName("dictCode 为 null 应抛异常")
    void constructor_withNullDictcode_shouldThrowException() {
        assertThatThrownBy(() -> new DictBasicInfo("test", null))
                .isInstanceOf(DictCodeNullException.class);
    }

}