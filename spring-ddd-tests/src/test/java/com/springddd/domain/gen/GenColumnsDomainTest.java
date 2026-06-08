package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class GenColumnsDomainTest {

    @Test
    void shouldCreateGenColumnsDomainWithAllFields() {
        GenColumnsDomain domain = new GenColumnsDomain();
        ColumnsId columnsId = new ColumnsId(1L);
        InfoId infoId = new InfoId(2L);
        Prop prop = new Prop("key", "name", "type", "comment", "javaType", "entity");
        Table table = new Table(true, true, false, null, null);
        Form form = new Form((byte) 1, true, false);
        I18n i18n = new I18n("en", "locale");
        GenColumnsExtendInfo extendInfo = new GenColumnsExtendInfo(100L, (byte) 1);

        domain.setId(columnsId);
        domain.setInfoId(infoId);
        domain.setProp(prop);
        domain.setTable(table);
        domain.setForm(form);
        domain.setI18n(i18n);
        domain.setExtendInfo(extendInfo);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setDeleteStatus(false);
        domain.setVersion(0);

        assertEquals(columnsId, domain.getId());
        assertEquals(infoId, domain.getInfoId());
        assertEquals(prop, domain.getProp());
        assertEquals(table, domain.getTable());
        assertEquals(form, domain.getForm());
        assertEquals(i18n, domain.getI18n());
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
        GenColumnsDomain domain = new GenColumnsDomain();

        domain.create();

        assertNotNull(domain);
    }

    @Test
    void shouldUpdateGenColumnsDomain() {
        GenColumnsDomain domain = new GenColumnsDomain();
        Prop prop = new Prop("newKey", "newName", "newType", "newComment", "newJavaType", "newEntity");
        Table table = new Table(false, false, true, (byte) 1, (byte) 1);
        Form form = new Form((byte) 2, false, true);
        I18n i18n = new I18n("en_us", "zh_cn");
        GenColumnsExtendInfo extendInfo = new GenColumnsExtendInfo(200L, (byte) 2);

        domain.update(prop, table, form, i18n, extendInfo);

        assertEquals(prop, domain.getProp());
        assertEquals(table, domain.getTable());
        assertEquals(form, domain.getForm());
        assertEquals(i18n, domain.getI18n());
        assertEquals(extendInfo, domain.getExtendInfo());
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithNullProp() {
        GenColumnsDomain domain = new GenColumnsDomain();
        Table table = new Table(true, true, false, null, null);
        Form form = new Form((byte) 1, true, false);
        I18n i18n = new I18n("en", "locale");
        GenColumnsExtendInfo extendInfo = new GenColumnsExtendInfo(100L, (byte) 1);

        assertThrows(IllegalArgumentException.class, () -> {
            domain.update(null, table, form, i18n, extendInfo);
        });
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithNullTable() {
        GenColumnsDomain domain = new GenColumnsDomain();
        Prop prop = new Prop("key", "name", "type", "comment", "javaType", "entity");
        Form form = new Form((byte) 1, true, false);
        I18n i18n = new I18n("en", "locale");
        GenColumnsExtendInfo extendInfo = new GenColumnsExtendInfo(100L, (byte) 1);

        assertThrows(IllegalArgumentException.class, () -> {
            domain.update(prop, null, form, i18n, extendInfo);
        });
    }

    @Test
    void shouldDeleteGenColumnsDomain() {
        GenColumnsDomain domain = new GenColumnsDomain();
        domain.setDeleteStatus(false);

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void shouldSetAndGetFields() {
        GenColumnsDomain domain = new GenColumnsDomain();
        ColumnsId columnsId = new ColumnsId(10L);
        InfoId infoId = new InfoId(20L);

        domain.setId(columnsId);
        domain.setInfoId(infoId);

        assertEquals(columnsId, domain.getId());
        assertEquals(infoId, domain.getInfoId());
    }

    @Test
    void shouldHandleNullValues() {
        GenColumnsDomain domain = new GenColumnsDomain();

        domain.setId(null);
        domain.setInfoId(null);
        domain.setProp(null);
        domain.setTable(null);
        domain.setForm(null);
        domain.setI18n(null);
        domain.setExtendInfo(null);

        assertNull(domain.getId());
        assertNull(domain.getInfoId());
        assertNull(domain.getProp());
        assertNull(domain.getTable());
        assertNull(domain.getForm());
        assertNull(domain.getI18n());
        assertNull(domain.getExtendInfo());
    }
}
