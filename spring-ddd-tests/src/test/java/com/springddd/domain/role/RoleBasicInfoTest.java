package com.springddd.domain.role;

import com.springddd.domain.role.exception.RoleCodeNullException;
import com.springddd.domain.role.exception.RoleDataScopeNullException;
import com.springddd.domain.role.exception.RoleNameNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RoleBasicInfoTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        RoleBasicInfo obj = new RoleBasicInfo("test", "test", 1, true, 1, true);
        assertThat(obj.roleName()).isEqualTo("test");
        assertThat(obj.roleCode()).isEqualTo("test");
        assertThat(obj.roleSort()).isEqualTo(1);
        assertThat(obj.roleStatus()).isEqualTo(true);
        assertThat(obj.roleDataScope()).isEqualTo(1);
        assertThat(obj.roleOwner()).isEqualTo(true);
    }

    @Test
    @DisplayName("自定义构造器正常构造")
    void customConstructor0_withValidValue_shouldCreate() {
        RoleBasicInfo obj = new RoleBasicInfo("test", "test", 1, true);
        assertThat(obj).isNotNull();
    }

    @Test
    @DisplayName("自定义构造器 roleName 为 null 应抛异常")
    void customConstructor0_withNullRolename_shouldThrowException() {
        assertThatThrownBy(() -> new RoleBasicInfo(null, "test", 1, true))
                .isInstanceOf(RoleNameNullException.class);
    }

    @Test
    @DisplayName("自定义构造器 roleCode 为 null 应抛异常")
    void customConstructor0_withNullRolecode_shouldThrowException() {
        assertThatThrownBy(() -> new RoleBasicInfo("test", null, 1, true))
                .isInstanceOf(RoleCodeNullException.class);
    }

    @Test
    @DisplayName("自定义构造器 roleDataScope 为 null 应抛异常")
    void customConstructor0_withNullRoledatascope_shouldThrowException() {
        assertThatThrownBy(() -> new RoleBasicInfo("test", "test", null, true))
                .isInstanceOf(RoleDataScopeNullException.class);
    }

}