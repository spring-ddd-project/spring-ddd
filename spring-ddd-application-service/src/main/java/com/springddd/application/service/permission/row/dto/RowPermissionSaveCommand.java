package com.springddd.application.service.permission.row.dto;

import lombok.Data;

import java.util.List;

@Data
public class RowPermissionSaveCommand {
    private Long roleId;
    private Long menuId;
    private List<RowPermissionRuleCommand> rules;
}
