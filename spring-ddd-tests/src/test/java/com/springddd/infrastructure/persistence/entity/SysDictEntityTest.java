package com.springddd.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class SysDictEntityTest {

    @Test
    void shouldCreateSysDictEntityWithDefaultConstructor() {
        SysDictEntity entity = new SysDictEntity();
        assertNotNull(entity);
    }

    @Test
    void shouldSetAndGetId() {
        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        assertEquals(1L, entity.getId());
    }

    @Test
    void shouldSetAndGetDictName() {
        SysDictEntity entity = new SysDictEntity();
        entity.setDictName("字典A");
        assertEquals("字典A", entity.getDictName());
    }

    @Test
    void shouldSetAndGetDictCode() {
        SysDictEntity entity = new SysDictEntity();
        entity.setDictCode("dict_a");
        assertEquals("dict_a", entity.getDictCode());
    }

    @Test
    void shouldSetAndGetSortOrder() {
        SysDictEntity entity = new SysDictEntity();
        entity.setSortOrder(1);
        assertEquals(1, entity.getSortOrder());
    }

    @Test
    void shouldSetAndGetDictStatus() {
        SysDictEntity entity = new SysDictEntity();
        entity.setDictStatus(true);
        assertTrue(entity.getDictStatus());
    }

    @Test
    void shouldSetAndGetDeleteStatus() {
        SysDictEntity entity = new SysDictEntity();
        entity.setDeleteStatus(false);
        assertFalse(entity.getDeleteStatus());
    }

    @Test
    void shouldSetAndGetCreateBy() {
        SysDictEntity entity = new SysDictEntity();
        entity.setCreateBy("admin");
        assertEquals("admin", entity.getCreateBy());
    }

    @Test
    void shouldSetAndGetCreateTime() {
        SysDictEntity entity = new SysDictEntity();
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        assertEquals(now, entity.getCreateTime());
    }

    @Test
    void shouldSetAndGetUpdateBy() {
        SysDictEntity entity = new SysDictEntity();
        entity.setUpdateBy("admin");
        assertEquals("admin", entity.getUpdateBy());
    }

    @Test
    void shouldSetAndGetUpdateTime() {
        SysDictEntity entity = new SysDictEntity();
        LocalDateTime now = LocalDateTime.now();
        entity.setUpdateTime(now);
        assertEquals(now, entity.getUpdateTime());
    }

    @Test
    void shouldSetAndGetVersion() {
        SysDictEntity entity = new SysDictEntity();
        entity.setVersion(0);
        assertEquals(0, entity.getVersion());
    }

    @Test
    void equals_shouldWorkForSameInstance() {
        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        assertEquals(entity, entity);
    }

    @Test
    void equals_shouldWorkForSameValues() {
        SysDictEntity entity1 = new SysDictEntity();
        entity1.setId(1L);
        entity1.setDictName("字典A");

        SysDictEntity entity2 = new SysDictEntity();
        entity2.setId(1L);
        entity2.setDictName("字典A");

        assertEquals(entity1, entity2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        entity.setDictName("字典A");
        int hash1 = entity.hashCode();
        int hash2 = entity.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void toString_shouldContainFields() {
        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        entity.setDictName("字典A");
        String str = entity.toString();
        assertTrue(str.contains("SysDictEntity"));
        assertTrue(str.contains("1"));
        assertTrue(str.contains("字典A"));
    }
}
