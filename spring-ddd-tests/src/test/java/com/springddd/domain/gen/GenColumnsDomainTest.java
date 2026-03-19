package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GenColumnsDomainTest {

    @Test
    void create_shouldInitializeSuccessfully() {
        GenColumnsDomain domain = new GenColumnsDomain();
        domain.create();
        assertNotNull(domain);
    }

    @Test
    void update_shouldUpdateAllFields() {
        GenColumnsDomain domain = new GenColumnsDomain();
        Prop prop = new Prop("pri", "column_name", "varchar", "comment", "String", "entity");
        Table table = new Table(true, true, false, null, null);
        Form form = new Form((byte) 1, true, false);
        I18n i18n = new I18n("en", "locale");
        GenColumnsExtendInfo extendInfo = new GenColumnsExtendInfo(1L, (byte) 1);

        domain.update(prop, table, form, i18n, extendInfo);

        assertEquals(prop, domain.getProp());
        assertEquals(table, domain.getTable());
        assertEquals(form, domain.getForm());
        assertEquals(i18n, domain.getI18n());
        assertEquals(extendInfo, domain.getExtendInfo());
    }

    @Test
    void update_withNullValues_shouldSetToNull() {
        GenColumnsDomain domain = new GenColumnsDomain();
        domain.setProp(new Prop("pri", "column_name", "varchar", "comment", "String", "entity"));

        domain.update(null, null, null, null, null);

        assertNull(domain.getProp());
        assertNull(domain.getTable());
        assertNull(domain.getForm());
        assertNull(domain.getI18n());
        assertNull(domain.getExtendInfo());
    }

    @Test
    void delete_shouldSetDeleteStatusToTrue() {
        GenColumnsDomain domain = new GenColumnsDomain();
        assertNull(domain.getDeleteStatus());

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }
}
