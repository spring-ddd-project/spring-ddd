package com.springddd.infrastructure.persistence.entity.eventsourcing;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * 聚合根快照表。
 */
@Data
@Table("event_sourcing_snapshot")
public class EventSourcingSnapshotEntity {

    @Id
    private Long id;

    /**
     * 聚合根唯一标识
     */
    private String entityId;

    /**
     * 聚合根类型
     */
    private String aggregateType;

    /**
     * 序列化后的聚合根数据（JSON）
     */
    private String entityData;

    /**
     * 快照对应最后一个事件 ID
     */
    private String eventId;

    /**
     * 快照对应最后一个事件时间
     */
    private LocalDateTime eventTime;

    /**
     * 逻辑删除标记
     */
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
