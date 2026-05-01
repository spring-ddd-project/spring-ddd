package com.springddd.application.service.gen;

import com.springddd.infrastructure.persistence.r2dbc.GenProjectInfoRepository;
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
class WipeGenProjectInfoByIdsDomainServiceImplTest {

    @Mock
    private GenProjectInfoRepository genProjectInfoRepository;

    private WipeGenProjectInfoByIdsDomainServiceImpl wipeGenProjectInfoByIdsDomainService;

    @BeforeEach
    void setUp() {
        wipeGenProjectInfoByIdsDomainService = new WipeGenProjectInfoByIdsDomainServiceImpl(genProjectInfoRepository);
    }

    @Test
    void wipeByIds_shouldDeleteProjectInfosByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        when(genProjectInfoRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenProjectInfoByIdsDomainService.wipeByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipeByIds_shouldComplete_whenIdsListIsEmpty() {
        List<Long> ids = Collections.emptyList();

        when(genProjectInfoRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenProjectInfoByIdsDomainService.wipeByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipeByIds_shouldDelete_whenSingleIdProvided() {
        List<Long> ids = Collections.singletonList(1L);

        when(genProjectInfoRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenProjectInfoByIdsDomainService.wipeByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipeByIds_shouldHandleLargeIdList() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L, 5L);

        when(genProjectInfoRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenProjectInfoByIdsDomainService.wipeByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
