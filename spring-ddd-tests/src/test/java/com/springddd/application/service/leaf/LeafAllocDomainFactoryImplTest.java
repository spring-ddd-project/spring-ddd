package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeafAllocDomainFactoryImplTest {

    @Test
    void shouldCreateNewInstance() {
        LeafAllocDomainFactoryImpl factory = new LeafAllocDomainFactoryImpl();

        LeafAllocDomain domain = factory.newInstance(
                new BizTag("test"),
                new MaxId(1000L),
                new Step(100),
                new Description("desc"),
                1L
        );

        assertNotNull(domain);
        assertEquals("test", domain.getBizTag().value());
        assertEquals(1000L, domain.getMaxId().value());
        assertEquals(100, domain.getStep().value());
        assertEquals("desc", domain.getDescription().value());
        assertEquals(1L, domain.getDeptId());
        assertFalse(domain.getDeleteStatus());
        assertNotNull(domain.getState());
    }
}
