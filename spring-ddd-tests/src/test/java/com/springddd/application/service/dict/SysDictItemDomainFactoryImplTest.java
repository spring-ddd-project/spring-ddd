package com.springddd.application.service.dict;

import com.springddd.domain.dict.DictId;
import com.springddd.domain.dict.DictItemBasicInfo;
import com.springddd.domain.dict.DictItemExtendInfo;
import com.springddd.domain.dict.SysDictItemDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysDictItemDomainFactoryImplTest {

    private final SysDictItemDomainFactoryImpl factory = new SysDictItemDomainFactoryImpl();

    @Test
    @DisplayName("should create SysDictItemDomain with correct fields set")
    void newInstance() {
        DictId dictId = new DictId(1L);
        DictItemBasicInfo basicInfo = new DictItemBasicInfo("Label", 1, true);
        DictItemExtendInfo extendInfo = new DictItemExtendInfo(1, true);

        SysDictItemDomain domain = factory.newInstance(dictId, basicInfo, extendInfo);

        assertNotNull(domain);
        assertEquals(dictId, domain.getDictId());
        assertEquals(basicInfo, domain.getItemBasicInfo());
        assertEquals(extendInfo, domain.getItemExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }
}
