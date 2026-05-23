package com.springddd.infrastructure.persistence.entity;

import com.springddd.domain.permission.DataPermissionEntity;

import com.springddd.domain.util.LeafId;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("sys_role_menu")
@DataPermissionEntity(name = "角色菜单关联")
public class SysRoleMenuEntity {

    @Id
    @LeafId
    private Long id;

    private Long roleId;

    private Long menuId;

    private Long deptId;

    private Boolean deleteStatus;

    @CreatedBy
    private String createBy;

    @CreatedDate
    private LocalDateTime createTime;

    @LastModifiedBy
    private String updateBy;

    @LastModifiedDate
    private LocalDateTime updateTime;

    @Version
    private Integer version;
}
