package com.springddd.application.service.gen;

import com.springddd.application.service.gen.GenColumnBindDomainFactoryImpl;
import com.springddd.domain.gen.GenColumnBindBasicInfo;
import com.springddd.domain.gen.GenColumnBindDomain;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenColumnBindDomainFactoryImplTest {

    private final GenColumnBindDomainFactoryImpl factory = new GenColumnBindDomainFactoryImpl();

    @Test
    void shouldCreateGenColumnBindDomainWithBasicInfo() {
        GenColumnBindBasicInfo basicInfo = new GenColumnBindBasicInfo("varchar", "String", (byte) 1, (byte) 1);

        GenColumnBindDomain domain = factory.newInstance(basicInfo);

        assertNotNull(domain);
        assertEquals(basicInfo, domain.getBasicInfo());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldCreateGenColumnBindDomainWithNullBasicInfo() {
        GenColumnBindDomain domain = factory.newInstance(null);

        assertNotNull(domain);
        assertNull(domain.getBasicInfo());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldSetDeleteStatusToFalse() {
        GenColumnBindBasicInfo basicInfo = new GenColumnBindBasicInfo("int", "Integer", (byte) 2, (byte) 2);

        GenColumnBindDomain domain = factory.newInstance(basicInfo);

        assertFalse(domain.getDeleteStatus());
    }
}
