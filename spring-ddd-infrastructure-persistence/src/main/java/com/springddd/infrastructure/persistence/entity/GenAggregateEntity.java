package com.springddd.infrastructure.persistence.entity;

import com.springddd.domain.util.LeafId;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("gen_aggregate")
public class GenAggregateEntity {

    @Id
    @LeafId
    private Long id;

    private Long infoId;

    private String objectName;

    private String objectValue;

    private Byte objectType;

    private Boolean hasCreated;

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
