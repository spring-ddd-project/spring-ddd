package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GenAggregateDomainTest {

    @Test
    void create_shouldInitializeSuccessfully() {
        GenAggregateDomain domain = new GenAggregateDomain();
        domain.create();
        assertNotNull(domain);
    }

    @Test
    void update_shouldUpdateAllFields() {
        GenAggregateDomain domain = new GenAggregateDomain();
        InfoId infoId = new InfoId(1L);
        GenAggregateValueObject valueObject = new GenAggregateValueObject("name", "value", (byte) 1);
        GenAggregateExtendInfo extendInfo = new GenAggregateExtendInfo(true);

        domain.update(infoId, valueObject, extendInfo);

        assertEquals(infoId, domain.getInfoId());
        assertEquals(valueObject, domain.getValueObject());
        assertEquals(extendInfo, domain.getExtendInfo());
    }

    @Test
    void update_withNullValues_shouldSetToNull() {
        GenAggregateDomain domain = new GenAggregateDomain();
        domain.setInfoId(new InfoId(1L));
        domain.setValueObject(new GenAggregateValueObject("name", "value", (byte) 1));
        domain.setExtendInfo(new GenAggregateExtendInfo(true));

        domain.update(null, null, null);

        assertNull(domain.getInfoId());
        assertNull(domain.getValueObject());
        assertNull(domain.getExtendInfo());
    }

}
