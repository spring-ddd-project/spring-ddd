package com.springddd.application.service.gen;

import com.springddd.domain.gen.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenColumnsDomainFactoryImplTest {

    private final GenColumnsDomainFactoryImpl factory = new GenColumnsDomainFactoryImpl();

    @Test
    @DisplayName("should create GenColumnsDomain with correct fields set")
    void newInstance() {
        InfoId infoId = new InfoId(1L);
        Prop prop = new Prop("key", "name", "type", "comment", "javaType", "entity");
        Table table = new Table(true, true, false, null, null);
        Form form = new Form((byte) 1, true, false);
        I18n i18n = new I18n("en", "zh");
        GenColumnsExtendInfo extendInfo = new GenColumnsExtendInfo(1L, (byte) 1);

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
}
