package com.springddd.infrastructure.persistence.entity;

import com.springddd.domain.util.IdGenerate;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("sys_dept")
public class SysDeptEntity {

    @Id
    @IdGenerate
    private Long id;

    private Long parentId;

    private String deptName;

    private Integer sortOrder;

    private Boolean deptStatus;

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
