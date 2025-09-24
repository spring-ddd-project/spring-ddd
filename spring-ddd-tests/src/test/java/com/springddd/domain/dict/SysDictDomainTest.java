package com.springddd.domain.dict;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysDictDomainTest {

    @Test
    void create_shouldInitializeDomain() {
        SysDictDomain domain = new SysDictDomain();
        domain.setId(new DictId(1L));
        domain.setDictBasicInfo(new DictBasicInfo("dictCode", "字典名称"));
        domain.create();
        assertNotNull(domain);
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void update_shouldModifyDomain() {
        SysDictDomain domain = new SysDictDomain();
        domain.setId(new DictId(1L));

        DictBasicInfo newBasicInfo = new DictBasicInfo("newCode", "新名称");
        DictExtendInfo newExtendInfo = new DictExtendInfo(1, true);

        domain.update(newBasicInfo, newExtendInfo);

        assertEquals(newBasicInfo, domain.getDictBasicInfo());
        assertEquals(newExtendInfo, domain.getDictExtendInfo());
    }

    @Test
    void delete_shouldSetDeleteStatus() {
        SysDictDomain domain = new SysDictDomain();
        assertFalse(domain.getDeleteStatus());

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void restore_shouldClearDeleteStatus() {
        SysDictDomain domain = new SysDictDomain();
        domain.delete();
        assertTrue(domain.getDeleteStatus());

        domain.restore();

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void setId_and_getId_shouldWork() {
        SysDictDomain domain = new SysDictDomain();
        DictId id = new DictId(100L);
        domain.setId(id);
        assertEquals(id, domain.getId());
    }

    @Test
    void setDictBasicInfo_and_getDictBasicInfo_shouldWork() {
        SysDictDomain domain = new SysDictDomain();
        DictBasicInfo basicInfo = new DictBasicInfo("code", "名称");
        domain.setDictBasicInfo(basicInfo);
        assertEquals(basicInfo, domain.getDictBasicInfo());
    }

    @Test
    void setDictExtendInfo_and_getDictExtendInfo_shouldWork() {
        SysDictDomain domain = new SysDictDomain();
        DictExtendInfo extendInfo = new DictExtendInfo(1, true);
        domain.setDictExtendInfo(extendInfo);
        assertEquals(extendInfo, domain.getDictExtendInfo());
    }
}
