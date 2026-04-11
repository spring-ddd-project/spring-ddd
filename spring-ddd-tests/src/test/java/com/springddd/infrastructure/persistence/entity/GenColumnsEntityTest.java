package com.springddd.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class GenColumnsEntityTest {

    @Test
    void shouldCreateGenColumnsEntityWithDefaultConstructor() {
        GenColumnsEntity entity = new GenColumnsEntity();
        assertNotNull(entity);
    }

    @Test
    void shouldSetAndGetId() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setId(1L);
        assertEquals(1L, entity.getId());
    }

    @Test
    void shouldSetAndGetInfoId() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setInfoId(100L);
        assertEquals(100L, entity.getInfoId());
    }

    @Test
    void shouldSetAndGetPropColumnKey() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setPropColumnKey("pri");
        assertEquals("pri", entity.getPropColumnKey());
    }

    @Test
    void shouldSetAndGetPropColumnName() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setPropColumnName("id");
        assertEquals("id", entity.getPropColumnName());
    }

    @Test
    void shouldSetAndGetPropColumnType() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setPropColumnType("bigint");
        assertEquals("bigint", entity.getPropColumnType());
    }

    @Test
    void shouldSetAndGetPropColumnComment() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setPropColumnComment("主键ID");
        assertEquals("主键ID", entity.getPropColumnComment());
    }

    @Test
    void shouldSetAndGetPropJavaEntity() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setPropJavaEntity("Long");
        assertEquals("Long", entity.getPropJavaEntity());
    }

    @Test
    void shouldSetAndGetPropJavaType() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setPropJavaType("Long");
        assertEquals("Long", entity.getPropJavaType());
    }

    @Test
    void shouldSetAndGetPropDictId() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setPropDictId(200L);
        assertEquals(200L, entity.getPropDictId());
    }

    @Test
    void shouldSetAndGetTableVisible() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setTableVisible(true);
        assertTrue(entity.getTableVisible());
    }

    @Test
    void shouldSetAndGetTableOrder() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setTableOrder(false);
        assertFalse(entity.getTableOrder());
    }

    @Test
    void shouldSetAndGetTableFilter() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setTableFilter(true);
        assertTrue(entity.getTableFilter());
    }

    @Test
    void shouldSetAndGetTableFilterComponent() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setTableFilterComponent((byte) 1);
        assertEquals((byte) 1, entity.getTableFilterComponent());
    }

    @Test
    void shouldSetAndGetTableFilterType() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setTableFilterType((byte) 2);
        assertEquals((byte) 2, entity.getTableFilterType());
    }

    @Test
    void shouldSetAndGetTypescriptType() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setTypescriptType((byte) 3);
        assertEquals((byte) 3, entity.getTypescriptType());
    }

    @Test
    void shouldSetAndGetFormComponent() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setFormComponent((byte) 4);
        assertEquals((byte) 4, entity.getFormComponent());
    }

    @Test
    void shouldSetAndGetFormVisible() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setFormVisible(true);
        assertTrue(entity.getFormVisible());
    }

    @Test
    void shouldSetAndGetFormRequired() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setFormRequired(false);
        assertFalse(entity.getFormRequired());
    }

    @Test
    void shouldSetAndGetEn() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setEn("english");
        assertEquals("english", entity.getEn());
    }

    @Test
    void shouldSetAndGetLocale() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setLocale("zh_CN");
        assertEquals("zh_CN", entity.getLocale());
    }

    @Test
    void shouldSetAndGetDeleteStatus() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setDeleteStatus(false);
        assertFalse(entity.getDeleteStatus());
    }

    @Test
    void shouldSetAndGetCreateBy() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setCreateBy("admin");
        assertEquals("admin", entity.getCreateBy());
    }

    @Test
    void shouldSetAndGetCreateTime() {
        GenColumnsEntity entity = new GenColumnsEntity();
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        assertEquals(now, entity.getCreateTime());
    }

    @Test
    void shouldSetAndGetUpdateBy() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setUpdateBy("admin");
        assertEquals("admin", entity.getUpdateBy());
    }

    @Test
    void shouldSetAndGetUpdateTime() {
        GenColumnsEntity entity = new GenColumnsEntity();
        LocalDateTime now = LocalDateTime.now();
        entity.setUpdateTime(now);
        assertEquals(now, entity.getUpdateTime());
    }

    @Test
    void shouldSetAndGetVersion() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setVersion(0);
        assertEquals(0, entity.getVersion());
    }

    @Test
    void equals_shouldWorkForSameInstance() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setId(1L);
        assertEquals(entity, entity);
    }

    @Test
    void equals_shouldWorkForSameValues() {
        GenColumnsEntity entity1 = new GenColumnsEntity();
        entity1.setId(1L);
        entity1.setPropColumnName("id");

        GenColumnsEntity entity2 = new GenColumnsEntity();
        entity2.setId(1L);
        entity2.setPropColumnName("id");

        assertEquals(entity1, entity2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setId(1L);
        entity.setPropColumnName("id");
        int hash1 = entity.hashCode();
        int hash2 = entity.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void toString_shouldContainFields() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setId(1L);
        entity.setPropColumnName("id");
        String str = entity.toString();
        assertTrue(str.contains("GenColumnsEntity"));
        assertTrue(str.contains("1"));
        assertTrue(str.contains("id"));
    }
}
