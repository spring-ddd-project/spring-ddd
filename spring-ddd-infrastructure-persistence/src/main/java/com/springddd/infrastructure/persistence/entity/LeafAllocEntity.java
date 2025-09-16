package com.springddd.infrastructure.persistence.entity;

import com.springddd.domain.util.IdGenerate;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("leaf_alloc")
public class LeafAllocEntity {

    private Long maxId;

    private String bizTag;

    private Integer step;

    private String description;

    @LastModifiedDate
    private LocalDateTime updateTime;

    @Id
    @IdGenerate
    private Long id;

    @Version
    private Integer version;

}
