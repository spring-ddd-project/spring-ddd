package com.springddd.infrastructure.persistence.entity;

import com.springddd.domain.util.IdGenerate;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("sys_user_role")
public class SysUserRoleEntity {

    @Id
    @IdGenerate
    private Long id;

    private Long userId;

    private Long roleId;

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
