package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenTemplateDomain;
import com.springddd.domain.gen.GenTemplateDomainRepository;
import com.springddd.domain.gen.TemplateId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestoreGenTemplateDomainServiceImplTest {

    @Mock
    private GenTemplateDomainRepository genTemplateDomainRepository;

    @InjectMocks
    private RestoreGenTemplateDomainServiceImpl service;

    @Test
    @DisplayName("restoreByIds 应恢复指定模板")
    void restoreByIds_shouldRestore() {
        GenTemplateDomain domain = mock(GenTemplateDomain.class);
        when(genTemplateDomainRepository.load(new TemplateId(1L))).thenReturn(Mono.just(domain));
        when(genTemplateDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.restoreByIds(List.of(1L)))
                .verifyComplete();

        verify(domain).restore();
        verify(genTemplateDomainRepository).save(domain);
    }
}
