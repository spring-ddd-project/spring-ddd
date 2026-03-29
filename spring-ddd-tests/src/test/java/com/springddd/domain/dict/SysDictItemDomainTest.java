package com.springddd.domain.dict;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysDictItemDomainTest {

    @Test
    void create_shouldInitializeDomain() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setId(new DictItemId(1L));
        domain.setDictItemBasicInfo(new DictItemBasicInfo(1L, "标签", "值"));
        domain.create();
        assertNotNull(domain);
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void update_shouldModifyDomain() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setId(new DictItemId(1L));

        DictItemBasicInfo newBasicInfo = new DictItemBasicInfo(1L, "新标签", "新值");
        DictItemExtendInfo newExtendInfo = new DictItemExtendInfo(1, true);

        domain.update(newBasicInfo, newExtendInfo);

        assertEquals(newBasicInfo, domain.getDictItemBasicInfo());
        assertEquals(newExtendInfo, domain.getDictItemExtendInfo());
    }

    @Test
    void delete_shouldSetDeleteStatus() {
        SysDictItemDomain domain = new SysDictItemDomain();
        assertFalse(domain.getDeleteStatus());

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void restore_shouldClearDeleteStatus() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.delete();
        assertTrue(domain.getDeleteStatus());

        domain.restore();

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void setId_and_getId_shouldWork() {
        SysDictItemDomain domain = new SysDictItemDomain();
        DictItemId id = new DictItemId(100L);
        domain.setId(id);
        assertEquals(id, domain.getId());
    }

    @Test
    void setDictItemBasicInfo_and_getDictItemBasicInfo_shouldWork() {
        SysDictItemDomain domain = new SysDictItemDomain();
        DictItemBasicInfo basicInfo = new DictItemBasicInfo(1L, "标签", "值");
        domain.setDictItemBasicInfo(basicInfo);
        assertEquals(basicInfo, domain.getDictItemBasicInfo());
    }

    @Test
    void setDictItemExtendInfo_and_getDictItemExtendInfo_shouldWork() {
        SysDictItemDomain domain = new SysDictItemDomain();
        DictItemExtendInfo extendInfo = new DictItemExtendInfo(1, true);
        domain.setDictItemExtendInfo(extendInfo);
        assertEquals(extendInfo, domain.getDictItemExtendInfo());
    }
}
