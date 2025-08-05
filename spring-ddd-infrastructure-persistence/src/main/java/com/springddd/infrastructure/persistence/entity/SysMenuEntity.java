package com.springddd.infrastructure.persistence.entity;

import com.springddd.domain.util.IdGenerate;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("sys_menu")
public class SysMenuEntity {

    @Id
    @IdGenerate
    private Long id;

    private Long parentId;

    private String name;

    private String permission;

    private String path;

    private String icon;

    private String visible;

    private Integer sortOrder;

    private String embedded;

    private String menuType;

    private String menuStatus;

    private Long deptId;

    @CreatedBy
    private String createBy;

    @CreatedDate
    private LocalDateTime createTime;

    @LastModifiedBy
    private String updateBy;

    @LastModifiedDate
    private LocalDateTime updateTime;

    private String deleteStatus;

    @Version
    private String version;
}
