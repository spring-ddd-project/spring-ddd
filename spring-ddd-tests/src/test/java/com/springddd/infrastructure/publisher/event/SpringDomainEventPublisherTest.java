package com.springddd.infrastructure.publisher.event;

import com.springddd.domain.event.fixture.TestCreated;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class SpringDomainEventPublisherTest {

    @Test
    void shouldPublishSingleEvent() {
        ApplicationEventPublisher applicationEventPublisher = Mockito.mock(ApplicationEventPublisher.class);
        SpringDomainEventPublisher publisher = new SpringDomainEventPublisher(applicationEventPublisher);

        TestCreated event = TestCreated.of("1", "name1");

        StepVerifier.create(publisher.publish(event))
                .verifyComplete();

        verify(applicationEventPublisher, times(1)).publishEvent(event);
    }

    @Test
    void shouldPublishMultipleEvents() {
        ApplicationEventPublisher applicationEventPublisher = Mockito.mock(ApplicationEventPublisher.class);
        SpringDomainEventPublisher publisher = new SpringDomainEventPublisher(applicationEventPublisher);

        TestCreated event1 = TestCreated.of("1", "name1");
        TestCreated event2 = TestCreated.of("2", "name2");

        StepVerifier.create(publisher.publish(List.of(event1, event2)))
                .verifyComplete();

        verify(applicationEventPublisher, times(1)).publishEvent(event1);
        verify(applicationEventPublisher, times(1)).publishEvent(event2);
    }

    @Test
    void shouldCompleteWhenEventListIsEmpty() {
        ApplicationEventPublisher applicationEventPublisher = Mockito.mock(ApplicationEventPublisher.class);
        SpringDomainEventPublisher publisher = new SpringDomainEventPublisher(applicationEventPublisher);

        StepVerifier.create(publisher.publish(List.of()))
                .verifyComplete();

        verify(applicationEventPublisher, times(0)).publishEvent(Mockito.any());
    }
}
