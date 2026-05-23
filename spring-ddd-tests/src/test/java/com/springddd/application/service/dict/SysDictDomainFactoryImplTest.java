package com.springddd.application.service.dict;

import com.springddd.domain.dict.DictBasicInfo;
import com.springddd.domain.dict.DictExtendInfo;
import com.springddd.domain.dict.SysDictDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysDictDomainFactoryImplTest {

    private final SysDictDomainFactoryImpl factory = new SysDictDomainFactoryImpl();

    @Test
    @DisplayName("should create SysDictDomain with correct fields set")
    void newInstance() {
        DictBasicInfo basicInfo = new DictBasicInfo("Test Dict", "test_dict");
        DictExtendInfo extendInfo = new DictExtendInfo(1, true);

        SysDictDomain domain = factory.newInstance(basicInfo, extendInfo);

        assertNotNull(domain);
        assertEquals(basicInfo, domain.getDictBasicInfo());
        assertEquals(extendInfo, domain.getDictExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }
}
