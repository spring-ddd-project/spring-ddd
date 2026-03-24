package com.springddd.application.service.gen;

import com.springddd.domain.gen.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GenProjectInfoDomainFactoryTest {

    private final GenProjectInfoDomainFactoryImpl factory = new GenProjectInfoDomainFactoryImpl();

    @Test
    void newInstance_shouldCreateDomainWithCorrectValues() {
        ProjectInfo projectInfo = new ProjectInfo("table_name", "com.example", "ClassName", "module", "project");
        GenProjectInfoExtendInfo extendInfo = new GenProjectInfoExtendInfo("requestName");

        GenProjectInfoDomain domain = factory.newInstance(projectInfo, extendInfo);

        assertNotNull(domain);
        assertEquals(projectInfo, domain.getProjectInfo());
        assertEquals(extendInfo, domain.getExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }
}
