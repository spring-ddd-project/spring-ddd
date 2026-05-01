package com.springddd.application.service.gen;

import com.springddd.infrastructure.persistence.r2dbc.GenAggregateRepository;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnsRepository;
import com.springddd.infrastructure.persistence.r2dbc.GenProjectInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WipeGenDataDomainServiceImplTest {

    @Mock
    private GenProjectInfoRepository infoProjectRepository;

    @Mock
    private GenColumnsRepository columnsRepository;

    @Mock
    private GenAggregateRepository aggregateRepository;

    private WipeGenDataDomainServiceImpl wipeGenDataDomainService;

    @BeforeEach
    void setUp() {
        wipeGenDataDomainService = new WipeGenDataDomainServiceImpl(
                infoProjectRepository,
                columnsRepository,
                aggregateRepository
        );
    }

    @Test
    void wipe_shouldDeleteAllDataFromAllRepositories() {
        when(infoProjectRepository.deleteAll()).thenReturn(Mono.empty());
        when(columnsRepository.deleteAll()).thenReturn(Mono.empty());
        when(aggregateRepository.deleteAll()).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenDataDomainService.wipe();

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipe_shouldCompleteEvenWhenRepositoriesAreEmpty() {
        when(infoProjectRepository.deleteAll()).thenReturn(Mono.empty());
        when(columnsRepository.deleteAll()).thenReturn(Mono.empty());
        when(aggregateRepository.deleteAll()).thenReturn(Mono.empty());

        StepVerifier.create(wipeGenDataDomainService.wipe())
                .verifyComplete();
    }

    @Test
    void wipe_shouldExecuteAllDeletions() {
        when(infoProjectRepository.deleteAll()).thenReturn(Mono.empty());
        when(columnsRepository.deleteAll()).thenReturn(Mono.empty());
        when(aggregateRepository.deleteAll()).thenReturn(Mono.empty());

        wipeGenDataDomainService.wipe();

        StepVerifier.create(wipeGenDataDomainService.wipe())
                .verifyComplete();
    }
}
