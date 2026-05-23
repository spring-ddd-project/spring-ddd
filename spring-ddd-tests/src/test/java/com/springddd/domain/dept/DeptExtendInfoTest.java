package com.springddd.domain.dept;

import com.springddd.domain.dept.exception.DeptStatusNullException;
import com.springddd.domain.dept.exception.SortOrderNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeptExtendInfoTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        DeptExtendInfo obj = new DeptExtendInfo(1, true);
        assertThat(obj.sortOrder()).isEqualTo(1);
        assertThat(obj.deptStatus()).isEqualTo(true);
    }

    @Test
    @DisplayName("sortOrder 为 null 应抛异常")
    void constructor_withNullSortorder_shouldThrowException() {
        assertThatThrownBy(() -> new DeptExtendInfo(null, true))
                .isInstanceOf(SortOrderNullException.class);
    }

    @Test
    @DisplayName("deptStatus 为 null 应抛异常")
    void constructor_withNullDeptstatus_shouldThrowException() {
        assertThatThrownBy(() -> new DeptExtendInfo(1, null))
                .isInstanceOf(DeptStatusNullException.class);
    }

}