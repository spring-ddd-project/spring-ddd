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

    private Integer order;

    private String title;

    private String affixTab;

    private String noBasicLayout;

    private String icon;

    private String menuType;

    private String visible;

    private String embedded;

    private String menuStatus;

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
