package com.springddd.domain.gen;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenAggregateExtendInfoTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        GenAggregateExtendInfo obj = new GenAggregateExtendInfo(true);
        assertThat(obj.hasCreated()).isEqualTo(true);
    }

}