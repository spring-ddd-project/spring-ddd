package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GenProjectInfoDomainTest {

    @Test
    void create_shouldInitializeSuccessfully() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.create();
        assertNotNull(domain);
    }

    @Test
    void update_shouldUpdateAllFields() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        ProjectInfo projectInfo = new ProjectInfo("table_name", "com.example", "ClassName", "module", "project");
        GenProjectInfoExtendInfo extendInfo = new GenProjectInfoExtendInfo("requestName");

        domain.update(projectInfo, extendInfo);

        assertEquals(projectInfo, domain.getProjectInfo());
        assertEquals(extendInfo, domain.getExtendInfo());
    }

    @Test
    void delete_shouldSetDeleteStatusToTrue() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        assertNull(domain.getDeleteStatus());

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }
}
