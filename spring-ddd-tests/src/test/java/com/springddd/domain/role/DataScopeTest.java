package com.springddd.domain.role;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataScopeTest {

    @Test
    void shouldReturnScopeForValidValues() {
        assertEquals(DataScope.ALL, DataScope.of(0));
        assertEquals(DataScope.DEPT_ONLY, DataScope.of(1));
        assertEquals(DataScope.DEPT_AND_CHILDREN, DataScope.of(2));
        assertEquals(DataScope.PERSONAL, DataScope.of(3));
        assertEquals(DataScope.POST, DataScope.of(4));
    }

    @Test
    void shouldReturnNullForInvalidOrNullValue() {
        assertNull(DataScope.of(null));
        assertNull(DataScope.of(-1));
        assertNull(DataScope.of(99));
    }

    @Test
    void shouldReturnCorrectIntValue() {
        assertEquals(0, DataScope.ALL.value());
        assertEquals(1, DataScope.DEPT_ONLY.value());
        assertEquals(2, DataScope.DEPT_AND_CHILDREN.value());
        assertEquals(3, DataScope.PERSONAL.value());
        assertEquals(4, DataScope.POST.value());
    }

    @Test
    void shouldValidateOnlyKnownValues() {
        assertTrue(DataScope.isValid(0));
        assertTrue(DataScope.isValid(4));
        assertFalse(DataScope.isValid(null));
        assertFalse(DataScope.isValid(100));
    }

    @Test
    void shouldResolveAllValues() {
        assertEquals(DataScope.ALL, DataScope.of(0));
        assertEquals(DataScope.DEPT_ONLY, DataScope.of(1));
        assertEquals(DataScope.DEPT_AND_CHILDREN, DataScope.of(2));
        assertEquals(DataScope.PERSONAL, DataScope.of(3));
        assertEquals(DataScope.POST, DataScope.of(4));
    }
}
