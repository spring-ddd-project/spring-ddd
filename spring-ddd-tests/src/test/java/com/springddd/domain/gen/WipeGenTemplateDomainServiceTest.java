package com.springddd.domain.gen;

import com.springddd.application.service.gen.WipeGenTemplateDomainServiceImpl;
import com.springddd.infrastructure.persistence.r2dbc.GenTemplateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WipeGenTemplateDomainServiceTest {

    @Mock
    private GenTemplateRepository genTemplateRepository;

    @InjectMocks
    private WipeGenTemplateDomainServiceImpl wipeGenTemplateDomainService;

    @Test
    void wipeByIds_shouldDeleteByIds() {
        when(genTemplateRepository.deleteAllById(anyList())).thenReturn(Mono.empty());

        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        StepVerifier.create(wipeGenTemplateDomainService.wipeByIds(ids))
                .verifyComplete();
    }
}
