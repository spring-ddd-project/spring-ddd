package com.springddd.infrastructure.persistence.entity;

import com.springddd.domain.util.IdGenerate;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * @author henrikabel25
 */
@Data
@Table("sys_user")
public class SysUserEntity {

    @Id
    @IdGenerate
    private Long id;

    private String username;

    private String password;

    private String phone;

    private String avatar;

    private String email;

    private Boolean sex;

    private Boolean lockStatus;

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
