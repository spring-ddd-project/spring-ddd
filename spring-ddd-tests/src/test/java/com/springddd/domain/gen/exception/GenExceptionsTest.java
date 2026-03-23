package com.springddd.domain.gen.exception;

import com.springddd.domain.DomainException;
import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenExceptionsTest {

    @Test
    void aggregateNullException_shouldHaveCorrectErrorCode() {
        AggregateNullException ex = new AggregateNullException();
        assertEquals(ErrorCode.GEN_INFO_AGGREGATE_NULL, ex.getErrorCode());
        assertEquals(1520, ex.getCode());
        assertEquals("error.gen.info.aggregate.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void classNameNullException_shouldHaveCorrectErrorCode() {
        ClassNameNullException ex = new ClassNameNullException();
        assertEquals(ErrorCode.GEN_INFO_CLASS_NAME_NULL, ex.getErrorCode());
        assertEquals(1502, ex.getCode());
        assertEquals("error.gen.info.class.name.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void columnCommentNullException_shouldHaveCorrectErrorCode() {
        ColumnCommentNullException ex = new ColumnCommentNullException();
        assertEquals(ErrorCode.GEN_INFO_COLUMN_COMMENT_NULL, ex.getErrorCode());
        assertEquals(1508, ex.getCode());
        assertEquals("error.gen.info.column.comment.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void columnKeyNullException_shouldHaveCorrectErrorCode() {
        ColumnKeyNullException ex = new ColumnKeyNullException();
        assertEquals(ErrorCode.GEN_INFO_COLUMN_KEY_NULL, ex.getErrorCode());
        assertEquals(1505, ex.getCode());
        assertEquals("error.gen.info.column.key.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void columnNameNullException_shouldHaveCorrectErrorCode() {
        ColumnNameNullException ex = new ColumnNameNullException();
        assertEquals(ErrorCode.GEN_INFO_COLUMN_NAME_NULL, ex.getErrorCode());
        assertEquals(1506, ex.getCode());
        assertEquals("error.gen.info.column.name.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void columnTypeNullException_shouldHaveCorrectErrorCode() {
        ColumnTypeNullException ex = new ColumnTypeNullException();
        assertEquals(ErrorCode.GEN_INFO_COLUMN_TYPE_NULL, ex.getErrorCode());
        assertEquals(1507, ex.getCode());
        assertEquals("error.gen.info.column.type.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void componentTypeNullException_shouldHaveCorrectErrorCode() {
        ComponentTypeNullException ex = new ComponentTypeNullException();
        assertEquals(ErrorCode.GEN_BIND_COMPONENT_TYPE_NULL, ex.getErrorCode());
        assertEquals(1521, ex.getCode());
        assertEquals("error.gen.bind.component.type.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void databaseNameNullException_shouldHaveCorrectErrorCode() {
        DatabaseNameNullException ex = new DatabaseNameNullException();
        assertEquals(ErrorCode.GEN_INFO_DATABASE_NAME_NULL, ex.getErrorCode());
        assertEquals(1522, ex.getCode());
        assertEquals("error.gen.info.database.name.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void dictIdNullException_shouldHaveCorrectErrorCode() {
        DictIdNullException ex = new DictIdNullException();
        assertEquals(ErrorCode.GEN_INFO_DICT_ID_NULL, ex.getErrorCode());
        assertEquals(1511, ex.getCode());
        assertEquals("error.gen.info.dict.id.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void domainMaskNullException_shouldHaveCorrectErrorCode() {
        DomainMaskNullException ex = new DomainMaskNullException();
        assertEquals(ErrorCode.GEN_AGGREGATE_DOMAIN_MASK_NULL, ex.getErrorCode());
        assertEquals(1523, ex.getCode());
        assertEquals("error.gen.aggregate.domain.mask.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void filterComponentNullException_shouldHaveCorrectErrorCode() {
        FilterComponentNullException ex = new FilterComponentNullException();
        assertEquals(ErrorCode.GEN_INFO_FILTER_COMPONENT_NULL, ex.getErrorCode());
        assertEquals(1515, ex.getCode());
        assertEquals("error.gen.info.filter.component.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void filterNullException_shouldHaveCorrectErrorCode() {
        FilterNullException ex = new FilterNullException();
        assertEquals(ErrorCode.GEN_INFO_FILTER_NULL, ex.getErrorCode());
        assertEquals(1514, ex.getCode());
        assertEquals("error.gen.info.filter.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void filterTypeNullException_shouldHaveCorrectErrorCode() {
        FilterTypeNullException ex = new FilterTypeNullException();
        assertEquals(ErrorCode.GEN_INFO_FILTER_TYPE_NULL, ex.getErrorCode());
        assertEquals(1516, ex.getCode());
        assertEquals("error.gen.info.filter.type.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void formComponentNullException_shouldHaveCorrectErrorCode() {
        FormComponentNullException ex = new FormComponentNullException();
        assertEquals(ErrorCode.GEN_INFO_FORM_COMPONENT_NULL, ex.getErrorCode());
        assertEquals(1517, ex.getCode());
        assertEquals("error.gen.info.form.component.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void formRequiredNullException_shouldHaveCorrectErrorCode() {
        FormRequiredNullException ex = new FormRequiredNullException();
        assertEquals(ErrorCode.GEN_INFO_FORM_REQUIRED_NULL, ex.getErrorCode());
        assertEquals(1519, ex.getCode());
        assertEquals("error.gen.info.form.required.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void formVisibleNullException_shouldHaveCorrectErrorCode() {
        FormVisibleNullException ex = new FormVisibleNullException();
        assertEquals(ErrorCode.GEN_INFO_FORM_VISIBLE_NULL, ex.getErrorCode());
        assertEquals(1518, ex.getCode());
        assertEquals("error.gen.info.form.visible.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void i18nEnNullException_shouldHaveCorrectErrorCode() {
        I18nEnNullException ex = new I18nEnNullException();
        assertEquals(ErrorCode.GEN_I18N_EN_NULL, ex.getErrorCode());
        assertEquals(1529, ex.getCode());
        assertEquals("error.gen.i18n.en.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void i18nLocaleNullException_shouldHaveCorrectErrorCode() {
        I18nLocaleNullException ex = new I18nLocaleNullException();
        assertEquals(ErrorCode.GEN_I18N_LOCALE_NULL, ex.getErrorCode());
        assertEquals(1530, ex.getCode());
        assertEquals("error.gen.i18n.locale.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void javaEntityNullException_shouldHaveCorrectErrorCode() {
        JavaEntityNullException ex = new JavaEntityNullException();
        assertEquals(ErrorCode.GEN_INFO_JAVA_ENTITY_NULL, ex.getErrorCode());
        assertEquals(1509, ex.getCode());
        assertEquals("error.gen.info.java.entity.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void javaTypeNullException_shouldHaveCorrectErrorCode() {
        JavaTypeNullException ex = new JavaTypeNullException();
        assertEquals(ErrorCode.GEN_INFO_JAVA_TYPE_NULL, ex.getErrorCode());
        assertEquals(1510, ex.getCode());
        assertEquals("error.gen.info.java.type.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void moduleNameNullException_shouldHaveCorrectErrorCode() {
        ModuleNameNullException ex = new ModuleNameNullException();
        assertEquals(ErrorCode.GEN_INFO_MODULE_NAME_NULL, ex.getErrorCode());
        assertEquals(1531, ex.getCode());
        assertEquals("error.gen.info.module.name.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void objectNameNullException_shouldHaveCorrectErrorCode() {
        ObjectNameNullException ex = new ObjectNameNullException();
        assertEquals(ErrorCode.GEN_AGGREGATE_OBJECT_NAME_NULL, ex.getErrorCode());
        assertEquals(1524, ex.getCode());
        assertEquals("error.gen.aggregate.object.name.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void objectTypeNullException_shouldHaveCorrectErrorCode() {
        ObjectTypeNullException ex = new ObjectTypeNullException();
        assertEquals(ErrorCode.GEN_AGGREGATE_OBJECT_TYPE_NULL, ex.getErrorCode());
        assertEquals(1525, ex.getCode());
        assertEquals("error.gen.aggregate.object.type.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void orderNullException_shouldHaveCorrectErrorCode() {
        OrderNullException ex = new OrderNullException();
        assertEquals(ErrorCode.GEN_INFO_ORDER_NULL, ex.getErrorCode());
        assertEquals(1513, ex.getCode());
        assertEquals("error.gen.info.order.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void packageNameNullException_shouldHaveCorrectErrorCode() {
        PackageNameNullException ex = new PackageNameNullException();
        assertEquals(ErrorCode.GEN_INFO_PACKAGE_NAME_NULL, ex.getErrorCode());
        assertEquals(1500, ex.getCode());
        assertEquals("error.gen.info.package.name.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void projectNameNullException_shouldHaveCorrectErrorCode() {
        ProjectNameNullException ex = new ProjectNameNullException();
        assertEquals(ErrorCode.GEN_INFO_PROJECT_NAME_NULL, ex.getErrorCode());
        assertEquals(1532, ex.getCode());
        assertEquals("error.gen.info.project.name.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void requestNameNullException_shouldHaveCorrectErrorCode() {
        RequestNameNullException ex = new RequestNameNullException();
        assertEquals(ErrorCode.GEN_INFO_REQUEST_NAME_NULL, ex.getErrorCode());
        assertEquals(1503, ex.getCode());
        assertEquals("error.gen.info.request.name.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void tableNameNullException_shouldHaveCorrectErrorCode() {
        TableNameNullException ex = new TableNameNullException();
        assertEquals(ErrorCode.GEN_INFO_TABLE_NAME_NULL, ex.getErrorCode());
        assertEquals(1501, ex.getCode());
        assertEquals("error.gen.info.table.name.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void templateContentNullException_shouldHaveCorrectErrorCode() {
        TemplateContentNullException ex = new TemplateContentNullException();
        assertEquals(ErrorCode.GEN_TEMPLATE_CONTENT_NULL, ex.getErrorCode());
        assertEquals(1527, ex.getCode());
        assertEquals("error.gen.template.content.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void templateNameNullException_shouldHaveCorrectErrorCode() {
        TemplateNameNullException ex = new TemplateNameNullException();
        assertEquals(ErrorCode.GEN_TEMPLATE_NAME_NULL, ex.getErrorCode());
        assertEquals(1526, ex.getCode());
        assertEquals("error.gen.template.name.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void typeScriptTypeNullException_shouldHaveCorrectErrorCode() {
        TypeScriptTypeNullException ex = new TypeScriptTypeNullException();
        assertEquals(ErrorCode.GEN_BIND_TYPESCRIPT_TYPE_NULL, ex.getErrorCode());
        assertEquals(1528, ex.getCode());
        assertEquals("error.gen.bind.typescript.type.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void valueObjectNullException_shouldHaveCorrectErrorCode() {
        ValueObjectNullException ex = new ValueObjectNullException();
        assertEquals(ErrorCode.GEN_INFO_VALUE_OBJECT_NULL, ex.getErrorCode());
        assertEquals(1504, ex.getCode());
        assertEquals("error.gen.info.value.object.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }

    @Test
    void visibleNullException_shouldHaveCorrectErrorCode() {
        VisibleNullException ex = new VisibleNullException();
        assertEquals(ErrorCode.GEN_INFO_VISIBLE_NULL, ex.getErrorCode());
        assertEquals(1512, ex.getCode());
        assertEquals("error.gen.info.visible.null", ex.getMessageKey());
        assertTrue(ex instanceof DomainException);
    }
}
