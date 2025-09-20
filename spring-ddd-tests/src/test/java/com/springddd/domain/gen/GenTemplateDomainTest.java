package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GenTemplateDomainTest {

    @Test
    void create_shouldInitializeSuccessfully() {
        GenTemplateDomain domain = new GenTemplateDomain();
        domain.create();
        assertNotNull(domain);
    }

    @Test
    void update_shouldUpdateTemplateInfo() {
        GenTemplateDomain domain = new GenTemplateDomain();
        TemplateInfo templateInfo = new TemplateInfo("templateName", "templateContent");

        domain.update(templateInfo);

        assertEquals(templateInfo, domain.getTemplateInfo());
    }

    @Test
    void delete_shouldSetDeleteStatusToTrue() {
        GenTemplateDomain domain = new GenTemplateDomain();
        assertNull(domain.getDeleteStatus());

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void restore_shouldSetDeleteStatusToFalse() {
        GenTemplateDomain domain = new GenTemplateDomain();
        domain.delete();
        assertTrue(domain.getDeleteStatus());

        domain.restore();

        assertFalse(domain.getDeleteStatus());
    }
}
