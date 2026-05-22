package com.springddd.infrastructure.persistence.entity;

import com.springddd.domain.permission.DataPermissionEntity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("leaf_alloc")
@DataPermissionEntity(name = "号段分配")
public class LeafAllocEntity {

    @Id
    private Long id;

    private String bizTag;

    private Long maxId;

    private Integer step;

    private String description;

    private LocalDateTime updateTime;

    private Integer version;

    private Boolean deleteStatus;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private Long deptId;
}
