package com.springddd.infrastructure.persistence.entity;

import com.springddd.domain.permission.DataPermissionEntity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("leaf_worker")
@DataPermissionEntity(name = "Worker节点")
public class LeafWorkerEntity {

    @Id
    private Long id;

    private Integer workerId;

    private Integer datacenterId;

    private String ip;

    private Integer port;

    private Long lastTimestamp;

    private LocalDateTime updateTime;

    private Boolean deleteStatus;
}
