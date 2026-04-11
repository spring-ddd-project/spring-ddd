package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class GenColumnBindDomainTest {

    @Test
    void shouldCreateGenColumnBindDomainWithAllFields() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        ColumnBindId bindId = new ColumnBindId(1L);
        GenColumnBindBasicInfo basicInfo = new GenColumnBindBasicInfo("varchar", "String", (byte) 1, (byte) 1);

        domain.setBindId(bindId);
        domain.setBasicInfo(basicInfo);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setDeleteStatus(false);
        domain.setVersion(0);

        assertEquals(bindId, domain.getBindId());
        assertEquals(basicInfo, domain.getBasicInfo());
        assertEquals("admin", domain.getCreateBy());
        assertNotNull(domain.getCreateTime());
        assertEquals("admin", domain.getUpdateBy());
        assertNotNull(domain.getUpdateTime());
        assertFalse(domain.getDeleteStatus());
        assertEquals(0, domain.getVersion());
    }

    @Test
    void shouldCallCreateMethod() {
        GenColumnBindDomain domain = new GenColumnBindDomain();

        domain.create();

        assertNotNull(domain);
    }

    @Test
    void shouldUpdateGenColumnBindDomain() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        GenColumnBindBasicInfo newBasicInfo = new GenColumnBindBasicInfo("int", "Integer", (byte) 2, (byte) 2);

        domain.update(newBasicInfo);

        assertEquals(newBasicInfo, domain.getBasicInfo());
    }

    @Test
    void shouldDeleteGenColumnBindDomain() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        domain.setDeleteStatus(false);

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void shouldRestoreGenColumnBindDomain() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        domain.setDeleteStatus(true);

        domain.restore();

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldSetAndGetFields() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        ColumnBindId bindId = new ColumnBindId(10L);
        GenColumnBindBasicInfo basicInfo = new GenColumnBindBasicInfo("varchar", "String", (byte) 1, (byte) 1);

        domain.setBindId(bindId);
        domain.setBasicInfo(basicInfo);

        assertEquals(bindId, domain.getBindId());
        assertEquals(basicInfo, domain.getBasicInfo());
    }

    @Test
    void shouldHandleNullValues() {
        GenColumnBindDomain domain = new GenColumnBindDomain();

        domain.setBindId(null);
        domain.setBasicInfo(null);

        assertNull(domain.getBindId());
        assertNull(domain.getBasicInfo());
    }
}
