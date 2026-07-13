package com.springddd.domain.role;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleMenuDataScopeIdTest {

    @Test
    void accessorsShouldWork() {
        var valueValue = 1L;
        RoleMenuDataScopeId rec = new RoleMenuDataScopeId(valueValue);
        assertEquals(valueValue, rec.value());
    }

    @Test
    void equalsAndHashCodeAndToStringShouldWork() {
        var valueValue = 1L;
        RoleMenuDataScopeId a = new RoleMenuDataScopeId(valueValue);
        RoleMenuDataScopeId b = new RoleMenuDataScopeId(valueValue);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotNull(a.toString());
    }
}
