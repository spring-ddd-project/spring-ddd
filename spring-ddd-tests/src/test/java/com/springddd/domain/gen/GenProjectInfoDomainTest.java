package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class GenProjectInfoDomainTest {

    @Test
    void shouldCreateGenProjectInfoDomainWithAllFields() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        InfoId infoId = new InfoId(1L);
        ProjectInfo projectInfo = new ProjectInfo("table", "com.example", "ClassName", "module", "project");
        GenProjectInfoExtendInfo extendInfo = new GenProjectInfoExtendInfo("requestName");

        domain.setId(infoId);
        domain.setProjectInfo(projectInfo);
        domain.setExtendInfo(extendInfo);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setDeleteStatus(false);
        domain.setVersion(0);

        assertEquals(infoId, domain.getId());
        assertEquals(projectInfo, domain.getProjectInfo());
        assertEquals(extendInfo, domain.getExtendInfo());
        assertEquals("admin", domain.getCreateBy());
        assertNotNull(domain.getCreateTime());
        assertEquals("admin", domain.getUpdateBy());
        assertNotNull(domain.getUpdateTime());
        assertFalse(domain.getDeleteStatus());
        assertEquals(0, domain.getVersion());
    }

    @Test
    void shouldCallCreateMethod() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();

        domain.create();

        assertNotNull(domain);
    }

    @Test
    void shouldUpdateGenProjectInfoDomain() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        ProjectInfo newProjectInfo = new ProjectInfo("newTable", "com.new", "NewClass", "newModule", "newProject");
        GenProjectInfoExtendInfo newExtendInfo = new GenProjectInfoExtendInfo("newRequest");

        domain.update(newProjectInfo, newExtendInfo);

        assertEquals(newProjectInfo, domain.getProjectInfo());
        assertEquals(newExtendInfo, domain.getExtendInfo());
    }

    @Test
    void shouldDeleteGenProjectInfoDomain() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.setDeleteStatus(false);

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void shouldSetAndGetFields() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        InfoId infoId = new InfoId(10L);
        ProjectInfo projectInfo = new ProjectInfo("table", "com.example", "ClassName", "module", "project");
        GenProjectInfoExtendInfo extendInfo = new GenProjectInfoExtendInfo("requestName");

        domain.setId(infoId);
        domain.setProjectInfo(projectInfo);
        domain.setExtendInfo(extendInfo);

        assertEquals(infoId, domain.getId());
        assertEquals(projectInfo, domain.getProjectInfo());
        assertEquals(extendInfo, domain.getExtendInfo());
    }

    @Test
    void shouldHandleNullValues() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();

        domain.setId(null);
        domain.setProjectInfo(null);
        domain.setExtendInfo(null);

        assertNull(domain.getId());
        assertNull(domain.getProjectInfo());
        assertNull(domain.getExtendInfo());
    }

    @Test
    void shouldDeleteWhenStateIsNullAndDeleteStatusFalse() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.setDeleteStatus(false);

        domain.delete();

        assertTrue(domain.getDeleteStatus());
        assertNotNull(domain.getState());
    }

    @Test
    void shouldDeleteWhenStateIsNullAndDeleteStatusTrue() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.setDeleteStatus(true);

        domain.delete();

        assertTrue(domain.getDeleteStatus());
        assertNotNull(domain.getState());
    }

    @Test
    void shouldDeleteWhenStateExists() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.setState(new com.springddd.domain.gen.state.ActiveProjectState());

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void shouldCloneWithNullFields() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.setId(null);
        domain.setProjectInfo(null);
        domain.setExtendInfo(null);

        GenProjectInfoDomain clone = domain.clone();

        assertNotNull(clone);
        assertNull(clone.getId());
        assertNull(clone.getProjectInfo());
        assertNull(clone.getExtendInfo());
    }

    @Test
    void shouldCloneWithAllFields() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.setId(new InfoId(1L));
        domain.setProjectInfo(new ProjectInfo("table", "com.example", "ClassName", "module", "project"));
        domain.setExtendInfo(new GenProjectInfoExtendInfo("requestName", "1.0"));

        GenProjectInfoDomain clone = domain.clone();

        assertNotNull(clone);
        assertEquals(1L, clone.getId().value());
        assertEquals("table", clone.getProjectInfo().tableName());
        assertEquals("requestName", clone.getExtendInfo().requestName());
    }

    @Test
    void shouldHandleCloneNotSupportedException() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain() {
            @Override
            protected Object doClone() throws CloneNotSupportedException {
                throw new CloneNotSupportedException("test");
            }
        };
        assertThrows(AssertionError.class, domain::clone);
    }
}
