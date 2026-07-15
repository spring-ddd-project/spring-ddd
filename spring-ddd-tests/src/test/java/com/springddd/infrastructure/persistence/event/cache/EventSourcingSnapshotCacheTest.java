package com.springddd.infrastructure.persistence.event.cache;

import com.springddd.infrastructure.persistence.entity.event.EventSourcingSnapshotEntity;
import com.springddd.infrastructure.persistence.r2dbc.event.EventSourcingSnapshotRepository;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class EventSourcingSnapshotCacheTest {

    @Test
    void cacheShouldReturnSameValueWithoutRepeatedRepositoryCall() {
        EventSourcingSnapshotRepository repository = mock(EventSourcingSnapshotRepository.class);
        EventSourcingSnapshotEntity entity = new EventSourcingSnapshotEntity();
        entity.setEntityId("1");
        entity.setEntityData("{}");
        entity.setEventTime(LocalDateTime.now());
        when(repository.findByEntityId(anyString())).thenReturn(Mono.just(entity));

        EventSourcingSnapshotCache cache = new EventSourcingSnapshotCache(repository);

        StepVerifier.create(cache.findByEntityId("1"))
                .expectNextMatches(e -> "1".equals(e.getEntityId()))
                .verifyComplete();
        StepVerifier.create(cache.findByEntityId("1"))
                .expectNextMatches(e -> "1".equals(e.getEntityId()))
                .verifyComplete();

        verify(repository, times(1)).findByEntityId("1");
    }
}
