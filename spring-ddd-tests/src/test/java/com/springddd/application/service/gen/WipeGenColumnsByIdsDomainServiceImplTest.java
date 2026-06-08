package com.springddd.application.service.gen;

import com.springddd.domain.gen.WipeGenColumnsByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeGenColumnsByIdsDomainServiceImplTest {

    @Mock
    private GenColumnsRepository genColumnsRepository;

    private WipeGenColumnsByIdsDomainServiceImpl wipeGenColumnsByIdsDomainService;

    @BeforeEach
    void setUp() {
        wipeGenColumnsByIdsDomainService = new WipeGenColumnsByIdsDomainServiceImpl(genColumnsRepository);
    }

    @Test
    void wipeByIds_shouldDeleteAllByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(genColumnsRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenColumnsByIdsDomainService.wipeByIds(ids);

        StepVerifier.create(result).verifyComplete();
        verify(genColumnsRepository).deleteAllById(ids);
    }

    @Test
    void wipeByIds_shouldHandleEmptyList() {
        List<Long> ids = Arrays.asList();
        when(genColumnsRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenColumnsByIdsDomainService.wipeByIds(ids);

        StepVerifier.create(result).verifyComplete();
        verify(genColumnsRepository).deleteAllById(ids);
    }

    @Test
    void wipeByIds_shouldHandleSingleId() {
        List<Long> ids = Arrays.asList(99L);
        when(genColumnsRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenColumnsByIdsDomainService.wipeByIds(ids);

        StepVerifier.create(result).verifyComplete();
        verify(genColumnsRepository).deleteAllById(ids);
    }

    @Test
    void wipeByIds_shouldDelegateToRepository() {
        List<Long> ids = Arrays.asList(5L, 10L, 15L, 20L);
        when(genColumnsRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        wipeGenColumnsByIdsDomainService.wipeByIds(ids);

        verify(genColumnsRepository, times(1)).deleteAllById(ids);
    }
}
