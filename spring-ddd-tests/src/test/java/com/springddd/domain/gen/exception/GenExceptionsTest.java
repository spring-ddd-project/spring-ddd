package com.springddd.domain.gen.exception;

import com.springddd.domain.gen.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenExceptionsTest {

    @Test
    void shouldThrowAggregateNullException() {
        assertThrows(AggregateNullException.class, () -> {
            throw new AggregateNullException();
        });
    }

    @Test
    void shouldThrowClassNameNullException() {
        assertThrows(ClassNameNullException.class, () -> {
            throw new ClassNameNullException();
        });
    }

    @Test
    void shouldThrowColumnCommentNullException() {
        assertThrows(ColumnCommentNullException.class, () -> {
            throw new ColumnCommentNullException();
        });
    }

    @Test
    void shouldThrowColumnKeyNullException() {
        assertThrows(ColumnKeyNullException.class, () -> {
            throw new ColumnKeyNullException();
        });
    }

    @Test
    void shouldThrowColumnNameNullException() {
        assertThrows(ColumnNameNullException.class, () -> {
            throw new ColumnNameNullException();
        });
    }

    @Test
    void shouldThrowColumnTypeNullException() {
        assertThrows(ColumnTypeNullException.class, () -> {
            throw new ColumnTypeNullException();
        });
    }

    @Test
    void shouldThrowComponentTypeNullException() {
        assertThrows(ComponentTypeNullException.class, () -> {
            throw new ComponentTypeNullException();
        });
    }

    @Test
    void shouldThrowDatabaseNameNullException() {
        assertThrows(DatabaseNameNullException.class, () -> {
            throw new DatabaseNameNullException();
        });
    }

    @Test
    void shouldThrowDictIdNullException() {
        assertThrows(DictIdNullException.class, () -> {
            throw new DictIdNullException();
        });
    }

    @Test
    void shouldThrowDomainMaskNullException() {
        assertThrows(DomainMaskNullException.class, () -> {
            throw new DomainMaskNullException();
        });
    }

    @Test
    void shouldThrowFilterComponentNullException() {
        assertThrows(FilterComponentNullException.class, () -> {
            throw new FilterComponentNullException();
        });
    }

    @Test
    void shouldThrowFilterNullException() {
        assertThrows(FilterNullException.class, () -> {
            throw new FilterNullException();
        });
    }

    @Test
    void shouldThrowFilterTypeNullException() {
        assertThrows(FilterTypeNullException.class, () -> {
            throw new FilterTypeNullException();
        });
    }

    @Test
    void shouldThrowFormComponentNullException() {
        assertThrows(FormComponentNullException.class, () -> {
            throw new FormComponentNullException();
        });
    }

    @Test
    void shouldThrowFormRequiredNullException() {
        assertThrows(FormRequiredNullException.class, () -> {
            throw new FormRequiredNullException();
        });
    }

    @Test
    void shouldThrowFormVisibleNullException() {
        assertThrows(FormVisibleNullException.class, () -> {
            throw new FormVisibleNullException();
        });
    }

    @Test
    void shouldThrowI18nEnNullException() {
        assertThrows(I18nEnNullException.class, () -> {
            throw new I18nEnNullException();
        });
    }

    @Test
    void shouldThrowI18nLocaleNullException() {
        assertThrows(I18nLocaleNullException.class, () -> {
            throw new I18nLocaleNullException();
        });
    }

    @Test
    void shouldThrowJavaEntityNullException() {
        assertThrows(JavaEntityNullException.class, () -> {
            throw new JavaEntityNullException();
        });
    }

    @Test
    void shouldThrowJavaTypeNullException() {
        assertThrows(JavaTypeNullException.class, () -> {
            throw new JavaTypeNullException();
        });
    }

    @Test
    void shouldThrowModuleNameNullException() {
        assertThrows(ModuleNameNullException.class, () -> {
            throw new ModuleNameNullException();
        });
    }

    @Test
    void shouldThrowObjectNameNullException() {
        assertThrows(ObjectNameNullException.class, () -> {
            throw new ObjectNameNullException();
        });
    }

    @Test
    void shouldThrowObjectTypeNullException() {
        assertThrows(ObjectTypeNullException.class, () -> {
            throw new ObjectTypeNullException();
        });
    }

    @Test
    void shouldThrowOrderNullException() {
        assertThrows(OrderNullException.class, () -> {
            throw new OrderNullException();
        });
    }

    @Test
    void shouldThrowPackageNameNullException() {
        assertThrows(PackageNameNullException.class, () -> {
            throw new PackageNameNullException();
        });
    }

    @Test
    void shouldThrowProjectNameNullException() {
        assertThrows(ProjectNameNullException.class, () -> {
            throw new ProjectNameNullException();
        });
    }

    @Test
    void shouldThrowRequestNameNullException() {
        assertThrows(RequestNameNullException.class, () -> {
            throw new RequestNameNullException();
        });
    }

    @Test
    void shouldThrowTableNameNullException() {
        assertThrows(TableNameNullException.class, () -> {
            throw new TableNameNullException();
        });
    }

    @Test
    void shouldThrowTemplateContentNullException() {
        assertThrows(TemplateContentNullException.class, () -> {
            throw new TemplateContentNullException();
        });
    }

    @Test
    void shouldThrowTemplateNameNullException() {
        assertThrows(TemplateNameNullException.class, () -> {
            throw new TemplateNameNullException();
        });
    }

    @Test
    void shouldThrowTypeScriptTypeNullException() {
        assertThrows(TypeScriptTypeNullException.class, () -> {
            throw new TypeScriptTypeNullException();
        });
    }

    @Test
    void shouldThrowValueObjectNullException() {
        assertThrows(ValueObjectNullException.class, () -> {
            throw new ValueObjectNullException();
        });
    }

    @Test
    void shouldThrowVisibleNullException() {
        assertThrows(VisibleNullException.class, () -> {
            throw new VisibleNullException();
        });
    }

    @Test
    void shouldVerifyExceptionMessages() {
        AggregateNullException ex = new AggregateNullException();
        assertNotNull(ex.getMessage());
        assertNotNull(ex.getMessageKey());
        assertNotNull(ex.getErrorCode());
    }

    @Test
    void shouldVerifyValueObjectExceptions() {
        assertThrows(ObjectNameNullException.class, () -> {
            new GenAggregateValueObject(null, "value", (byte) 1);
        });

        assertThrows(ValueObjectNullException.class, () -> {
            new GenAggregateValueObject("name", null, (byte) 1);
        });

        assertThrows(ObjectTypeNullException.class, () -> {
            new GenAggregateValueObject("name", "value", null);
        });
    }

    @Test
    void shouldVerifyPropExceptions() {
        assertThrows(ColumnNameNullException.class, () -> {
            new Prop("key", null, "type", "comment", "javaType", "entity");
        });

        assertThrows(ColumnTypeNullException.class, () -> {
            new Prop("key", "name", null, "comment", "javaType", "entity");
        });

        assertThrows(ColumnCommentNullException.class, () -> {
            new Prop("key", "name", "type", null, "javaType", "entity");
        });

        assertThrows(JavaTypeNullException.class, () -> {
            new Prop("key", "name", "type", "comment", null, "entity");
        });

        assertThrows(JavaEntityNullException.class, () -> {
            new Prop("key", "name", "type", "comment", "javaType", null);
        });
    }

    @Test
    void shouldVerifyTableExceptions() {
        assertThrows(VisibleNullException.class, () -> {
            new Table(null, true, false, null, null);
        });

        assertThrows(OrderNullException.class, () -> {
            new Table(true, null, false, null, null);
        });

        assertThrows(FilterNullException.class, () -> {
            new Table(true, true, null, null, null);
        });

        assertThrows(FilterComponentNullException.class, () -> {
            new Table(true, true, true, null, null);
        });
    }

    @Test
    void shouldVerifyFormExceptions() {
        assertThrows(FormComponentNullException.class, () -> {
            new Form(null, true, false);
        });

        assertThrows(FormVisibleNullException.class, () -> {
            new Form((byte) 1, null, false);
        });

        assertThrows(FormRequiredNullException.class, () -> {
            new Form((byte) 1, true, null);
        });
    }

    @Test
    void shouldVerifyI18nExceptions() {
        assertThrows(I18nEnNullException.class, () -> {
            new I18n(null, "locale");
        });
    }

    @Test
    void shouldVerifyProjectInfoExceptions() {
        assertThrows(TableNameNullException.class, () -> {
            new ProjectInfo(null, "package", "class", "module", "project");
        });

        assertThrows(PackageNameNullException.class, () -> {
            new ProjectInfo("table", null, "class", "module", "project");
        });

        assertThrows(ClassNameNullException.class, () -> {
            new ProjectInfo("table", "package", null, "module", "project");
        });

        assertThrows(ModuleNameNullException.class, () -> {
            new ProjectInfo("table", "package", "class", null, "project");
        });

        assertThrows(ProjectNameNullException.class, () -> {
            new ProjectInfo("table", "package", "class", "module", null);
        });
    }

    @Test
    void shouldVerifyTemplateInfoExceptions() {
        assertThrows(TemplateNameNullException.class, () -> {
            new TemplateInfo(null, "content");
        });

        assertThrows(TemplateContentNullException.class, () -> {
            new TemplateInfo("name", null);
        });
    }

    @Test
    void shouldVerifyGenColumnBindBasicInfoExceptions() {
        assertThrows(ColumnTypeNullException.class, () -> {
            new GenColumnBindBasicInfo(null, "entity", (byte) 1, (byte) 1);
        });

        assertThrows(JavaTypeNullException.class, () -> {
            new GenColumnBindBasicInfo("type", null, (byte) 1, (byte) 1);
        });

        assertThrows(ComponentTypeNullException.class, () -> {
            new GenColumnBindBasicInfo("type", "entity", null, (byte) 1);
        });

        assertThrows(TypeScriptTypeNullException.class, () -> {
            new GenColumnBindBasicInfo("type", "entity", (byte) 1, null);
        });
    }
}
