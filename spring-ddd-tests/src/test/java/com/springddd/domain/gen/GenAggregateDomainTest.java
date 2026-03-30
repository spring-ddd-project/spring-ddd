package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class GenAggregateDomainTest {

    @Test
    void shouldCreateGenAggregateDomainWithAllFields() {
        GenAggregateDomain domain = new GenAggregateDomain();
        AggregateId aggregateId = new AggregateId(1L);
        InfoId infoId = new InfoId(2L);
        GenAggregateValueObject valueObject = new GenAggregateValueObject("name", "value", (byte) 1);
        GenAggregateExtendInfo extendInfo = new GenAggregateExtendInfo(true);

        domain.setAggregateId(aggregateId);
        domain.setInfoId(infoId);
        domain.setValueObject(valueObject);
        domain.setExtendInfo(extendInfo);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setDeleteStatus(false);
        domain.setVersion(0);

        assertEquals(aggregateId, domain.getAggregateId());
        assertEquals(infoId, domain.getInfoId());
        assertEquals(valueObject, domain.getValueObject());
        assertEquals(extendInfo, domain.getExtendInfo());
        assertEquals("admin", domain.getCreateBy());
        assertNotNull(domain.getCreateTime());
        assertEquals("admin", domain.getUpdateBy());
        assertNotNull(domain.getUpdateTime());
        assertFalse(domain.getDeleteStatus());
        assertEquals(0, domain.getVersion());
    }

    @Test
    void shouldCallCreateMethod() {
        GenAggregateDomain domain = new GenAggregateDomain();

        domain.create();

        assertNotNull(domain);
    }

    @Test
    void shouldUpdateGenAggregateDomain() {
        GenAggregateDomain domain = new GenAggregateDomain();
        InfoId newInfoId = new InfoId(3L);
        GenAggregateValueObject newValueObject = new GenAggregateValueObject("newName", "newValue", (byte) 2);
        GenAggregateExtendInfo newExtendInfo = new GenAggregateExtendInfo(false);

        domain.update(newInfoId, newValueObject, newExtendInfo);

        assertEquals(newInfoId, domain.getInfoId());
        assertEquals(newValueObject, domain.getValueObject());
        assertEquals(newExtendInfo, domain.getExtendInfo());
    }

    @Test
    void shouldDeleteGenAggregateDomain() {
        GenAggregateDomain domain = new GenAggregateDomain();
        domain.setDeleteStatus(false);

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void shouldRestoreGenAggregateDomain() {
        GenAggregateDomain domain = new GenAggregateDomain();
        domain.setDeleteStatus(true);

        domain.restore();

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldSetAndGetFields() {
        GenAggregateDomain domain = new GenAggregateDomain();
        AggregateId aggregateId = new AggregateId(10L);
        InfoId infoId = new InfoId(20L);

        domain.setAggregateId(aggregateId);
        domain.setInfoId(infoId);

        assertEquals(aggregateId, domain.getAggregateId());
        assertEquals(infoId, domain.getInfoId());
    }

    @Test
    void shouldHandleNullValues() {
        GenAggregateDomain domain = new GenAggregateDomain();

        domain.setAggregateId(null);
        domain.setInfoId(null);
        domain.setValueObject(null);
        domain.setExtendInfo(null);

        assertNull(domain.getAggregateId());
        assertNull(domain.getInfoId());
        assertNull(domain.getValueObject());
        assertNull(domain.getExtendInfo());
    }
}
