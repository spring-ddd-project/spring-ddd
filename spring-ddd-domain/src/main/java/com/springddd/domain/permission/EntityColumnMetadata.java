package com.springddd.domain.permission;

import java.util.List;

public record EntityColumnMetadata(String entityCode, String entityName, List<ColumnInfo> columns) {

    public record ColumnInfo(String field, String label) {
    }
}
