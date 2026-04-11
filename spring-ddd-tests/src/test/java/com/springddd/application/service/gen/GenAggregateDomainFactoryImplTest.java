package com.springddd.application.service.gen;

import com.springddd.domain.gen.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenAggregateDomainFactoryImplTest {

    private final GenAggregateDomainFactoryImpl factory = new GenAggregateDomainFactoryImpl();

    @Test
    void shouldCreateGenAggregateDomainWithAllFields() {
        InfoId infoId = new InfoId(1L);
        GenAggregateValueObject valueObject = new GenAggregateValueObject("name", "value", (byte) 1);
        GenAggregateExtendInfo extendInfo = new GenAggregateExtendInfo(true);

        GenAggregateDomain domain = factory.newInstance(infoId, valueObject, extendInfo);

        assertNotNull(domain);
        assertEquals(infoId, domain.getInfoId());
        assertEquals(valueObject, domain.getValueObject());
        assertEquals(extendInfo, domain.getExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldCreateGenAggregateDomainWithNullExtendInfo() {
        InfoId infoId = new InfoId(2L);
        GenAggregateValueObject valueObject = new GenAggregateValueObject("testName", "testValue", (byte) 0);

        GenAggregateDomain domain = factory.newInstance(infoId, valueObject, null);

        assertNotNull(domain);
        assertEquals(infoId, domain.getInfoId());
        assertEquals(valueObject, domain.getValueObject());
        assertNull(domain.getExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldSetDeleteStatusToFalse() {
        InfoId infoId = new InfoId(3L);
        GenAggregateValueObject valueObject = new GenAggregateValueObject("name", "value", (byte) 1);
        GenAggregateExtendInfo extendInfo = new GenAggregateExtendInfo(false);

        GenAggregateDomain domain = factory.newInstance(infoId, valueObject, extendInfo);

        assertFalse(domain.getDeleteStatus());
    }
}
