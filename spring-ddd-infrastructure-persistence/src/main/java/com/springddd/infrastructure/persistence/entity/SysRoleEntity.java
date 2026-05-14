package com.springddd.infrastructure.persistence.entity;

import com.springddd.domain.util.LeafId;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * @author henrikabel25
 *
 * sys_role data model
 */
@Data
@Table("sys_role")
public class SysRoleEntity {

    @Id
    @LeafId
    private Long id;

    private String roleName;

    private String roleCode;

    private String roleDesc;

    private Integer dataScope;

    private String dataPermission;

    private Boolean roleStatus;

    private Boolean ownerStatus;

    private Long deptId;

    @CreatedBy
    private String createBy;

    @CreatedDate
    private LocalDateTime createTime;

    @LastModifiedBy
    private String updateBy;

    @LastModifiedDate
    private LocalDateTime updateTime;

    private Boolean deleteStatus;

    @Version
    private Integer version;
}
