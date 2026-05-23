package com.springddd.infrastructure.persistence.entity;

import com.springddd.domain.permission.DataPermissionEntity;

import com.springddd.domain.util.LeafId;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("sys_dict_item")
@DataPermissionEntity(name = "字典项管理")
public class SysDictItemEntity {

    @Id
    @LeafId
    private Long id;

    private Long dictId;

    private String itemLabel;

    private Integer itemValue;

    private Integer sortOrder;

    private Boolean itemStatus;

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
