package com.springddd.domain.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnRule {
    private String entityCode;
    private String entityName;
    private List<String> columns;
}




















