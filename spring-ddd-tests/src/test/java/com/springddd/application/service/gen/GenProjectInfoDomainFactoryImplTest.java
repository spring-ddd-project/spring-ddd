package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenProjectInfoDomain;
import com.springddd.domain.gen.GenProjectInfoExtendInfo;
import com.springddd.domain.gen.ProjectInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenProjectInfoDomainFactoryImplTest {

    private final GenProjectInfoDomainFactoryImpl factory = new GenProjectInfoDomainFactoryImpl();

    @Test
    @DisplayName("should create GenProjectInfoDomain with correct fields set")
    void newInstance() {
        ProjectInfo projectInfo = new ProjectInfo("sys_user", "com.springddd", "SysUser", "user", "spring-ddd");
        GenProjectInfoExtendInfo extendInfo = new GenProjectInfoExtendInfo("sys-user", "1.0");

        GenProjectInfoDomain domain = factory.newInstance(projectInfo, extendInfo);

        assertNotNull(domain);
        assertEquals(projectInfo, domain.getProjectInfo());
        assertEquals(extendInfo, domain.getExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }
}
