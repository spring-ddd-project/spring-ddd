package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenAggregateDomain;
import com.springddd.domain.gen.GenAggregateExtendInfo;
import com.springddd.domain.gen.GenAggregateValueObject;
import com.springddd.domain.gen.InfoId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenAggregateDomainFactoryImplTest {

    private final GenAggregateDomainFactoryImpl factory = new GenAggregateDomainFactoryImpl();

    @Test
    void newInstance_shouldCreateGenAggregateDomainWithAllFields() {
        InfoId infoId = new InfoId(1L);
        GenAggregateValueObject valueObject = new GenAggregateValueObject("testObject", "testValue", (byte) 1);
        GenAggregateExtendInfo extendInfo = new GenAggregateExtendInfo(true);

        GenAggregateDomain domain = factory.newInstance(infoId, valueObject, extendInfo);

        assertNotNull(domain);
        assertEquals(infoId, domain.getInfoId());
        assertEquals(valueObject, domain.getValueObject());
        assertEquals(extendInfo, domain.getExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void newInstance_shouldCreateDomainWithNullExtendInfo() {
        InfoId infoId = new InfoId(2L);
        GenAggregateValueObject valueObject = new GenAggregateValueObject("objectName", "objectValue", (byte) 2);

        GenAggregateDomain domain = factory.newInstance(infoId, valueObject, null);

        assertNotNull(domain);
        assertEquals(infoId, domain.getInfoId());
        assertEquals(valueObject, domain.getValueObject());
        assertNull(domain.getExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void newInstance_shouldSetDeleteStatusToFalse() {
        InfoId infoId = new InfoId(3L);
        GenAggregateValueObject valueObject = new GenAggregateValueObject("name", "value", (byte) 1);
        GenAggregateExtendInfo extendInfo = new GenAggregateExtendInfo(false);

        GenAggregateDomain domain = factory.newInstance(infoId, valueObject, extendInfo);

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void newInstance_shouldCreateDomainWithDifferentObjectTypes() {
        InfoId infoId = new InfoId(4L);
        GenAggregateValueObject valueObject1 = new GenAggregateValueObject("type1", "value1", (byte) 0);
        GenAggregateValueObject valueObject2 = new GenAggregateValueObject("type2", "value2", (byte) 1);

        GenAggregateDomain domain1 = factory.newInstance(infoId, valueObject1, null);
        GenAggregateDomain domain2 = factory.newInstance(infoId, valueObject2, null);

        assertNotNull(domain1);
        assertNotNull(domain2);
        assertEquals(valueObject1, domain1.getValueObject());
        assertEquals(valueObject2, domain2.getValueObject());
    }
}
