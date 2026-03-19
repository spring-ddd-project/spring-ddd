package com.springddd.domain.dict;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SysDictDomainTest {

    @Test
    void create_shouldInitializeSuccessfully() {
        SysDictDomain domain = new SysDictDomain();
        domain.create();
        assertNotNull(domain);
    }

    @Test
    void update_shouldUpdateBasicInfoAndExtendInfo() {
        SysDictDomain domain = new SysDictDomain();
        DictBasicInfo basicInfo = new DictBasicInfo("testDict", "TEST_DICT");
        DictExtendInfo extendInfo = new DictExtendInfo(1, true);

        domain.update(basicInfo, extendInfo);

        assertEquals(basicInfo, domain.getDictBasicInfo());
        assertEquals(extendInfo, domain.getDictExtendInfo());
    }

    @Test
    void delete_shouldSetDeleteStatusToTrue() {
        SysDictDomain domain = new SysDictDomain();
        assertNull(domain.getDeleteStatus());

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void restore_shouldSetDeleteStatusToFalse() {
        SysDictDomain domain = new SysDictDomain();
        domain.delete();
        assertTrue(domain.getDeleteStatus());

        domain.restore();

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void update_withNullBasicInfo_shouldSetToNull() {
        SysDictDomain domain = new SysDictDomain();
        domain.setDictBasicInfo(new DictBasicInfo("testDict", "TEST_DICT"));

        domain.update(null, null);

        assertNull(domain.getDictBasicInfo());
        assertNull(domain.getDictExtendInfo());
    }
}
