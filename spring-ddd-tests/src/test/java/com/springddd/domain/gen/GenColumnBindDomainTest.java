package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GenColumnBindDomainTest {

    @Test
    void create_shouldInitializeSuccessfully() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        domain.create();
        assertNotNull(domain);
    }

    @Test
    void update_shouldUpdateBasicInfo() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        GenColumnBindBasicInfo basicInfo = new GenColumnBindBasicInfo("varchar", "String", (byte) 1, (byte) 1);

        domain.update(basicInfo);

        assertEquals(basicInfo, domain.getBasicInfo());
    }

    @Test
    void delete_shouldSetDeleteStatusToTrue() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        assertNull(domain.getDeleteStatus());

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void restore_shouldSetDeleteStatusToFalse() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        domain.delete();
        assertTrue(domain.getDeleteStatus());

        domain.restore();

        assertFalse(domain.getDeleteStatus());
    }
}
