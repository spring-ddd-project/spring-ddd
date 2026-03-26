package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueObjectsValidationTest {

    // ==================== Prop Validation Tests ====================

    @Test
    void prop_withNullPropColumnKey_shouldCreateSuccessfully() {
        // propColumnKey is allowed to be null
        Prop prop = new Prop(null, "column_name", "varchar", "comment", "String", "entity");
        assertNull(prop.propColumnKey());
    }

    @Test
    void prop_withAllValidParams_shouldCreateSuccessfully() {
        Prop prop = new Prop("pri", "column_name", "varchar", "comment", "String", "entity");
        assertEquals("pri", prop.propColumnKey());
        assertEquals("column_name", prop.propColumnName());
        assertEquals("varchar", prop.propColumnType());
        assertEquals("comment", prop.propColumnComment());
        assertEquals("String", prop.propJavaType());
        assertEquals("entity", prop.propJavaEntity());
    }

    @Test
    void prop_withNullPropColumnType_shouldThrowColumnTypeNullException() {
        assertThrows(ColumnTypeNullException.class,
            () -> new Prop("pri", "column_name", null, "comment", "String", "entity"));
    }

    @Test
    void prop_withNullPropColumnComment_shouldThrowColumnCommentNullException() {
        assertThrows(ColumnCommentNullException.class,
            () -> new Prop("pri", "column_name", "varchar", null, "String", "entity"));
    }

    @Test
    void prop_withNullPropJavaType_shouldThrowJavaTypeNullException() {
        assertThrows(JavaTypeNullException.class,
            () -> new Prop("pri", "column_name", "varchar", "comment", null, "entity"));
    }

    @Test
    void prop_withNullPropJavaEntity_shouldThrowJavaEntityNullException() {
        assertThrows(JavaEntityNullException.class,
            () -> new Prop("pri", "column_name", "varchar", "comment", "String", null));
    }

    // ==================== Table Validation Tests ====================

    @Test
    void table_withValidParamsAndFilterEnabled_shouldCreateSuccessfully() {
        Table table = new Table(true, true, true, (byte) 1, (byte) 1);
        assertTrue(table.tableVisible());
        assertTrue(table.tableOrder());
        assertTrue(table.tableFilter());
        assertEquals((byte) 1, table.tableFilterComponent());
        assertEquals((byte) 1, table.tableFilterType());
    }

    @Test
    void table_withNullTableVisible_shouldThrowVisibleNullException() {
        assertThrows(VisibleNullException.class,
            () -> new Table(null, true, false, null, null));
    }

    @Test
    void table_withNullTableOrder_shouldThrowOrderNullException() {
        assertThrows(OrderNullException.class,
            () -> new Table(true, null, false, null, null));
    }

    @Test
    void table_withNullTableFilter_shouldThrowFilterNullException() {
        assertThrows(FilterNullException.class,
            () -> new Table(true, true, null, null, null));
    }

    @Test
    void table_withFilterTrueAndNullFilterComponent_shouldThrowFilterComponentNullException() {
        assertThrows(FilterComponentNullException.class,
            () -> new Table(true, true, true, null, null));
    }

    @Test
    void table_withFilterFalseAndNullFilterComponent_shouldCreateSuccessfully() {
        // When tableFilter is false, tableFilterComponent can be null
        Table table = new Table(true, true, false, null, null);
        assertFalse(table.tableFilter());
        assertNull(table.tableFilterComponent());
    }

    // ==================== Form Validation Tests ====================

    @Test
    void form_withNullFormComponent_shouldThrowFormComponentNullException() {
        assertThrows(FormComponentNullException.class,
            () -> new Form(null, true, false));
    }

    @Test
    void form_withNullFormVisible_shouldThrowFormVisibleNullException() {
        assertThrows(FormVisibleNullException.class,
            () -> new Form((byte) 1, null, false));
    }

    @Test
    void form_withNullFormRequired_shouldThrowFormRequiredNullException() {
        assertThrows(FormRequiredNullException.class,
            () -> new Form((byte) 1, true, null));
    }

    @Test
    void form_withAllFalse_shouldCreateSuccessfully() {
        Form form = new Form((byte) 0, false, false);
        assertEquals((byte) 0, form.formComponent());
        assertFalse(form.formVisible());
        assertFalse(form.formRequired());
    }

    @Test
    void form_withAllTrue_shouldCreateSuccessfully() {
        Form form = new Form((byte) 1, true, true);
        assertEquals((byte) 1, form.formComponent());
        assertTrue(form.formVisible());
        assertTrue(form.formRequired());
    }

    // ==================== I18n Validation Tests ====================

    @Test
    void i18n_withNullEn_shouldThrowI18nEnNullException() {
        assertThrows(I18nEnNullException.class,
            () -> new I18n(null, "locale"));
    }

    @Test
    void i18n_withNullLocale_shouldCreateSuccessfully() {
        // locale is allowed to be null
        I18n i18n = new I18n("en", null);
        assertEquals("en", i18n.en());
        assertNull(i18n.locale());
    }

    @Test
    void i18n_withBothParams_shouldCreateSuccessfully() {
        I18n i18n = new I18n("en_US", "zh_CN");
        assertEquals("en_US", i18n.en());
        assertEquals("zh_CN", i18n.locale());
    }

    // ==================== ProjectInfo Validation Tests ====================

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

    @Test
    void projectInfo_withAllValidParams_shouldCreateSuccessfully() {
        ProjectInfo info = new ProjectInfo("table_name", "com.example", "ClassName", "module", "project");
        assertEquals("table_name", info.tableName());
        assertEquals("com.example", info.packageName());
        assertEquals("ClassName", info.className());
        assertEquals("module", info.moduleName());
        assertEquals("project", info.projectName());
    }

    // ==================== TemplateInfo Validation Tests ====================

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

    @Test
    void templateInfo_withAllValidParams_shouldCreateSuccessfully() {
        TemplateInfo info = new TemplateInfo("templateName", "templateContent");
        assertEquals("templateName", info.templateName());
        assertEquals("templateContent", info.templateContent());
    }

    // ==================== GenColumnBindBasicInfo Validation Tests ====================

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
    void genColumnBindBasicInfo_withNullTypeScriptType_shouldThrowTypeScriptTypeNullException() {
        assertThrows(TypeScriptTypeNullException.class,
            () -> new GenColumnBindBasicInfo("varchar", "String", (byte) 1, null));
    }

    @Test
    void genColumnBindBasicInfo_withAllValidParams_shouldCreateSuccessfully() {
        GenColumnBindBasicInfo info = new GenColumnBindBasicInfo("varchar", "String", (byte) 1, (byte) 1);
        assertEquals("varchar", info.columnType());
        assertEquals("String", info.entityType());
        assertEquals((byte) 1, info.componentType());
        assertEquals((byte) 1, info.typescriptType());
    }

    // ==================== GenAggregateValueObject Validation Tests ====================

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

    @Test
    void genAggregateValueObject_withAllValidParams_shouldCreateSuccessfully() {
        GenAggregateValueObject vo = new GenAggregateValueObject("name", "value", (byte) 1);
        assertEquals("name", vo.objectName());
        assertEquals("value", vo.objectValue());
        assertEquals((byte) 1, vo.objectType());
    }

    // ==================== GenColumnsExtendInfo Tests ====================

    @Test
    void genColumnsExtendInfo_withNullDictId_shouldCreateSuccessfully() {
        GenColumnsExtendInfo info = new GenColumnsExtendInfo(null, (byte) 1);
        assertNull(info.propDictId());
        assertEquals((byte) 1, info.typescriptType());
    }

    @Test
    void genColumnsExtendInfo_withAllParams_shouldCreateSuccessfully() {
        GenColumnsExtendInfo info = new GenColumnsExtendInfo(1L, (byte) 1);
        assertEquals(1L, info.propDictId());
        assertEquals((byte) 1, info.typescriptType());
    }

    // ==================== GenAggregateExtendInfo Tests ====================

    @Test
    void genAggregateExtendInfo_withNull_shouldCreateSuccessfully() {
        GenAggregateExtendInfo info = new GenAggregateExtendInfo(null);
        assertNull(info.hasCreated());
    }

    @Test
    void genAggregateExtendInfo_withTrue_shouldCreateSuccessfully() {
        GenAggregateExtendInfo info = new GenAggregateExtendInfo(true);
        assertTrue(info.hasCreated());
    }

    // ==================== GenProjectInfoExtendInfo Tests ====================

    @Test
    void genProjectInfoExtendInfo_withNull_shouldCreateSuccessfully() {
        GenProjectInfoExtendInfo info = new GenProjectInfoExtendInfo(null);
        assertNull(info.requestName());
    }

    @Test
    void genProjectInfoExtendInfo_withValue_shouldCreateSuccessfully() {
        GenProjectInfoExtendInfo info = new GenProjectInfoExtendInfo("requestName");
        assertEquals("requestName", info.requestName());
    }
}
