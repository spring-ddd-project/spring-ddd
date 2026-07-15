package com.springddd.application.service.permission.row.dto;

import lombok.Data;

@Data
public class RowPermissionView {
    private Long id;
    private Long roleId;
    private Long menuId;
    private Integer scopeType;
    private Long targetId;
    private String targetName;
}
