package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenColumnBindBasicInfo;
import com.springddd.domain.gen.GenColumnBindDomain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GenColumnBindDomainFactoryTest {

    private final GenColumnBindDomainFactoryImpl factory = new GenColumnBindDomainFactoryImpl();

    @Test
    void newInstance_shouldCreateDomainWithCorrectValues() {
        GenColumnBindBasicInfo basicInfo = new GenColumnBindBasicInfo("varchar", "String", (byte) 1, (byte) 1);

        GenColumnBindDomain domain = factory.newInstance(basicInfo);

        assertNotNull(domain);
        assertEquals(basicInfo, domain.getBasicInfo());
        assertFalse(domain.getDeleteStatus());
    }
}
