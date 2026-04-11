package com.springddd.application.service.gen;

import com.springddd.domain.gen.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenProjectInfoDomainFactoryImplTest {

    private final GenProjectInfoDomainFactoryImpl factory = new GenProjectInfoDomainFactoryImpl();

    @Test
    void shouldCreateGenProjectInfoDomainWithAllFields() {
        ProjectInfo projectInfo = new ProjectInfo("table", "com.example", "ClassName", "module", "project");
        GenProjectInfoExtendInfo extendInfo = new GenProjectInfoExtendInfo("requestName");

        GenProjectInfoDomain domain = factory.newInstance(projectInfo, extendInfo);

        assertNotNull(domain);
        assertEquals(projectInfo, domain.getProjectInfo());
        assertEquals(extendInfo, domain.getExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldCreateGenProjectInfoDomainWithNullExtendInfo() {
        ProjectInfo projectInfo = new ProjectInfo("table", "com.example", "ClassName", "module", "project");

        GenProjectInfoDomain domain = factory.newInstance(projectInfo, null);

        assertNotNull(domain);
        assertEquals(projectInfo, domain.getProjectInfo());
        assertNull(domain.getExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldSetDeleteStatusToFalse() {
        ProjectInfo projectInfo = new ProjectInfo("newTable", "com.new", "NewClass", "newModule", "newProject");
        GenProjectInfoExtendInfo extendInfo = new GenProjectInfoExtendInfo("newRequest");

        GenProjectInfoDomain domain = factory.newInstance(projectInfo, extendInfo);

        assertFalse(domain.getDeleteStatus());
    }
}
