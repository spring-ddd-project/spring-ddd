package com.springddd.infrastructure.persistence.entity;

import com.springddd.domain.permission.DataPermissionEntity;

import com.springddd.domain.util.LeafId;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("sys_dict")
@DataPermissionEntity(name = "字典管理")
public class SysDictEntity {

    @Id
    @LeafId
    private Long id;

    private String dictName;

    private String dictCode;

    private Integer sortOrder;

    private Boolean dictStatus;

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
