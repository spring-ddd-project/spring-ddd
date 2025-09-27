package com.springddd.domain.dict;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysDictDomainTest {

    @Test
    void shouldCreateSysDictDomain() {
        SysDictDomain domain = new SysDictDomain();
        assertNotNull(domain);
    }

    @Test
    void shouldSetAndGetId() {
        SysDictDomain domain = new SysDictDomain();
        DictId id = new DictId(1L);
        domain.setDictId(id);
        assertEquals(id, domain.getDictId());
    }

    @Test
    void shouldSetAndGetBasicInfo() {
        SysDictDomain domain = new SysDictDomain();
        DictBasicInfo basicInfo = new DictBasicInfo("dictName", "dictCode");
        domain.setDictBasicInfo(basicInfo);
        assertEquals(basicInfo, domain.getDictBasicInfo());
    }

    @Test
    void shouldSetAndGetExtendInfo() {
        SysDictDomain domain = new SysDictDomain();
        DictExtendInfo extendInfo = new DictExtendInfo(1, true);
        domain.setDictExtendInfo(extendInfo);
        assertEquals(extendInfo, domain.getDictExtendInfo());
    }

    @Test
    void shouldCallCreate() {
        SysDictDomain domain = new SysDictDomain();
        domain.create();
        assertNotNull(domain);
    }

    @Test
    void shouldSetDeleteStatusOnDelete() {
        SysDictDomain domain = new SysDictDomain();
        domain.setDeleteStatus(false);
        domain.delete();
        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void shouldClearDeleteStatusOnRestore() {
        SysDictDomain domain = new SysDictDomain();
        domain.setDeleteStatus(true);
        domain.restore();
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldSetAndGetDeleteStatus() {
        SysDictDomain domain = new SysDictDomain();
        domain.setDeleteStatus(true);
        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        SysDictDomain domain = new SysDictDomain();
        String str = domain.toString();
        assertTrue(str.contains("SysDictDomain"));
    }
}
