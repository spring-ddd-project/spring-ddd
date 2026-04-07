package com.springddd.domain.dict;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysDictItemDomainTest {

    @Test
    void shouldCreateSysDictItemDomain() {
        SysDictItemDomain domain = new SysDictItemDomain();
        assertNotNull(domain);
    }

    @Test
    void shouldSetAndGetId() {
        SysDictItemDomain domain = new SysDictItemDomain();
        DictItemId id = new DictItemId(1L);
        domain.setItemId(id);
        assertEquals(id, domain.getItemId());
    }

    @Test
    void shouldSetAndGetDictId() {
        SysDictItemDomain domain = new SysDictItemDomain();
        DictId dictId = new DictId(1L);
        domain.setDictId(dictId);
        assertEquals(dictId, domain.getDictId());
    }

    @Test
    void shouldSetAndGetBasicInfo() {
        SysDictItemDomain domain = new SysDictItemDomain();
        DictItemBasicInfo basicInfo = new DictItemBasicInfo("label", 1);
        domain.setItemBasicInfo(basicInfo);
        assertEquals(basicInfo, domain.getItemBasicInfo());
    }

    @Test
    void shouldSetAndGetExtendInfo() {
        SysDictItemDomain domain = new SysDictItemDomain();
        DictItemExtendInfo extendInfo = new DictItemExtendInfo(1, true);
        domain.setItemExtendInfo(extendInfo);
        assertEquals(extendInfo, domain.getItemExtendInfo());
    }

    @Test
    void shouldSetDeleteStatusOnDelete() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setDeleteStatus(false);
        domain.delete();
        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void shouldClearDeleteStatusOnRestore() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setDeleteStatus(true);
        domain.restore();
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        SysDictItemDomain domain = new SysDictItemDomain();
        String str = domain.toString();
        assertTrue(str.contains("SysDictItemDomain"));
    }
}
