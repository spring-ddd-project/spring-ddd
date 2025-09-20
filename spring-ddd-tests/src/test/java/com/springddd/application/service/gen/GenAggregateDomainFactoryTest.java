package com.springddd.application.service.gen;

import com.springddd.domain.gen.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GenAggregateDomainFactoryTest {

    private final GenAggregateDomainFactoryImpl factory = new GenAggregateDomainFactoryImpl();

    @Test
    void newInstance_shouldCreateDomainWithCorrectValues() {
        InfoId infoId = new InfoId(1L);
        GenAggregateValueObject valueObject = new GenAggregateValueObject("testObject", "value", (byte) 1);
        GenAggregateExtendInfo extendInfo = new GenAggregateExtendInfo(false);

        GenAggregateDomain domain = factory.newInstance(infoId, valueObject, extendInfo);

        assertNotNull(domain);
        assertEquals(infoId, domain.getInfoId());
        assertEquals(valueObject, domain.getValueObject());
        assertEquals(extendInfo, domain.getExtendInfo());
    }
}
