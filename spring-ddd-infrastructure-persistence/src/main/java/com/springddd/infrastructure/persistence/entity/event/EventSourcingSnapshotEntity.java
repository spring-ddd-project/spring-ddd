package com.springddd.infrastructure.persistence.entity.event;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
@Data
@Table("sys_event_snapshot")
public class EventSourcingSnapshotEntity {

    @Id
    private Long id;
    private String entityId;
    private String aggregateType;
    private String entityData;
    private String eventId;
    private LocalDateTime eventTime;
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
