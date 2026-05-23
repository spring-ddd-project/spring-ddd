package com.springddd.domain.gen;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GenProjectInfoExtendInfoTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        GenProjectInfoExtendInfo obj = new GenProjectInfoExtendInfo("test", "test");
        assertThat(obj.requestName()).isEqualTo("test");
        assertThat(obj.projectVersion()).isEqualTo("test");
    }

    @Test
    @DisplayName("无参构造器")
    void noArgConstructor_shouldCreate() {
        GenProjectInfoExtendInfo obj = new GenProjectInfoExtendInfo();
        assertThat(obj.requestName()).isNull();
        assertThat(obj.projectVersion()).isNull();
    }

}