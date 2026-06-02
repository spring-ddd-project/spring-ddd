package com.springddd.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class SysDeptEntityTest {

    @Test
    void shouldCreateSysDeptEntityWithDefaultConstructor() {
        SysDeptEntity entity = new SysDeptEntity();
        assertNotNull(entity);
    }

    @Test
    void shouldSetAndGetId() {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setId(1L);
        assertEquals(1L, entity.getId());
    }

    @Test
    void shouldSetAndGetParentId() {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setParentId(0L);
        assertEquals(0L, entity.getParentId());
    }

    @Test
    void shouldSetAndGetDeptName() {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setDeptName("部门A");
        assertEquals("部门A", entity.getDeptName());
    }

    @Test
    void shouldSetAndGetSortOrder() {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setSortOrder(1);
        assertEquals(1, entity.getSortOrder());
    }

    @Test
    void shouldSetAndGetDeptStatus() {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setDeptStatus(true);
        assertTrue(entity.getDeptStatus());
    }

    @Test
    void shouldSetAndGetDeleteStatus() {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setDeleteStatus(false);
        assertFalse(entity.getDeleteStatus());
    }

    @Test
    void shouldSetAndGetCreateBy() {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setCreateBy("admin");
        assertEquals("admin", entity.getCreateBy());
    }

    @Test
    void shouldSetAndGetCreateTime() {
        SysDeptEntity entity = new SysDeptEntity();
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        assertEquals(now, entity.getCreateTime());
    }

    @Test
    void shouldSetAndGetUpdateBy() {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setUpdateBy("admin");
        assertEquals("admin", entity.getUpdateBy());
    }

    @Test
    void shouldSetAndGetUpdateTime() {
        SysDeptEntity entity = new SysDeptEntity();
        LocalDateTime now = LocalDateTime.now();
        entity.setUpdateTime(now);
        assertEquals(now, entity.getUpdateTime());
    }

    @Test
    void shouldSetAndGetVersion() {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setVersion(0);
        assertEquals(0, entity.getVersion());
    }

    @Test
    void equals_shouldWorkForSameInstance() {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setId(1L);
        assertEquals(entity, entity);
    }

    @Test
    void equals_shouldWorkForSameValues() {
        SysDeptEntity entity1 = new SysDeptEntity();
        entity1.setId(1L);
        entity1.setDeptName("部门A");

        SysDeptEntity entity2 = new SysDeptEntity();
        entity2.setId(1L);
        entity2.setDeptName("部门A");

        assertEquals(entity1, entity2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setId(1L);
        entity.setDeptName("部门A");
        int hash1 = entity.hashCode();
        int hash2 = entity.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void toString_shouldContainFields() {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setId(1L);
        entity.setDeptName("部门A");
        String str = entity.toString();
        assertTrue(str.contains("SysDeptEntity"));
        assertTrue(str.contains("1"));
        assertTrue(str.contains("部门A"));
    }
}
