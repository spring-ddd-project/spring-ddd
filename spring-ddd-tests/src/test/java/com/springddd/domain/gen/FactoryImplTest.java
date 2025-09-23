package com.springddd.domain.gen;

import com.springddd.application.service.gen.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FactoryImplTest {

    // ==================== GenAggregateDomainFactoryImpl Tests ====================

    @InjectMocks
    private GenAggregateDomainFactoryImpl genAggregateDomainFactory;

    @Test
    void genAggregateDomainFactory_newInstance_shouldCreateDomainWithAllFields() {
        InfoId infoId = new InfoId(1L);
        GenAggregateValueObject valueObject = new GenAggregateValueObject("name", "value", (byte) 1);
        GenAggregateExtendInfo extendInfo = new GenAggregateExtendInfo(true);

        GenAggregateDomain domain = genAggregateDomainFactory.newInstance(infoId, valueObject, extendInfo);

        assertNotNull(domain);
        assertEquals(infoId, domain.getInfoId());
        assertEquals(valueObject, domain.getValueObject());
        assertEquals(extendInfo, domain.getExtendInfo());
    }

    @Test
    void genAggregateDomainFactory_newInstance_withNullExtendInfo_shouldCreateDomain() {
        InfoId infoId = new InfoId(1L);
        GenAggregateValueObject valueObject = new GenAggregateValueObject("name", "value", (byte) 1);

        GenAggregateDomain domain = genAggregateDomainFactory.newInstance(infoId, valueObject, null);

        assertNotNull(domain);
        assertEquals(infoId, domain.getInfoId());
        assertEquals(valueObject, domain.getValueObject());
        assertNull(domain.getExtendInfo());
    }

    // ==================== GenColumnsDomainFactoryImpl Tests ====================

    @InjectMocks
    private GenColumnsDomainFactoryImpl genColumnsDomainFactory;

    @Test
    void genColumnsDomainFactory_newInstance_shouldCreateDomainWithAllFields() {
        InfoId infoId = new InfoId(1L);
        Prop prop = new Prop("pri", "column_name", "varchar", "comment", "String", "entity");
        Table table = new Table(true, true, false, null, null);
        Form form = new Form((byte) 1, true, false);
        I18n i18n = new I18n("en", "locale");
        GenColumnsExtendInfo extendInfo = new GenColumnsExtendInfo(1L, (byte) 1);

        GenColumnsDomain domain = genColumnsDomainFactory.newInstance(infoId, prop, table, form, i18n, extendInfo);

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
    void genColumnsDomainFactory_newInstance_withNullExtendInfo_shouldCreateDomain() {
        InfoId infoId = new InfoId(1L);
        Prop prop = new Prop("pri", "column_name", "varchar", "comment", "String", "entity");
        Table table = new Table(true, true, false, null, null);
        Form form = new Form((byte) 1, true, false);
        I18n i18n = new I18n("en", "locale");

        GenColumnsDomain domain = genColumnsDomainFactory.newInstance(infoId, prop, table, form, i18n, null);

        assertNotNull(domain);
        assertEquals(infoId, domain.getInfoId());
        assertNull(domain.getExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }

    // ==================== GenColumnBindDomainFactoryImpl Tests ====================

    @InjectMocks
    private GenColumnBindDomainFactoryImpl genColumnBindDomainFactory;

    @Test
    void genColumnBindDomainFactory_newInstance_shouldCreateDomainWithAllFields() {
        GenColumnBindBasicInfo basicInfo = new GenColumnBindBasicInfo("varchar", "String", (byte) 1, (byte) 1);

        GenColumnBindDomain domain = genColumnBindDomainFactory.newInstance(basicInfo);

        assertNotNull(domain);
        assertEquals(basicInfo, domain.getBasicInfo());
        assertFalse(domain.getDeleteStatus());
    }

    // ==================== GenProjectInfoDomainFactoryImpl Tests ====================

    @InjectMocks
    private GenProjectInfoDomainFactoryImpl genProjectInfoDomainFactory;

    @Test
    void genProjectInfoDomainFactory_newInstance_shouldCreateDomainWithAllFields() {
        ProjectInfo projectInfo = new ProjectInfo("table_name", "com.example", "ClassName", "module", "project");
        GenProjectInfoExtendInfo extendInfo = new GenProjectInfoExtendInfo("requestName");

        GenProjectInfoDomain domain = genProjectInfoDomainFactory.newInstance(projectInfo, extendInfo);

        assertNotNull(domain);
        assertEquals(projectInfo, domain.getProjectInfo());
        assertEquals(extendInfo, domain.getExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void genProjectInfoDomainFactory_newInstance_withNullExtendInfo_shouldCreateDomain() {
        ProjectInfo projectInfo = new ProjectInfo("table_name", "com.example", "ClassName", "module", "project");

        GenProjectInfoDomain domain = genProjectInfoDomainFactory.newInstance(projectInfo, null);

        assertNotNull(domain);
        assertEquals(projectInfo, domain.getProjectInfo());
        assertNull(domain.getExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }

    // ==================== GenTemplateDomainFactoryImpl Tests ====================

    @InjectMocks
    private GenTemplateDomainFactoryImpl genTemplateDomainFactory;

    @Test
    void genTemplateDomainFactory_newInstance_shouldCreateDomainWithAllFields() {
        TemplateInfo templateInfo = new TemplateInfo("templateName", "templateContent");

        GenTemplateDomain domain = genTemplateDomainFactory.newInstance(templateInfo);

        assertNotNull(domain);
        assertEquals(templateInfo, domain.getTemplateInfo());
        assertFalse(domain.getDeleteStatus());
    }
}
