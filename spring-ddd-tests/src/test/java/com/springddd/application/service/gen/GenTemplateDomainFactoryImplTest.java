package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenTemplateDomain;
import com.springddd.domain.gen.TemplateInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenTemplateDomainFactoryImplTest {

    private final GenTemplateDomainFactoryImpl factory = new GenTemplateDomainFactoryImpl();

    @Test
    void shouldCreateGenTemplateDomainWithTemplateInfo() {
        TemplateInfo templateInfo = new TemplateInfo("templateName", "templateContent");

        GenTemplateDomain domain = factory.newInstance(templateInfo);

        assertNotNull(domain);
        assertEquals(templateInfo, domain.getTemplateInfo());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldCreateGenTemplateDomainWithNullTemplateInfo() {
        GenTemplateDomain domain = factory.newInstance(null);

        assertNotNull(domain);
        assertNull(domain.getTemplateInfo());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldSetDeleteStatusToFalse() {
        TemplateInfo templateInfo = new TemplateInfo("newTemplate", "newContent");

        GenTemplateDomain domain = factory.newInstance(templateInfo);

        assertFalse(domain.getDeleteStatus());
    }
}
