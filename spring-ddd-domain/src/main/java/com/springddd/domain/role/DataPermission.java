package com.springddd.domain.role;

import java.util.List;

public record DataPermission(RowScope rowScope, List<ColumnRule> columnRules, Integer dataScope, List<Long> deptIds) {
}
