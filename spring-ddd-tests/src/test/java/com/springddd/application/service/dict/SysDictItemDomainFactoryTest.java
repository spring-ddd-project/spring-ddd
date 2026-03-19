package com.springddd.application.service.dict;

import com.springddd.domain.dict.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SysDictItemDomainFactoryTest {

    private final SysDictItemDomainFactoryImpl factory = new SysDictItemDomainFactoryImpl();

    @Test
    void newInstance_shouldCreateDomainWithCorrectValues() {
        DictId dictId = new DictId(1L);
        DictItemBasicInfo basicInfo = new DictItemBasicInfo("Test Item", 1);
        DictItemExtendInfo extendInfo = new DictItemExtendInfo(1, true);

        SysDictItemDomain domain = factory.newInstance(dictId, basicInfo, extendInfo);

        assertNotNull(domain);
        assertEquals(dictId, domain.getDictId());
        assertEquals(basicInfo, domain.getItemBasicInfo());
        assertEquals(extendInfo, domain.getItemExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void newInstance_shouldSetDeleteStatusToFalse() {
        DictId dictId = new DictId(1L);
        DictItemBasicInfo basicInfo = new DictItemBasicInfo("Test Item", 1);
        DictItemExtendInfo extendInfo = new DictItemExtendInfo(1, true);

        SysDictItemDomain domain = factory.newInstance(dictId, basicInfo, extendInfo);

        assertFalse(domain.getDeleteStatus());
    }
}
