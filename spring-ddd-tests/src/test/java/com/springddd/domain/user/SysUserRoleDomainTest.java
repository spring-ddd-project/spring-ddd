package com.springddd.domain.user;

import com.springddd.domain.role.RoleId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SysUserRoleDomainTest {

    @Test
    @DisplayName("create 应正常执行")
    void create_shouldExecute() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        domain.create();
        assertThat(domain.getUserRoleId()).isNull();
    }

    @Test
    @DisplayName("update 应更新字段")
    void update_shouldUpdateFields() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        domain.update(new UserId(1L), new RoleId(2L), 3L);

        assertThat(domain.getUserId().value()).isEqualTo(1L);
        assertThat(domain.getRoleId().value()).isEqualTo(2L);
        assertThat(domain.getDeptId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("delete 应设置 deleteStatus")
    void delete_shouldSetDeleteStatus() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        domain.delete();
        assertThat(domain.getDeleteStatus()).isTrue();
    }
}
