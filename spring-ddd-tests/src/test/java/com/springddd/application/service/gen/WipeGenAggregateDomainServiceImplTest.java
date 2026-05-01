package com.springddd.application.service.gen;

import com.springddd.infrastructure.persistence.r2dbc.GenAggregateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WipeGenAggregateDomainServiceImplTest {

    @Mock
    private GenAggregateRepository aggregateRepository;

    private WipeGenAggregateDomainServiceImpl wipeGenAggregateDomainService;

    @BeforeEach
    void setUp() {
        wipeGenAggregateDomainService = new WipeGenAggregateDomainServiceImpl(aggregateRepository);
    }

    @Test
    void wipe_shouldDeleteAggregatesByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        when(aggregateRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenAggregateDomainService.wipe(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipe_shouldComplete_whenIdsListIsEmpty() {
        List<Long> ids = Collections.emptyList();

        when(aggregateRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenAggregateDomainService.wipe(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipe_shouldDelete_whenSingleIdProvided() {
        List<Long> ids = Collections.singletonList(1L);

        when(aggregateRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenAggregateDomainService.wipe(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipe_shouldHandleLargeIdList() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);

        when(aggregateRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenAggregateDomainService.wipe(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
