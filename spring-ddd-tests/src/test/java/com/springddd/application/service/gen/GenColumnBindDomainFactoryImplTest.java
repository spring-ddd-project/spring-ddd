package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenColumnBindBasicInfo;
import com.springddd.domain.gen.GenColumnBindDomain;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenColumnBindDomainFactoryImplTest {

    private final GenColumnBindDomainFactoryImpl factory = new GenColumnBindDomainFactoryImpl();

    @Test
    void newInstance_shouldCreateDomain_withValidBasicInfo() {
        GenColumnBindBasicInfo basicInfo = new GenColumnBindBasicInfo("varchar", "String", (byte) 1, (byte) 1);

        GenColumnBindDomain domain = factory.newInstance(basicInfo);

        assertNotNull(domain);
        assertEquals(basicInfo, domain.getBasicInfo());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void newInstance_shouldSetDeleteStatusToFalse() {
        GenColumnBindBasicInfo basicInfo = new GenColumnBindBasicInfo("varchar", "String", (byte) 1, (byte) 1);

        GenColumnBindDomain domain = factory.newInstance(basicInfo);

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void newInstance_shouldThrowException_whenColumnTypeIsNull() {
        assertThrows(Exception.class, () -> {
            new GenColumnBindBasicInfo(null, "String", (byte) 1, (byte) 1);
        });
    }

    @Test
    void newInstance_shouldThrowException_whenEntityTypeIsNull() {
        assertThrows(Exception.class, () -> {
            new GenColumnBindBasicInfo("varchar", null, (byte) 1, (byte) 1);
        });
    }

    @Test
    void newInstance_shouldThrowException_whenComponentTypeIsNull() {
        assertThrows(Exception.class, () -> {
            new GenColumnBindBasicInfo("varchar", "String", null, (byte) 1);
        });
    }

    @Test
    void newInstance_shouldThrowException_whenTypescriptTypeIsNull() {
        assertThrows(Exception.class, () -> {
            new GenColumnBindBasicInfo("varchar", "String", (byte) 1, null);
        });
    }
}
