package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenColumnBindBasicInfo;
import com.springddd.domain.gen.GenColumnBindDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenColumnBindDomainFactoryImplTest {

    private final GenColumnBindDomainFactoryImpl factory = new GenColumnBindDomainFactoryImpl();

    @Test
    @DisplayName("should create GenColumnBindDomain with correct fields set")
    void newInstance() {
        GenColumnBindBasicInfo basicInfo = new GenColumnBindBasicInfo("varchar", "String", (byte) 1, (byte) 1);

        GenColumnBindDomain domain = factory.newInstance(basicInfo);

        assertNotNull(domain);
        assertEquals(basicInfo, domain.getBasicInfo());
        assertFalse(domain.getDeleteStatus());
    }
}
