package com.springddd.domain.permission;

import java.util.*;

public record ColumnPermissionContext(
        String entityCode,
        Set<String> visibleColumns,
        MaskStrategy maskStrategy
) {

    public boolean isVisible(String columnName) {
        if (visibleColumns == null || visibleColumns.isEmpty()) {
            return true;
        }
        return visibleColumns.contains(columnName);
    }
}
