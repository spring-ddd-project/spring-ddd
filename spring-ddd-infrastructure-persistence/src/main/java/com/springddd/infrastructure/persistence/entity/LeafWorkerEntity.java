package com.springddd.infrastructure.persistence.entity;

import com.springddd.domain.util.IdGenerate;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("leaf_worker")
public class LeafWorkerEntity {

    @Column("worker_id")
    private Integer workerId;

    @Column("datacenter_id")
    private Integer datacenterId;

    @Column("ip")
    private String ip;

    @Column("last_timestamp")
    private Long lastTimestamp;

    @Column("port")
    private Integer port;

    @Id
    @IdGenerate
    @Column("id")
    private Long id;

    @Column("delete_status")
    private Boolean deleteStatus;

    @LastModifiedDate
    @Column("update_time")
    private LocalDateTime updateTime;

}
