package com.springddd.application.service.gen;

import com.springddd.domain.gen.WipeGenTemplateDomainService;
import com.springddd.infrastructure.persistence.r2dbc.GenTemplateRepository;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeGenTemplateByIdsDomainServiceImplTest {

    @Mock
    private GenTemplateRepository genTemplateRepository;

    private WipeGenTemplateDomainServiceImpl wipeGenTemplateDomainService;

    @BeforeEach
    void setUp() {
        wipeGenTemplateDomainService = new WipeGenTemplateDomainServiceImpl(genTemplateRepository);
    }

    @Test
    void wipeByIds_shouldDeleteAllByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(genTemplateRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenTemplateDomainService.wipeByIds(ids);

        StepVerifier.create(result).verifyComplete();
        verify(genTemplateRepository).deleteAllById(ids);
    }

    @Test
    void wipeByIds_shouldHandleEmptyList() {
        List<Long> ids = Arrays.asList();
        when(genTemplateRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenTemplateDomainService.wipeByIds(ids);

        StepVerifier.create(result).verifyComplete();
        verify(genTemplateRepository).deleteAllById(ids);
    }

    @Test
    void wipeByIds_shouldHandleSingleId() {
        List<Long> ids = Arrays.asList(1L);
        when(genTemplateRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenTemplateDomainService.wipeByIds(ids);

        StepVerifier.create(result).verifyComplete();
        verify(genTemplateRepository).deleteAllById(ids);
    }

    @Test
    void wipeByIds_shouldDelegateToRepository() {
        List<Long> ids = Arrays.asList(10L, 20L, 30L);
        when(genTemplateRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        wipeGenTemplateDomainService.wipeByIds(ids);

        verify(genTemplateRepository, times(1)).deleteAllById(ids);
    }
}
