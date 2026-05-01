package com.springddd.application.service.gen;

import com.springddd.domain.gen.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenColumnsDomainFactoryImplTest {

    private final GenColumnsDomainFactoryImpl factory = new GenColumnsDomainFactoryImpl();

    @Test
    void shouldCreateGenColumnsDomainWithAllFields() {
        InfoId infoId = new InfoId(1L);
        Prop prop = new Prop("key", "name", "type", "comment", "javaType", "entity");
        Table table = new Table(true, true, false, null, null);
        Form form = new Form((byte) 1, true, false);
        I18n i18n = new I18n("en", "locale");
        GenColumnsExtendInfo extendInfo = new GenColumnsExtendInfo(100L, (byte) 1);

        GenColumnsDomain domain = factory.newInstance(infoId, prop, table, form, i18n, extendInfo);

        assertNotNull(domain);
        assertEquals(infoId, domain.getInfoId());
        assertEquals(prop, domain.getProp());
        assertEquals(table, domain.getTable());
        assertEquals(form, domain.getForm());
        assertEquals(i18n, domain.getI18n());
        assertEquals(extendInfo, domain.getExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldCreateGenColumnsDomainWithNullExtendInfo() {
        InfoId infoId = new InfoId(2L);
        Prop prop = new Prop("key", "name", "type", "comment", "javaType", "entity");
        Table table = new Table(false, false, true, (byte) 1, (byte) 1);
        Form form = new Form((byte) 2, false, true);
        I18n i18n = new I18n("en_us", "zh_cn");

        GenColumnsDomain domain = factory.newInstance(infoId, prop, table, form, i18n, null);

        assertNotNull(domain);
        assertEquals(infoId, domain.getInfoId());
        assertEquals(prop, domain.getProp());
        assertEquals(table, domain.getTable());
        assertEquals(form, domain.getForm());
        assertEquals(i18n, domain.getI18n());
        assertNull(domain.getExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldSetDeleteStatusToFalse() {
        InfoId infoId = new InfoId(3L);
        Prop prop = new Prop("key", "name", "type", "comment", "javaType", "entity");
        Table table = new Table(true, true, false, null, null);
        Form form = new Form((byte) 1, true, false);
        I18n i18n = new I18n("en", "locale");
        GenColumnsExtendInfo extendInfo = new GenColumnsExtendInfo(100L, (byte) 1);

        GenColumnsDomain domain = factory.newInstance(infoId, prop, table, form, i18n, extendInfo);

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldCreateGenColumnsDomainWithAllTableFlags() {
        InfoId infoId = new InfoId(4L);
        Prop prop = new Prop("testKey", "testName", "varchar", "testComment", "String", "TestEntity");
        Table table = new Table(false, true, true, (byte) 2, (byte) 3);
        Form form = new Form((byte) 0, false, false);
        I18n i18n = new I18n("zh", "chinese");
        GenColumnsExtendInfo extendInfo = new GenColumnsExtendInfo(200L, (byte) 2);

        GenColumnsDomain domain = factory.newInstance(infoId, prop, table, form, i18n, extendInfo);

        assertNotNull(domain);
        assertEquals(infoId, domain.getInfoId());
        assertEquals(prop, domain.getProp());
        assertEquals(table, domain.getTable());
        assertEquals(form, domain.getForm());
        assertEquals(i18n, domain.getI18n());
        assertEquals(extendInfo, domain.getExtendInfo());
    }
}
