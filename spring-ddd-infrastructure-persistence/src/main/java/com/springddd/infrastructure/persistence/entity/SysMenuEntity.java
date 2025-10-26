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

    private String path;

    private String component;

    private String redirect;

    private String permission;

    private Integer sortOrder;

    private String title;

    private Boolean affixTab;

    private Boolean noBasicLayout;

    private String icon;

    private Integer menuType;

    private Boolean visible;

    private Boolean embedded;

    private Boolean menuStatus;

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
