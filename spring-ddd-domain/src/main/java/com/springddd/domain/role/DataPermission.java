package com.springddd.domain.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataPermission {
    private RowScope rowScope;
    private List<ColumnRule> columnRules;
}




















































