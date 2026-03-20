package com.springddd.domain.util;

import com.springddd.domain.AbstractDomainMask;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AbstractDomainMaskTest {

    @Test
    void concreteClass_ShouldBeAbstract() {
        // Verify AbstractDomainMask is abstract
        assertTrue(java.lang.reflect.Modifier.isAbstract(AbstractDomainMask.class.getModifiers()));
    }

    @Test
    void concreteImplementation_ShouldStoreAllFields() {
        // Given
        TestDomainMask mask = new TestDomainMask();

        // When
        mask.setDeptId(100L);
        mask.setCreateBy("admin");
        mask.setCreateTime(LocalDateTime.now());
        mask.setUpdateBy("admin");
        mask.setUpdateTime(LocalDateTime.now());
        mask.setDeleteStatus(false);
        mask.setVersion(1);

        // Then
        assertEquals(100L, mask.getDeptId());
        assertEquals("admin", mask.getCreateBy());
        assertNotNull(mask.getCreateTime());
        assertEquals("admin", mask.getUpdateBy());
        assertNotNull(mask.getUpdateTime());
        assertFalse(mask.getDeleteStatus());
        assertEquals(1, mask.getVersion());
    }

    @Test
    void concreteImplementation_AllFieldsCanBeSetAndRetrieved() {
        // Given
        TestDomainMask mask = new TestDomainMask();
        LocalDateTime now = LocalDateTime.now();

        // When - Set all fields
        mask.setDeptId(1L);
        mask.setCreateBy("creator");
        mask.setCreateTime(now);
        mask.setUpdateBy("updater");
        mask.setUpdateTime(now);
        mask.setDeleteStatus(true);
        mask.setVersion(5);

        // Then - Verify all fields
        assertEquals(1L, mask.getDeptId());
        assertEquals("creator", mask.getCreateBy());
        assertEquals(now, mask.getCreateTime());
        assertEquals("updater", mask.getUpdateBy());
        assertEquals(now, mask.getUpdateTime());
        assertTrue(mask.getDeleteStatus());
        assertEquals(5, mask.getVersion());
    }

    @Test
    void concreteImplementation_FieldsDefaultToNull() {
        // Given
        TestDomainMask mask = new TestDomainMask();

        // Then - All fields should be null by default
        assertNull(mask.getDeptId());
        assertNull(mask.getCreateBy());
        assertNull(mask.getCreateTime());
        assertNull(mask.getUpdateBy());
        assertNull(mask.getUpdateTime());
        assertNull(mask.getDeleteStatus());
        assertNull(mask.getVersion());
    }

    @Test
    void concreteImplementation_VersionCanBeZero() {
        // Given
        TestDomainMask mask = new TestDomainMask();

        // When
        mask.setVersion(0);

        // Then
        assertEquals(0, mask.getVersion());
    }

    @Test
    void concreteImplementation_DeleteStatusCanBeTrue() {
        // Given
        TestDomainMask mask = new TestDomainMask();

        // When
        mask.setDeleteStatus(true);

        // Then
        assertTrue(mask.getDeleteStatus());
    }

    @Test
    void concreteImplementation_DeleteStatusCanBeFalse() {
        // Given
        TestDomainMask mask = new TestDomainMask();

        // When
        mask.setDeleteStatus(false);

        // Then
        assertFalse(mask.getDeleteStatus());
    }

    @Test
    void concreteImplementation_LombokDataAnnotationProvidesGettersAndSetters() {
        // Given
        TestDomainMask mask = new TestDomainMask();

        // When - Using Lombok @Data generated methods
        mask.setDeptId(50L);
        mask.setVersion(10);

        // Then - Getters should work
        assertEquals(50L, mask.getDeptId());
        assertEquals(10, mask.getVersion());
    }

    @Test
    void concreteImplementation_LombokDataAnnotationProvidesEqualsAndHashCode() {
        // Given
        TestDomainMask mask1 = new TestDomainMask();
        mask1.setDeptId(1L);
        TestDomainMask mask2 = new TestDomainMask();
        mask2.setDeptId(1L);
        TestDomainMask mask3 = new TestDomainMask();
        mask3.setDeptId(2L);

        // Then
        assertEquals(mask1, mask2);
        assertNotEquals(mask1, mask3);
        assertEquals(mask1.hashCode(), mask2.hashCode());
    }

    @Test
    void concreteImplementation_LombokDataAnnotationProvidesToString() {
        // Given
        TestDomainMask mask = new TestDomainMask();
        mask.setDeptId(100L);

        // When
        String result = mask.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("100"));
    }

    // Test implementation of AbstractDomainMask
    private static class TestDomainMask extends AbstractDomainMask {
    }
}
