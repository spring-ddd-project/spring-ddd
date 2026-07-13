package com.springddd.domain.role;

import com.springddd.domain.role.exception.RoleMenuDataScopeMenuIdNullException;
import com.springddd.domain.role.exception.RoleMenuDataScopeNullException;
import com.springddd.domain.role.exception.RoleMenuDataScopeRoleIdNullException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class RoleMenuDataScopeInfoValidationTest {

    @ParameterizedTest
    @CsvSource({"0", "1", "2", "3", "4"})
    void shouldAcceptAllValidDataScopes(int scope) {
        RoleMenuDataScopeInfo info = new RoleMenuDataScopeInfo(1L, 2L, scope);
        assertEquals(1L, info.roleId());
        assertEquals(2L, info.menuId());
        assertEquals(scope, info.dataScope());
    }

    @Test
    void shouldRejectNullRoleId() {
        assertThrows(RoleMenuDataScopeRoleIdNullException.class, () -> new RoleMenuDataScopeInfo(null, 1L, 0));
    }

    @Test
    void shouldRejectNullMenuId() {
        assertThrows(RoleMenuDataScopeMenuIdNullException.class, () -> new RoleMenuDataScopeInfo(1L, null, 0));
    }

    @Test
    void shouldRejectNullDataScope() {
        assertThrows(RoleMenuDataScopeNullException.class, () -> new RoleMenuDataScopeInfo(1L, 1L, null));
    }

    @Test
    void shouldRejectInvalidDataScope() {
        assertThrows(RoleMenuDataScopeNullException.class, () -> new RoleMenuDataScopeInfo(1L, 1L, 99));
    }
}
