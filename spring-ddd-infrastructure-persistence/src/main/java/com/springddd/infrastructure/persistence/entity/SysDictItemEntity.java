package com.springddd.infrastructure.persistence.entity;

import com.springddd.domain.util.IdGenerate;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("sys_dict_item")
public class SysDictItemEntity {

    @Id
    @IdGenerate
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
