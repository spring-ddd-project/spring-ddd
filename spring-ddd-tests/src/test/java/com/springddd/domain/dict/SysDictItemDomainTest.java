package com.springddd.domain.dict;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SysDictItemDomainTest {

    @Test
    void create_shouldInitializeSuccessfully() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.create();
        assertNotNull(domain);
    }

    @Test
    void update_shouldUpdateBasicInfoAndExtendInfo() {
        SysDictItemDomain domain = new SysDictItemDomain();
        DictItemBasicInfo basicInfo = new DictItemBasicInfo("label", 1);
        DictItemExtendInfo extendInfo = new DictItemExtendInfo(1, true);

        domain.update(basicInfo, extendInfo);

        assertEquals(basicInfo, domain.getItemBasicInfo());
        assertEquals(extendInfo, domain.getItemExtendInfo());
    }

    @Test
    void delete_shouldSetDeleteStatusToTrue() {
        SysDictItemDomain domain = new SysDictItemDomain();
        assertNull(domain.getDeleteStatus());

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void restore_shouldSetDeleteStatusToFalse() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.delete();
        assertTrue(domain.getDeleteStatus());

        domain.restore();

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void update_withNullValues_shouldSetToNull() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemBasicInfo(new DictItemBasicInfo("label", 1));

        domain.update(null, null);

        assertNull(domain.getItemBasicInfo());
        assertNull(domain.getItemExtendInfo());
    }
}
