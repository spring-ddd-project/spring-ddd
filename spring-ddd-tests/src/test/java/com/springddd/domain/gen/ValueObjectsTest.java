package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueObjectsTest {

    // Prop tests
    @Test
    void prop_withValidParams_shouldCreateSuccessfully() {
        Prop prop = new Prop("pri", "column_name", "varchar", "comment", "String", "entity");
        assertEquals("pri", prop.propColumnKey());
        assertEquals("column_name", prop.propColumnName());
        assertEquals("varchar", prop.propColumnType());
        assertEquals("comment", prop.propColumnComment());
        assertEquals("String", prop.propJavaType());
        assertEquals("entity", prop.propJavaEntity());
    }

    @Test
    void prop_withNullColumnName_shouldThrowColumnNameNullException() {
        assertThrows(ColumnNameNullException.class,
            () -> new Prop("pri", null, "varchar", "comment", "String", "entity"));
    }

    @Test
    void prop_withEmptyColumnName_shouldThrowColumnNameNullException() {
        assertThrows(ColumnNameNullException.class,
            () -> new Prop("pri", "", "varchar", "comment", "String", "entity"));
    }

    @Test
    void prop_withNullColumnType_shouldThrowColumnTypeNullException() {
        assertThrows(ColumnTypeNullException.class,
            () -> new Prop("pri", "column_name", null, "comment", "String", "entity"));
    }

    @Test
    void prop_withNullColumnComment_shouldThrowColumnCommentNullException() {
        assertThrows(ColumnCommentNullException.class,
            () -> new Prop("pri", "column_name", "varchar", null, "String", "entity"));
    }

    @Test
    void prop_withNullJavaType_shouldThrowJavaTypeNullException() {
        assertThrows(JavaTypeNullException.class,
            () -> new Prop("pri", "column_name", "varchar", "comment", null, "entity"));
    }

    @Test
    void prop_withNullJavaEntity_shouldThrowJavaEntityNullException() {
        assertThrows(JavaEntityNullException.class,
            () -> new Prop("pri", "column_name", "varchar", "comment", "String", null));
    }

    // Table tests
    @Test
    void table_withValidParams_shouldCreateSuccessfully() {
        Table table = new Table(true, true, false, null, null);
        assertTrue(table.tableVisible());
        assertTrue(table.tableOrder());
        assertFalse(table.tableFilter());
    }

    @Test
    void table_withNullVisible_shouldThrowVisibleNullException() {
        assertThrows(VisibleNullException.class,
            () -> new Table(null, true, false, null, null));
    }

    @Test
    void table_withNullOrder_shouldThrowOrderNullException() {
        assertThrows(OrderNullException.class,
            () -> new Table(true, null, false, null, null));
    }

    @Test
    void table_withNullFilter_shouldThrowFilterNullException() {
        assertThrows(FilterNullException.class,
            () -> new Table(true, true, null, null, null));
    }

    @Test
    void table_withFilterTrueAndNullComponent_shouldThrowFilterComponentNullException() {
        assertThrows(FilterComponentNullException.class,
            () -> new Table(true, true, true, null, null));
    }

    @Test
    void table_withFilterTrueAndValidComponent_shouldCreateSuccessfully() {
        Table table = new Table(true, true, true, (byte) 1, (byte) 1);
        assertTrue(table.tableFilter());
        assertEquals((byte) 1, table.tableFilterComponent());
    }

    // Form tests
    @Test
    void form_withValidParams_shouldCreateSuccessfully() {
        Form form = new Form((byte) 1, true, false);
        assertEquals((byte) 1, form.formComponent());
        assertTrue(form.formVisible());
        assertFalse(form.formRequired());
    }

    @Test
    void form_withNullComponent_shouldThrowFormComponentNullException() {
        assertThrows(FormComponentNullException.class,
            () -> new Form(null, true, false));
    }

    @Test
    void form_withNullVisible_shouldThrowFormVisibleNullException() {
        assertThrows(FormVisibleNullException.class,
            () -> new Form((byte) 1, null, false));
    }

    @Test
    void form_withNullRequired_shouldThrowFormRequiredNullException() {
        assertThrows(FormRequiredNullException.class,
            () -> new Form((byte) 1, true, null));
    }

    // I18n tests
    @Test
    void i18n_withValidParams_shouldCreateSuccessfully() {
        I18n i18n = new I18n("en", "locale");
        assertEquals("en", i18n.en());
        assertEquals("locale", i18n.locale());
    }

    @Test
    void i18n_withNullEn_shouldThrowI18nEnNullException() {
        assertThrows(I18nEnNullException.class,
            () -> new I18n(null, "locale"));
    }

    @Test
    void i18n_withEmptyEn_shouldThrowI18nEnNullException() {
        assertThrows(I18nEnNullException.class,
            () -> new I18n("", "locale"));
    }

    // ProjectInfo tests
    @Test
    void projectInfo_withValidParams_shouldCreateSuccessfully() {
        ProjectInfo info = new ProjectInfo("table_name", "com.example", "ClassName", "module", "project");
        assertEquals("table_name", info.tableName());
        assertEquals("com.example", info.packageName());
        assertEquals("ClassName", info.className());
        assertEquals("module", info.moduleName());
        assertEquals("project", info.projectName());
    }

    @Test
    void projectInfo_withNullTableName_shouldThrowTableNameNullException() {
        assertThrows(TableNameNullException.class,
            () -> new ProjectInfo(null, "com.example", "ClassName", "module", "project"));
    }

    @Test
    void projectInfo_withNullPackageName_shouldThrowPackageNameNullException() {
        assertThrows(PackageNameNullException.class,
            () -> new ProjectInfo("table_name", null, "ClassName", "module", "project"));
    }

    @Test
    void projectInfo_withNullClassName_shouldThrowClassNameNullException() {
        assertThrows(ClassNameNullException.class,
            () -> new ProjectInfo("table_name", "com.example", null, "module", "project"));
    }

    @Test
    void projectInfo_withNullModuleName_shouldThrowModuleNameNullException() {
        assertThrows(ModuleNameNullException.class,
            () -> new ProjectInfo("table_name", "com.example", "ClassName", null, "project"));
    }

    @Test
    void projectInfo_withNullProjectName_shouldThrowProjectNameNullException() {
        assertThrows(ProjectNameNullException.class,
            () -> new ProjectInfo("table_name", "com.example", "ClassName", "module", null));
    }

    // TemplateInfo tests
    @Test
    void templateInfo_withValidParams_shouldCreateSuccessfully() {
        TemplateInfo info = new TemplateInfo("templateName", "templateContent");
        assertEquals("templateName", info.templateName());
        assertEquals("templateContent", info.templateContent());
    }

    @Test
    void templateInfo_withNullTemplateName_shouldThrowTemplateNameNullException() {
        assertThrows(TemplateNameNullException.class,
            () -> new TemplateInfo(null, "templateContent"));
    }

    @Test
    void templateInfo_withNullTemplateContent_shouldThrowTemplateContentNullException() {
        assertThrows(TemplateContentNullException.class,
            () -> new TemplateInfo("templateName", null));
    }

    // GenColumnBindBasicInfo tests
    @Test
    void genColumnBindBasicInfo_withValidParams_shouldCreateSuccessfully() {
        GenColumnBindBasicInfo info = new GenColumnBindBasicInfo("varchar", "String", (byte) 1, (byte) 1);
        assertEquals("varchar", info.columnType());
        assertEquals("String", info.entityType());
        assertEquals((byte) 1, info.componentType());
        assertEquals((byte) 1, info.typescriptType());
    }

    @Test
    void genColumnBindBasicInfo_withNullColumnType_shouldThrowColumnTypeNullException() {
        assertThrows(ColumnTypeNullException.class,
            () -> new GenColumnBindBasicInfo(null, "String", (byte) 1, (byte) 1));
    }

    @Test
    void genColumnBindBasicInfo_withNullEntityType_shouldThrowJavaTypeNullException() {
        assertThrows(JavaTypeNullException.class,
            () -> new GenColumnBindBasicInfo("varchar", null, (byte) 1, (byte) 1));
    }

    @Test
    void genColumnBindBasicInfo_withNullComponentType_shouldThrowComponentTypeNullException() {
        assertThrows(ComponentTypeNullException.class,
            () -> new GenColumnBindBasicInfo("varchar", "String", null, (byte) 1));
    }

    @Test
    void genColumnBindBasicInfo_withNullTypescriptType_shouldThrowTypeScriptTypeNullException() {
        assertThrows(TypeScriptTypeNullException.class,
            () -> new GenColumnBindBasicInfo("varchar", "String", (byte) 1, null));
    }

    // GenAggregateValueObject tests
    @Test
    void genAggregateValueObject_withValidParams_shouldCreateSuccessfully() {
        GenAggregateValueObject vo = new GenAggregateValueObject("name", "value", (byte) 1);
        assertEquals("name", vo.objectName());
        assertEquals("value", vo.objectValue());
        assertEquals((byte) 1, vo.objectType());
    }

    @Test
    void genAggregateValueObject_withNullObjectName_shouldThrowObjectNameNullException() {
        assertThrows(ObjectNameNullException.class,
            () -> new GenAggregateValueObject(null, "value", (byte) 1));
    }

    @Test
    void genAggregateValueObject_withNullObjectValue_shouldThrowValueObjectNullException() {
        assertThrows(ValueObjectNullException.class,
            () -> new GenAggregateValueObject("name", null, (byte) 1));
    }

    @Test
    void genAggregateValueObject_withNullObjectType_shouldThrowObjectTypeNullException() {
        assertThrows(ObjectTypeNullException.class,
            () -> new GenAggregateValueObject("name", "value", null));
    }

    // GenColumnsExtendInfo tests
    @Test
    void genColumnsExtendInfo_withValidParams_shouldCreateSuccessfully() {
        GenColumnsExtendInfo info = new GenColumnsExtendInfo(1L, (byte) 1);
        assertEquals(1L, info.propDictId());
        assertEquals((byte) 1, info.typescriptType());
    }

    @Test
    void genColumnsExtendInfo_withNullValues_shouldCreateSuccessfully() {
        GenColumnsExtendInfo info = new GenColumnsExtendInfo(null, null);
        assertNull(info.propDictId());
        assertNull(info.typescriptType());
    }

    // GenAggregateExtendInfo tests
    @Test
    void genAggregateExtendInfo_withValidParams_shouldCreateSuccessfully() {
        GenAggregateExtendInfo info = new GenAggregateExtendInfo(true);
        assertTrue(info.hasCreated());
    }

    @Test
    void genAggregateExtendInfo_withNull_shouldCreateSuccessfully() {
        GenAggregateExtendInfo info = new GenAggregateExtendInfo(null);
        assertNull(info.hasCreated());
    }

    // GenProjectInfoExtendInfo tests
    @Test
    void genProjectInfoExtendInfo_withValidParams_shouldCreateSuccessfully() {
        GenProjectInfoExtendInfo info = new GenProjectInfoExtendInfo("requestName");
        assertEquals("requestName", info.requestName());
    }

    @Test
    void genProjectInfoExtendInfo_withNull_shouldCreateSuccessfully() {
        GenProjectInfoExtendInfo info = new GenProjectInfoExtendInfo(null);
        assertNull(info.requestName());
    }
}
