package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenTemplateDomain;
import com.springddd.domain.gen.TemplateInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenTemplateDomainFactoryImplTest {

    private final GenTemplateDomainFactoryImpl factory = new GenTemplateDomainFactoryImpl();

    @Test
    @DisplayName("should create GenTemplateDomain with correct fields set")
    void newInstance() {
        TemplateInfo templateInfo = new TemplateInfo("TestTemplate", "Template Content");

        GenTemplateDomain domain = factory.newInstance(templateInfo);

        assertNotNull(domain);
        assertEquals(templateInfo, domain.getTemplateInfo());
        assertFalse(domain.getDeleteStatus());
    }
}
