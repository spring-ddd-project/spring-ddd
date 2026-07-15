package com.springddd.application.service.eventsourcing;

import com.springddd.domain.eventsourcing.EventSourcingRepository;
import com.springddd.domain.eventsourcing.fixture.TestAggregateRoot;
import com.springddd.domain.eventsourcing.fixture.TestCreated;
import com.springddd.infrastructure.publisher.eventsourcing.DomainEventPublisher;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

class AbstractEventSourcingCommandServiceTest {

    @Test
    void shouldLoadExecuteSaveAndPublishEvents() {
        @SuppressWarnings("unchecked")
        EventSourcingRepository<TestAggregateRoot.TestId, TestAggregateRoot> repository = Mockito.mock(EventSourcingRepository.class);
        DomainEventPublisher publisher = Mockito.mock(DomainEventPublisher.class);

        TestAggregateRoot aggregate = new TestAggregateRoot();
        aggregate.apply(TestCreated.of("1", "name1"));

        when(repository.load(new TestAggregateRoot.TestId("1"))).thenReturn(Mono.just(aggregate));
        doReturn(Mono.<Void>empty()).when(repository).save(any(TestAggregateRoot.class));
        when(publisher.publish(anyList())).thenReturn(Mono.empty());

        TestCommandService service = new TestCommandService(repository, publisher);

        StepVerifier.create(service.changeCount(new TestAggregateRoot.TestId("1"), 5))
                .verifyComplete();
    }

    static class TestCommandService
            extends AbstractEventSourcingCommandService<TestAggregateRoot.TestId, TestAggregateRoot> {

        TestCommandService(EventSourcingRepository<TestAggregateRoot.TestId, TestAggregateRoot> repository,
                           DomainEventPublisher publisher) {
            super(repository, publisher);
        }

        Mono<Void> changeCount(TestAggregateRoot.TestId id, int count) {
            return execute(id, aggregate -> aggregate.changeCount(count));
        }
    }
}
