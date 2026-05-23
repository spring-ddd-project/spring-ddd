package com.springddd.domain.dept;

import com.springddd.domain.dept.exception.DeptNameNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeptBasicInfoTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        DeptBasicInfo obj = new DeptBasicInfo("test");
        assertThat(obj.deptName()).isEqualTo("test");
    }

    @Test
    @DisplayName("deptName 为 null 应抛异常")
    void constructor_withNullDeptname_shouldThrowException() {
        assertThatThrownBy(() -> new DeptBasicInfo(null))
                .isInstanceOf(DeptNameNullException.class);
    }

}