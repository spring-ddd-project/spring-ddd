package com.springddd.domain.dict;

import com.springddd.domain.dict.exception.DictDictStatusNullException;
import com.springddd.domain.dict.exception.DictSortOrderNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DictExtendInfoTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        DictExtendInfo obj = new DictExtendInfo(1, true);
        assertThat(obj.sortOrder()).isEqualTo(1);
        assertThat(obj.dictStatus()).isEqualTo(true);
    }

    @Test
    @DisplayName("sortOrder 为 null 应抛异常")
    void constructor_withNullSortorder_shouldThrowException() {
        assertThatThrownBy(() -> new DictExtendInfo(null, true))
                .isInstanceOf(DictSortOrderNullException.class);
    }

    @Test
    @DisplayName("dictStatus 为 null 应抛异常")
    void constructor_withNullDictstatus_shouldThrowException() {
        assertThatThrownBy(() -> new DictExtendInfo(1, null))
                .isInstanceOf(DictDictStatusNullException.class);
    }

}