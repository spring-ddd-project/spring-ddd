package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenAggregateDomain;
import com.springddd.domain.gen.GenAggregateExtendInfo;
import com.springddd.domain.gen.GenAggregateValueObject;
import com.springddd.domain.gen.InfoId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenAggregateDomainFactoryImplTest {

    private final GenAggregateDomainFactoryImpl factory = new GenAggregateDomainFactoryImpl();

    @Test
    @DisplayName("should create GenAggregateDomain with correct fields set")
    void newInstance() {
        InfoId infoId = new InfoId(1L);
        GenAggregateValueObject valueObject = new GenAggregateValueObject("ObjectName", "ObjectValue", (byte) 1);
        GenAggregateExtendInfo extendInfo = new GenAggregateExtendInfo(true);

        GenAggregateDomain domain = factory.newInstance(infoId, valueObject, extendInfo);

        assertNotNull(domain);
        assertEquals(infoId, domain.getInfoId());
        assertEquals(valueObject, domain.getValueObject());
        assertEquals(extendInfo, domain.getExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }
}
