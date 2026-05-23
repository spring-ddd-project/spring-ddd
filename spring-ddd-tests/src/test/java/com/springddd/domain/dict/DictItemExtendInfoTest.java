package com.springddd.domain.dict;

import com.springddd.domain.dict.exception.DictItemItemStatusNullException;
import com.springddd.domain.dict.exception.DictItemSortOrderNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DictItemExtendInfoTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        DictItemExtendInfo obj = new DictItemExtendInfo(1, true);
        assertThat(obj.sortOrder()).isEqualTo(1);
        assertThat(obj.itemStatus()).isEqualTo(true);
    }

    @Test
    @DisplayName("sortOrder 为 null 应抛异常")
    void constructor_withNullSortorder_shouldThrowException() {
        assertThatThrownBy(() -> new DictItemExtendInfo(null, true))
                .isInstanceOf(DictItemSortOrderNullException.class);
    }

    @Test
    @DisplayName("itemStatus 为 null 应抛异常")
    void constructor_withNullItemstatus_shouldThrowException() {
        assertThatThrownBy(() -> new DictItemExtendInfo(1, null))
                .isInstanceOf(DictItemItemStatusNullException.class);
    }

}