package com.springddd.infrastructure.persistence.entity;

import com.springddd.domain.util.IdGenerate;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("gen_project_info")
public class GenProjectInfoEntity {

    @Id
    @IdGenerate
    private Long id;

    private String tableName;

    private String packageName;

    private String className;

    private String requestName;

    private String moduleName;

    private String projectName;

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
