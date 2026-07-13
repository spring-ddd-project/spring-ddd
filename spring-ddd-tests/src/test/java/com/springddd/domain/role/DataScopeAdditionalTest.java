package com.springddd.domain.role;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataScopeAdditionalTest {

    @Test
    void shouldContainAllValuesInOrder() {
        DataScope[] scopes = DataScope.values();
        assertEquals(5, scopes.length);
        assertEquals(DataScope.ALL, scopes[0]);
        assertEquals(DataScope.DEPT_ONLY, scopes[1]);
        assertEquals(DataScope.DEPT_AND_CHILDREN, scopes[2]);
        assertEquals(DataScope.PERSONAL, scopes[3]);
        assertEquals(DataScope.POST, scopes[4]);
    }

    @Test
    void shouldResolveByName() {
        assertEquals(DataScope.ALL, DataScope.valueOf("ALL"));
        assertEquals(DataScope.DEPT_ONLY, DataScope.valueOf("DEPT_ONLY"));
        assertEquals(DataScope.DEPT_AND_CHILDREN, DataScope.valueOf("DEPT_AND_CHILDREN"));
        assertEquals(DataScope.PERSONAL, DataScope.valueOf("PERSONAL"));
        assertEquals(DataScope.POST, DataScope.valueOf("POST"));
    }

    @Test
    void shouldThrowForUnknownName() {
        assertThrows(IllegalArgumentException.class, () -> DataScope.valueOf("UNKNOWN"));
    }
}
