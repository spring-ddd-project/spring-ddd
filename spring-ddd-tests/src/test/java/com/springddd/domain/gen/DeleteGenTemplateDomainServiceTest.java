package com.springddd.domain.gen;

import com.springddd.application.service.gen.DeleteGenTemplateDomainServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteGenTemplateDomainServiceTest {

    @Mock
    private GenTemplateDomainRepository domainRepository;

    @InjectMocks
    private DeleteGenTemplateDomainServiceImpl deleteGenTemplateDomainService;

    @Test
    void deleteByIds_shouldDeleteByIds() {
        GenTemplateDomain domain = new GenTemplateDomain();
        domain.setDeleteStatus(false);

        when(domainRepository.load(any(TemplateId.class))).thenReturn(Mono.just(domain));
        when(domainRepository.save(any(GenTemplateDomain.class))).thenReturn(Mono.just(1L));

        List<Long> ids = Arrays.asList(1L);

        StepVerifier.create(deleteGenTemplateDomainService.deleteByIds(ids))
                .verifyComplete();
    }
}
