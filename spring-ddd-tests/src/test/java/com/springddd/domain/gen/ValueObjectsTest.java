package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueObjectsTest {

    @Test
    void prop_withValidParams_shouldCreateSuccessfully() {
        Prop prop = new Prop("pri", "column_name", "varchar", "comment", "String", "entity");
        assertEquals("pri", prop.propColumnKey());
        assertEquals("column_name", prop.propColumnName());
    }

    @Test
    void prop_withNullColumnName_shouldThrowColumnNameNullException() {
        assertThrows(ColumnNameNullException.class,
            () -> new Prop("pri", null, "varchar", "comment", "String", "entity"));
    }

    @Test
    void table_withValidParams_shouldCreateSuccessfully() {
        Table table = new Table(true, true, false, null, null);
        assertTrue(table.tableVisible());
        assertTrue(table.tableOrder());
    }

    @Test
    void table_withNullVisible_shouldThrowVisibleNullException() {
        assertThrows(VisibleNullException.class,
            () -> new Table(null, true, false, null, null));
    }

    @Test
    void form_withValidParams_shouldCreateSuccessfully() {
        Form form = new Form((byte) 1, true, false);
        assertEquals((byte) 1, form.formComponent());
        assertTrue(form.formVisible());
    }

    @Test
    void i18n_withValidParams_shouldCreateSuccessfully() {
        I18n i18n = new I18n("en", "locale");
        assertEquals("en", i18n.en());
    }

    @Test
    void i18n_withNullEn_shouldThrowI18nEnNullException() {
        assertThrows(I18nEnNullException.class,
            () -> new I18n(null, "locale"));
    }

    @Test
    void templateInfo_withValidParams_shouldCreateSuccessfully() {
        TemplateInfo info = new TemplateInfo("templateName", "templateContent");
        assertEquals("templateName", info.templateName());
    }

    @Test
    void templateInfo_withNullTemplateName_shouldThrowTemplateNameNullException() {
        assertThrows(TemplateNameNullException.class,
            () -> new TemplateInfo(null, "templateContent"));
    }

    @Test
    void projectInfo_withValidParams_shouldCreateSuccessfully() {
        ProjectInfo info = new ProjectInfo("table_name", "com.example", "ClassName", "module", "project");
        assertEquals("table_name", info.tableName());
    }

    @Test
    void projectInfo_withNullTableName_shouldThrowTableNameNullException() {
        assertThrows(TableNameNullException.class,
            () -> new ProjectInfo(null, "com.example", "ClassName", "module", "project"));
    }

    @Test
    void genColumnBindBasicInfo_withValidParams_shouldCreateSuccessfully() {
        GenColumnBindBasicInfo info = new GenColumnBindBasicInfo("varchar", "String", (byte) 1, (byte) 1);
        assertEquals("varchar", info.columnType());
    }

    @Test
    void genAggregateValueObject_withValidParams_shouldCreateSuccessfully() {
        GenAggregateValueObject vo = new GenAggregateValueObject("name", "value", (byte) 1);
        assertEquals("name", vo.objectName());
    }

    @Test
    void genColumnsExtendInfo_withValidParams_shouldCreateSuccessfully() {
        GenColumnsExtendInfo info = new GenColumnsExtendInfo(1L, (byte) 1);
        assertEquals(1L, info.propDictId());
    }

    @Test
    void genAggregateExtendInfo_withValidParams_shouldCreateSuccessfully() {
        GenAggregateExtendInfo info = new GenAggregateExtendInfo(true);
        assertTrue(info.hasCreated());
    }

    @Test
    void genProjectInfoExtendInfo_withValidParams_shouldCreateSuccessfully() {
        GenProjectInfoExtendInfo info = new GenProjectInfoExtendInfo("requestName");
        assertEquals("requestName", info.requestName());
    }
}
