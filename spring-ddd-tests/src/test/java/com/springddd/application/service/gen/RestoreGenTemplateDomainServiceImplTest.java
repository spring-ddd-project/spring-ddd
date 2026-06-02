package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenTemplateDomain;
import com.springddd.domain.gen.GenTemplateDomainRepository;
import com.springddd.domain.gen.TemplateId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestoreGenTemplateDomainServiceImplTest {

    @Mock
    private GenTemplateDomainRepository genTemplateDomainRepository;

    private RestoreGenTemplateDomainServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new RestoreGenTemplateDomainServiceImpl(genTemplateDomainRepository);
    }

    @Test
    void restoreByIds_shouldComplete_whenValidIds() {
        GenTemplateDomain domain = new GenTemplateDomain();
        domain.setDeleteStatus(true);
        when(genTemplateDomainRepository.load(any(TemplateId.class))).thenReturn(Mono.just(domain));
        when(genTemplateDomainRepository.save(any(GenTemplateDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.restoreByIds(List.of(1L)))
                .verifyComplete();
    }
}
