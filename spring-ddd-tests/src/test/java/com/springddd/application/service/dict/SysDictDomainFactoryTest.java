package com.springddd.application.service.dict;

import com.springddd.domain.dict.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SysDictDomainFactoryTest {

    private final SysDictDomainFactoryImpl factory = new SysDictDomainFactoryImpl();

    @Test
    void newInstance_shouldCreateDomainWithCorrectValues() {
        DictBasicInfo basicInfo = new DictBasicInfo("Test Dictionary", "TEST_DICT");
        DictExtendInfo extendInfo = new DictExtendInfo(1, true);

        SysDictDomain domain = factory.newInstance(basicInfo, extendInfo);

        assertNotNull(domain);
        assertEquals(basicInfo, domain.getDictBasicInfo());
        assertEquals(extendInfo, domain.getDictExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void newInstance_shouldSetDeleteStatusToFalse() {
        DictBasicInfo basicInfo = new DictBasicInfo("Test Dictionary", "TEST_DICT");
        DictExtendInfo extendInfo = new DictExtendInfo(1, true);

        SysDictDomain domain = factory.newInstance(basicInfo, extendInfo);

        assertFalse(domain.getDeleteStatus());
    }
}
