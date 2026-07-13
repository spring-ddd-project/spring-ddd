package com.springddd.domain.role;

import com.springddd.domain.role.exception.RoleMenuDataScopeMenuIdNullException;
import com.springddd.domain.role.exception.RoleMenuDataScopeNullException;
import com.springddd.domain.role.exception.RoleMenuDataScopeRoleIdNullException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleMenuDataScopeInfoTest {

    @Test
    void accessorsShouldWork() {
        var roleIdValue = 1L;
        var menuIdValue = 1L;
        var dataScopeValue = 1;
        RoleMenuDataScopeInfo rec = new RoleMenuDataScopeInfo(roleIdValue, menuIdValue, dataScopeValue);
        assertEquals(roleIdValue, rec.roleId());
        assertEquals(menuIdValue, rec.menuId());
        assertEquals(dataScopeValue, rec.dataScope());
    }

    @Test
    void equalsAndHashCodeAndToStringShouldWork() {
        var roleIdValue = 1L;
        var menuIdValue = 1L;
        var dataScopeValue = 1;
        RoleMenuDataScopeInfo a = new RoleMenuDataScopeInfo(roleIdValue, menuIdValue, dataScopeValue);
        RoleMenuDataScopeInfo b = new RoleMenuDataScopeInfo(roleIdValue, menuIdValue, dataScopeValue);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotNull(a.toString());
    }

    @Test
    void nullRoleIdShouldThrowException() {
        assertThrows(RoleMenuDataScopeRoleIdNullException.class, () -> new RoleMenuDataScopeInfo(null, 1L, 1));
    }

    @Test
    void nullMenuIdShouldThrowException() {
        assertThrows(RoleMenuDataScopeMenuIdNullException.class, () -> new RoleMenuDataScopeInfo(1L, null, 1));
    }

    @Test
    void nullDataScopeShouldThrowException() {
        assertThrows(RoleMenuDataScopeNullException.class, () -> new RoleMenuDataScopeInfo(1L, 1L, null));
    }

    @Test
    void invalidDataScopeShouldThrowException() {
        assertThrows(RoleMenuDataScopeNullException.class, () -> new RoleMenuDataScopeInfo(1L, 1L, 99));
    }

    @Test
    void validDataScopesShouldBeAccepted() {
        assertDoesNotThrow(() -> new RoleMenuDataScopeInfo(1L, 1L, 0));
        assertDoesNotThrow(() -> new RoleMenuDataScopeInfo(1L, 1L, 1));
        assertDoesNotThrow(() -> new RoleMenuDataScopeInfo(1L, 1L, 2));
        assertDoesNotThrow(() -> new RoleMenuDataScopeInfo(1L, 1L, 3));
        assertDoesNotThrow(() -> new RoleMenuDataScopeInfo(1L, 1L, 4));
    }
}
