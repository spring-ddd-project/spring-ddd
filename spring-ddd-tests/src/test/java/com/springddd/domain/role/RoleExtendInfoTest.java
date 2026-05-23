package com.springddd.domain.role;

import com.springddd.domain.role.exception.RoleStatusNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RoleExtendInfoTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        RoleExtendInfo obj = new RoleExtendInfo("test", true, "test", true);
        assertThat(obj.roleRemark()).isEqualTo("test");
        assertThat(obj.ownerStatus()).isEqualTo(true);
        assertThat(obj.roleDesc()).isEqualTo("test");
        assertThat(obj.roleStatus()).isEqualTo(true);
    }

    @Test
    @DisplayName("自定义构造器正常构造")
    void customConstructor0_withValidValue_shouldCreate() {
        RoleExtendInfo obj = new RoleExtendInfo("test", true);
        assertThat(obj).isNotNull();
    }

    @Test
    @DisplayName("自定义构造器 roleStatus 为 null 应抛异常")
    void customConstructor0_withNullRolestatus_shouldThrowException() {
        assertThatThrownBy(() -> new RoleExtendInfo("test", null))
                .isInstanceOf(RoleStatusNullException.class);
    }

}