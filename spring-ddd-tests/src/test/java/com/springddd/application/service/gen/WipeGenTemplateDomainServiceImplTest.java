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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WipeGenTemplateDomainServiceImplTest {

    @Mock
    private GenTemplateRepository genTemplateRepository;

    private WipeGenTemplateDomainServiceImpl wipeGenTemplateDomainService;

    @BeforeEach
    void setUp() {
        wipeGenTemplateDomainService = new WipeGenTemplateDomainServiceImpl(genTemplateRepository);
    }

    @Test
    void wipeByIds_shouldDeleteAllById() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        when(genTemplateRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenTemplateDomainService.wipeByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipeByIds_shouldComplete_whenEmptyIds() {
        List<Long> ids = Arrays.asList();

        when(genTemplateRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenTemplateDomainService.wipeByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipeByIds_shouldHandleSingleId() {
        List<Long> ids = Arrays.asList(1L);

        when(genTemplateRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenTemplateDomainService.wipeByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipeByIds_shouldHandleLargeIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);

        when(genTemplateRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        Mono<Void> result = wipeGenTemplateDomainService.wipeByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
