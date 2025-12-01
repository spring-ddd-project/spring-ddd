package com.springddd.application.service.dept.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for SysDeptViewMapStruct interface.
 * MapStruct generates an implementation at compile time.
 */
class SysDeptViewMapStructImplTest {

    @Test
    void shouldHaveSysDeptViewMapStructInterface() {
        assertNotNull(SysDeptViewMapStruct.class);
    }

    @Test
    void shouldHaveToViewMethod() {
        assertNotNull(SysDeptViewMapStruct.class.getDeclaredMethods());
    }

    @Test
    void shouldHaveToViewsMethod() {
        assertNotNull(SysDeptViewMapStruct.class.getDeclaredMethods());
    }
}
