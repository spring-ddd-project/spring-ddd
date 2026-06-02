package com.springddd.infrastructure.persistence.entity;

import com.springddd.domain.util.IdGenerate;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("leaf_alloc")
public class LeafAllocEntity {

    @Id
    @IdGenerate
    private Long id;

    private String bizTag;

    private Long maxId;

    private Integer step;

    private String description;

    @LastModifiedDate
    private LocalDateTime updateTime;

    @Version
    private Integer version;

    private Boolean deleteStatus;

    @CreatedBy
    private String createBy;

    @CreatedDate
    private LocalDateTime createTime;

    @LastModifiedBy
    private String updateBy;

    private Long deptId;
}
