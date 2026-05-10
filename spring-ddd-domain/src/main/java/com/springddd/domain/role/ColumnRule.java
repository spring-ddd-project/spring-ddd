package com.springddd.domain.role;

import java.util.List;

public record ColumnRule(String entityCode, String entityName, List<String> columns) {
}
