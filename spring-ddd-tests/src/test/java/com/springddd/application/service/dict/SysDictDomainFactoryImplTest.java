package com.springddd.application.service.dict;

import com.springddd.domain.dict.DictBasicInfo;
import com.springddd.domain.dict.DictExtendInfo;
import com.springddd.domain.dict.SysDictDomain;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysDictDomainFactoryImplTest {

    @Test
    void shouldCreateNewInstance() {
        SysDictDomainFactoryImpl factory = new SysDictDomainFactoryImpl();

        DictBasicInfo basicInfo = new DictBasicInfo("DictName", "DICT_CODE");
        DictExtendInfo extendInfo = new DictExtendInfo(1, true);

        SysDictDomain domain = factory.newInstance(basicInfo, extendInfo);

        assertNotNull(domain);
        assertEquals(basicInfo, domain.getDictBasicInfo());
        assertEquals(extendInfo, domain.getDictExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }
}
