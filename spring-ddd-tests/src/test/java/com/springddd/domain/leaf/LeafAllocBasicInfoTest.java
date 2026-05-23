package com.springddd.domain.leaf;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LeafAllocBasicInfoTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        LeafAllocBasicInfo obj = new LeafAllocBasicInfo("test");
        assertThat(obj.description()).isEqualTo("test");
    }

    @Test
    @DisplayName("null description 应转为空字符串")
    void constructor_withNullDescription_shouldBecomeEmpty() {
        LeafAllocBasicInfo obj = new LeafAllocBasicInfo(null);
        assertThat(obj.description()).isEqualTo("");
    }

}