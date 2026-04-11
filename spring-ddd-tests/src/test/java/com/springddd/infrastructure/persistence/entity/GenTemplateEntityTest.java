package com.springddd.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class GenTemplateEntityTest {

    @Test
    void shouldCreateGenTemplateEntityWithDefaultConstructor() {
        GenTemplateEntity entity = new GenTemplateEntity();
        assertNotNull(entity);
    }

    @Test
    void shouldSetAndGetId() {
        GenTemplateEntity entity = new GenTemplateEntity();
        entity.setId(1L);
        assertEquals(1L, entity.getId());
    }

    @Test
    void shouldSetAndGetTemplateName() {
        GenTemplateEntity entity = new GenTemplateEntity();
        entity.setTemplateName("templateA");
        assertEquals("templateA", entity.getTemplateName());
    }

    @Test
    void shouldSetAndGetTemplateContent() {
        GenTemplateEntity entity = new GenTemplateEntity();
        entity.setTemplateContent("content here");
        assertEquals("content here", entity.getTemplateContent());
    }

    @Test
    void shouldSetAndGetDeleteStatus() {
        GenTemplateEntity entity = new GenTemplateEntity();
        entity.setDeleteStatus(false);
        assertFalse(entity.getDeleteStatus());
    }

    @Test
    void shouldSetAndGetCreateBy() {
        GenTemplateEntity entity = new GenTemplateEntity();
        entity.setCreateBy("admin");
        assertEquals("admin", entity.getCreateBy());
    }

    @Test
    void shouldSetAndGetCreateTime() {
        GenTemplateEntity entity = new GenTemplateEntity();
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        assertEquals(now, entity.getCreateTime());
    }

    @Test
    void shouldSetAndGetUpdateBy() {
        GenTemplateEntity entity = new GenTemplateEntity();
        entity.setUpdateBy("admin");
        assertEquals("admin", entity.getUpdateBy());
    }

    @Test
    void shouldSetAndGetUpdateTime() {
        GenTemplateEntity entity = new GenTemplateEntity();
        LocalDateTime now = LocalDateTime.now();
        entity.setUpdateTime(now);
        assertEquals(now, entity.getUpdateTime());
    }

    @Test
    void shouldSetAndGetVersion() {
        GenTemplateEntity entity = new GenTemplateEntity();
        entity.setVersion(0);
        assertEquals(0, entity.getVersion());
    }

    @Test
    void equals_shouldWorkForSameInstance() {
        GenTemplateEntity entity = new GenTemplateEntity();
        entity.setId(1L);
        assertEquals(entity, entity);
    }

    @Test
    void equals_shouldWorkForSameValues() {
        GenTemplateEntity entity1 = new GenTemplateEntity();
        entity1.setId(1L);
        entity1.setTemplateName("templateA");

        GenTemplateEntity entity2 = new GenTemplateEntity();
        entity2.setId(1L);
        entity2.setTemplateName("templateA");

        assertEquals(entity1, entity2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        GenTemplateEntity entity = new GenTemplateEntity();
        entity.setId(1L);
        entity.setTemplateName("templateA");
        int hash1 = entity.hashCode();
        int hash2 = entity.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void toString_shouldContainFields() {
        GenTemplateEntity entity = new GenTemplateEntity();
        entity.setId(1L);
        entity.setTemplateName("templateA");
        String str = entity.toString();
        assertTrue(str.contains("GenTemplateEntity"));
        assertTrue(str.contains("1"));
        assertTrue(str.contains("templateA"));
    }
}
