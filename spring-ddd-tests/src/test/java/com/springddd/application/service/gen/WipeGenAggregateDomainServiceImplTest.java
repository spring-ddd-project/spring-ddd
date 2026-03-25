package com.springddd.application.service.gen;

import com.springddd.application.service.gen.WipeGenAggregateDomainServiceImpl;
import com.springddd.infrastructure.persistence.r2dbc.GenAggregateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeGenAggregateDomainServiceImplTest {

    @Mock
    private GenAggregateRepository aggregateRepository;

    @InjectMocks
    private WipeGenAggregateDomainServiceImpl wipeGenAggregateDomainService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void wipe_shouldDeleteAggregates() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        when(aggregateRepository.deleteAllById(ids)).thenReturn(Mono.empty().then());

        StepVerifier.create(wipeGenAggregateDomainService.wipe(ids))
                .verifyComplete();

        verify(aggregateRepository).deleteAllById(ids);
    }

    @Test
    void wipe_shouldHandleEmptyList() {
        List<Long> ids = Collections.emptyList();

        when(aggregateRepository.deleteAllById(ids)).thenReturn(Mono.empty().then());

        StepVerifier.create(wipeGenAggregateDomainService.wipe(ids))
                .verifyComplete();

        verify(aggregateRepository).deleteAllById(ids);
    }
}
