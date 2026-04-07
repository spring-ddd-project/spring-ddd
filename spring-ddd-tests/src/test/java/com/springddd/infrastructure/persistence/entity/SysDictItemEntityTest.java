package com.springddd.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class SysDictItemEntityTest {

    @Test
    void shouldCreateSysDictItemEntityWithDefaultConstructor() {
        SysDictItemEntity entity = new SysDictItemEntity();
        assertNotNull(entity);
    }

    @Test
    void shouldSetAndGetId() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);
        assertEquals(1L, entity.getId());
    }

    @Test
    void shouldSetAndGetDictId() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setDictId(100L);
        assertEquals(100L, entity.getDictId());
    }

    @Test
    void shouldSetAndGetItemLabel() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setItemLabel("启用");
        assertEquals("启用", entity.getItemLabel());
    }

    @Test
    void shouldSetAndGetItemValue() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setItemValue(1);
        assertEquals(1, entity.getItemValue());
    }

    @Test
    void shouldSetAndGetSortOrder() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setSortOrder(1);
        assertEquals(1, entity.getSortOrder());
    }

    @Test
    void shouldSetAndGetItemStatus() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setItemStatus(true);
        assertTrue(entity.getItemStatus());
    }

    @Test
    void shouldSetAndGetDeleteStatus() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setDeleteStatus(false);
        assertFalse(entity.getDeleteStatus());
    }

    @Test
    void shouldSetAndGetCreateBy() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setCreateBy("admin");
        assertEquals("admin", entity.getCreateBy());
    }

    @Test
    void shouldSetAndGetCreateTime() {
        SysDictItemEntity entity = new SysDictItemEntity();
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        assertEquals(now, entity.getCreateTime());
    }

    @Test
    void shouldSetAndGetUpdateBy() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setUpdateBy("admin");
        assertEquals("admin", entity.getUpdateBy());
    }

    @Test
    void shouldSetAndGetUpdateTime() {
        SysDictItemEntity entity = new SysDictItemEntity();
        LocalDateTime now = LocalDateTime.now();
        entity.setUpdateTime(now);
        assertEquals(now, entity.getUpdateTime());
    }

    @Test
    void shouldSetAndGetVersion() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setVersion(0);
        assertEquals(0, entity.getVersion());
    }

    @Test
    void equals_shouldWorkForSameInstance() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);
        assertEquals(entity, entity);
    }

    @Test
    void equals_shouldWorkForSameValues() {
        SysDictItemEntity entity1 = new SysDictItemEntity();
        entity1.setId(1L);
        entity1.setItemLabel("启用");

        SysDictItemEntity entity2 = new SysDictItemEntity();
        entity2.setId(1L);
        entity2.setItemLabel("启用");

        assertEquals(entity1, entity2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);
        entity.setItemLabel("启用");
        int hash1 = entity.hashCode();
        int hash2 = entity.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void toString_shouldContainFields() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);
        entity.setItemLabel("启用");
        String str = entity.toString();
        assertTrue(str.contains("SysDictItemEntity"));
        assertTrue(str.contains("1"));
        assertTrue(str.contains("启用"));
    }
}
