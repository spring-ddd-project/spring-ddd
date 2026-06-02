package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class GenTemplateDomainTest {

    @Test
    void shouldCreateGenTemplateDomainWithAllFields() {
        GenTemplateDomain domain = new GenTemplateDomain();
        TemplateId templateId = new TemplateId(1L);
        TemplateInfo templateInfo = new TemplateInfo("templateName", "templateContent");

        domain.setId(templateId);
        domain.setTemplateInfo(templateInfo);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setDeleteStatus(false);
        domain.setVersion(0);

        assertEquals(templateId, domain.getId());
        assertEquals(templateInfo, domain.getTemplateInfo());
        assertEquals("admin", domain.getCreateBy());
        assertNotNull(domain.getCreateTime());
        assertEquals("admin", domain.getUpdateBy());
        assertNotNull(domain.getUpdateTime());
        assertFalse(domain.getDeleteStatus());
        assertEquals(0, domain.getVersion());
    }

    @Test
    void shouldCallCreateMethod() {
        GenTemplateDomain domain = new GenTemplateDomain();

        domain.create();

        assertNotNull(domain);
    }

    @Test
    void shouldUpdateGenTemplateDomain() {
        GenTemplateDomain domain = new GenTemplateDomain();
        TemplateInfo newTemplateInfo = new TemplateInfo("newTemplateName", "newTemplateContent");

        domain.update(newTemplateInfo);

        assertEquals(newTemplateInfo, domain.getTemplateInfo());
    }

    @Test
    void shouldDeleteGenTemplateDomain() {
        GenTemplateDomain domain = new GenTemplateDomain();
        domain.setDeleteStatus(false);

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void shouldRestoreGenTemplateDomain() {
        GenTemplateDomain domain = new GenTemplateDomain();
        domain.setDeleteStatus(true);

        domain.restore();

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldSetAndGetFields() {
        GenTemplateDomain domain = new GenTemplateDomain();
        TemplateId templateId = new TemplateId(10L);
        TemplateInfo templateInfo = new TemplateInfo("name", "content");

        domain.setId(templateId);
        domain.setTemplateInfo(templateInfo);

        assertEquals(templateId, domain.getId());
        assertEquals(templateInfo, domain.getTemplateInfo());
    }

    @Test
    void shouldHandleNullValues() {
        GenTemplateDomain domain = new GenTemplateDomain();

        domain.setId(null);
        domain.setTemplateInfo(null);

        assertNull(domain.getId());
        assertNull(domain.getTemplateInfo());
    }
}
