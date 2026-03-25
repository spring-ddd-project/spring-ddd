package com.springddd.application.service.gen;

import com.springddd.application.service.gen.WipeGenTemplateDomainServiceImpl;
import com.springddd.infrastructure.persistence.r2dbc.GenTemplateRepository;
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
class WipeGenTemplateDomainServiceImplTest {

    @Mock
    private GenTemplateRepository genTemplateRepository;

    @InjectMocks
    private WipeGenTemplateDomainServiceImpl wipeGenTemplateDomainService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void wipeByIds_shouldDeleteTemplates() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        when(genTemplateRepository.deleteAllById(ids)).thenReturn(Mono.empty().then());

        StepVerifier.create(wipeGenTemplateDomainService.wipeByIds(ids))
                .verifyComplete();

        verify(genTemplateRepository).deleteAllById(ids);
    }

    @Test
    void wipeByIds_shouldHandleEmptyList() {
        List<Long> ids = Collections.emptyList();

        when(genTemplateRepository.deleteAllById(ids)).thenReturn(Mono.empty().then());

        StepVerifier.create(wipeGenTemplateDomainService.wipeByIds(ids))
                .verifyComplete();

        verify(genTemplateRepository).deleteAllById(ids);
    }
}
