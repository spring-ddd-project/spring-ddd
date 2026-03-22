package com.springddd.application.service.gen;

import com.springddd.application.service.gen.WipeGenDataDomainServiceImpl;
import com.springddd.infrastructure.persistence.r2dbc.GenAggregateRepository;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnsRepository;
import com.springddd.infrastructure.persistence.r2dbc.GenProjectInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeGenDataDomainServiceImplTest {

    @Mock
    private GenProjectInfoRepository infoProjectRepository;

    @Mock
    private GenColumnsRepository columnsRepository;

    @Mock
    private GenAggregateRepository aggregateRepository;

    @InjectMocks
    private WipeGenDataDomainServiceImpl wipeGenDataDomainService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void wipe_shouldDeleteAllData() {
        when(infoProjectRepository.deleteAll()).thenReturn(Mono.empty());
        when(columnsRepository.deleteAll()).thenReturn(Mono.empty());
        when(aggregateRepository.deleteAll()).thenReturn(Mono.empty());

        StepVerifier.create(wipeGenDataDomainService.wipe())
                .verifyComplete();

        verify(infoProjectRepository).deleteAll();
        verify(columnsRepository).deleteAll();
        verify(aggregateRepository).deleteAll();
    }
}
