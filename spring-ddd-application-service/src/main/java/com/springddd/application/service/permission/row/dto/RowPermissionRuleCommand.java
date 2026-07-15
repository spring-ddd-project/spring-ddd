package com.springddd.application.service.permission.row.dto;

import lombok.Data;

@Data
public class RowPermissionRuleCommand {
    private Long id;
    private Integer scopeType;
    private Long targetId;
}
