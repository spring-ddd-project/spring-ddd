package com.springddd.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AbstractDomainMaskTest {

    @Test
    void shouldSetAndGetDeleteStatus() {
        AbstractDomainMask mask = new AbstractDomainMask() {};
        mask.setDeleteStatus(true);
        assertTrue(mask.getDeleteStatus());
    }

    @Test
    void shouldSetAndGetCreateBy() {
        AbstractDomainMask mask = new AbstractDomainMask() {};
        mask.setCreateBy("admin");
        assertEquals("admin", mask.getCreateBy());
    }

    @Test
    void shouldSetAndGetUpdateBy() {
        AbstractDomainMask mask = new AbstractDomainMask() {};
        mask.setUpdateBy("admin");
        assertEquals("admin", mask.getUpdateBy());
    }

    @Test
    void shouldSetAndGetDeptId() {
        AbstractDomainMask mask = new AbstractDomainMask() {};
        mask.setDeptId(1L);
        assertEquals(1L, mask.getDeptId());
    }

    @Test
    void shouldSetAndGetVersion() {
        AbstractDomainMask mask = new AbstractDomainMask() {};
        mask.setVersion(1);
        assertEquals(1, mask.getVersion());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        AbstractDomainMask mask = new AbstractDomainMask() {};
        String str = mask.toString();
        assertTrue(str.contains("AbstractDomainMask"));
    }
}
