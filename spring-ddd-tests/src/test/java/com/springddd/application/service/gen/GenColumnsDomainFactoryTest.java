package com.springddd.application.service.gen;

import com.springddd.domain.gen.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GenColumnsDomainFactoryTest {

    private final GenColumnsDomainFactoryImpl factory = new GenColumnsDomainFactoryImpl();

    @Test
    void newInstance_shouldCreateDomainWithCorrectValues() {
        InfoId infoId = new InfoId(1L);
        Prop prop = new Prop("pri", "column_name", "varchar", "comment", "String", "entity");
        Table table = new Table(true, true, false, null, null);
        Form form = new Form((byte) 1, true, false);
        I18n i18n = new I18n("en", "locale");
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
