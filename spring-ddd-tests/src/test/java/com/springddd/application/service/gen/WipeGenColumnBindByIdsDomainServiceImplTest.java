package com.springddd.application.service.gen;

import com.springddd.infrastructure.persistence.r2dbc.GenColumnBindRepository;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WipeGenColumnBindByIdsDomainServiceImplTest {

    @Mock
    private GenColumnBindRepository genColumnBindRepository;

    private WipeGenColumnBindByIdsDomainServiceImpl wipeGenColumnBindByIdsDomainService;

    @BeforeEach
    void setUp() {
        wipeGenColumnBindByIdsDomainService = new WipeGenColumnBindByIdsDomainServiceImpl(genColumnBindRepository);
    }

    @Test
    void wipeByIds_shouldDeleteColumnBindsByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        when(genColumnBindRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenColumnBindByIdsDomainService.wipeByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipeByIds_shouldComplete_whenIdsListIsEmpty() {
        List<Long> ids = Collections.emptyList();

        when(genColumnBindRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenColumnBindByIdsDomainService.wipeByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipeByIds_shouldDelete_whenSingleIdProvided() {
        List<Long> ids = Collections.singletonList(1L);

        when(genColumnBindRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenColumnBindByIdsDomainService.wipeByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipeByIds_shouldHandleMultipleIds() {
        List<Long> ids = Arrays.asList(10L, 20L, 30L);

        when(genColumnBindRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenColumnBindByIdsDomainService.wipeByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
