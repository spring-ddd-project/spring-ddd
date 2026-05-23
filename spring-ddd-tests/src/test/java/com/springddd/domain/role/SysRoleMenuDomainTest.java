package com.springddd.domain.role;

import com.springddd.domain.menu.MenuId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SysRoleMenuDomainTest {

    @Test
    @DisplayName("create 应正常执行")
    void create_shouldExecute() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        domain.create();
        assertThat(domain.getRoleMenuId()).isNull();
    }

    @Test
    @DisplayName("update 应更新字段")
    void update_shouldUpdateFields() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        domain.update(new RoleId(1L), new MenuId(2L), 3L, "admin");

        assertThat(domain.getRoleId().value()).isEqualTo(1L);
        assertThat(domain.getMenuId().value()).isEqualTo(2L);
        assertThat(domain.getDeptId()).isEqualTo(3L);
        assertThat(domain.getUpdateBy()).isEqualTo("admin");
    }

    @Test
    @DisplayName("delete 应设置 deleteStatus")
    void delete_shouldSetDeleteStatus() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        domain.delete("admin");
        assertThat(domain.getDeleteStatus()).isTrue();
    }
}
