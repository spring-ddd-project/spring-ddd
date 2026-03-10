package com.springddd.infrastructure.persistence.entity;

import com.springddd.domain.util.IdGenerate;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
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
}
