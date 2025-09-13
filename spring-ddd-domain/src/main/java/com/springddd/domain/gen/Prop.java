package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.*;
import org.springframework.util.ObjectUtils;

public record Prop(String propColumnKey, String propColumnName, String propColumnType, String propColumnComment, String propJavaType, String propJavaEntity) {

    public Prop {
        if (ObjectUtils.isEmpty(propColumnName)) {
            throw new ColumnNameNullException();
        }
        if (ObjectUtils.isEmpty(propColumnType)) {
            throw new ColumnTypeNullException();
        }
        if (ObjectUtils.isEmpty(propColumnComment)) {
            throw new ColumnCommentNullException();
        }
        if (ObjectUtils.isEmpty(propJavaType)) {
            throw new JavaTypeNullException();
        }
        if (ObjectUtils.isEmpty(propJavaEntity)) {
            throw new JavaEntityNullException();
        }
    }
}
