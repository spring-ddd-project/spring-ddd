package com.springddd.infrastructure.persistence.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("leaf_alloc")
public class LeafAllocEntity {

    private String bizTag;

    private Long maxId;

    private Integer step;

    private String description;

    private LocalDateTime updateTime;
}
