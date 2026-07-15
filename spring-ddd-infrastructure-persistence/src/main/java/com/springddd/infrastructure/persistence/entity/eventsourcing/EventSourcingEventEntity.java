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
 * 领域事件表。
 */
@Data
@Table("event_sourcing_event")
public class EventSourcingEventEntity {

    @Id
    private Long id;

    /**
     * 事件唯一标识
     */
    private String eventId;

    /**
     * 聚合根唯一标识
     */
    private String entityId;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 序列化后的事件数据（JSON）
     */
    private String eventData;

    /**
     * 事件发生时间
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
