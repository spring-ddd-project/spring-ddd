package com.springddd.infrastructure.persistence.entity;

import com.springddd.domain.util.IdGenerate;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("leaf_alloc")
public class LeafAllocEntity {

    private String bizTag;

    private Integer step;

    @Id
    @IdGenerate
    private Long id;

    private String description;

    @LastModifiedDate
    private LocalDateTime updateTime;

    private Long maxId;

    @Version
    private Integer version;

}
