package com.springddd.domain.role;

import lombok.Data;

@Data
public class RoleBasicInfo {

    private RoleName roleName;

    private RoleCode roleCode;

    private RoleDataScope roleDataScope;
}
