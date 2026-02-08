package com.springddd.application.service.role.dto;

import com.springddd.domain.role.DataPermission;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysRoleView implements Serializable {

    private Long id;

    private String roleName;

    private String roleCode;

    private String roleDesc;

    private Integer dataScope;

    private Boolean roleStatus;

    private Boolean ownerStatus;

    private Long deptId;

    private DataPermission dataPermission;

    private Boolean deleteStatus;

    private Integer version;
}












































