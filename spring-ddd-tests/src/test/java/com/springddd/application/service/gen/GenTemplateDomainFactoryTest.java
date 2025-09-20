package com.springddd.application.service.gen;

import com.springddd.domain.gen.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GenTemplateDomainFactoryTest {

    private final GenTemplateDomainFactoryImpl factory = new GenTemplateDomainFactoryImpl();

    @Test
    void newInstance_shouldCreateDomainWithCorrectValues() {
        TemplateInfo templateInfo = new TemplateInfo("templateName", "templateContent");

        GenTemplateDomain domain = factory.newInstance(templateInfo);

        assertNotNull(domain);
        assertEquals(templateInfo, domain.getTemplateInfo());
        assertFalse(domain.getDeleteStatus());
    }
}
