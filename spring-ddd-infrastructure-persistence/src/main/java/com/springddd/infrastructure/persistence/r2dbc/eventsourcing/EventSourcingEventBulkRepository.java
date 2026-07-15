package com.springddd.infrastructure.persistence.r2dbc.eventsourcing;

import com.springddd.infrastructure.persistence.entity.eventsourcing.EventSourcingEventEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventSourcingEventBulkRepository {

    private final DatabaseClient databaseClient;

    public Mono<Void> saveAll(List<EventSourcingEventEntity> entities) {
        if (entities.isEmpty()) {
            return Mono.empty();
        }
        StringBuilder sql = new StringBuilder(
                "INSERT INTO sys_event (event_id, entity_id, event_type, event_data, event_time, delete_status, version) VALUES ");
        int size = entities.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sql.append(", ");
            }
            sql.append("(:event_id").append(i)
                    .append(", :entity_id").append(i)
                    .append(", :event_type").append(i)
                    .append(", :event_data").append(i)
                    .append(", :event_time").append(i)
                    .append(", 0, 0)");
        }
        DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(sql.toString());
        for (int i = 0; i < size; i++) {
            EventSourcingEventEntity entity = entities.get(i);
            LocalDateTime eventTime = entity.getEventTime();
            spec = spec.bind("event_id" + i, entity.getEventId())
                    .bind("entity_id" + i, entity.getEntityId())
                    .bind("event_type" + i, entity.getEventType())
                    .bind("event_data" + i, entity.getEventData())
                    .bind("event_time" + i, eventTime);
        }
        return spec.then();
    }
}
