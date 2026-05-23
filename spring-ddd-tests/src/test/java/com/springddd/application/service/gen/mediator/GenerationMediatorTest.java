package com.springddd.application.service.gen.mediator;

import com.springddd.domain.gen.GenerateDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerationMediatorTest {

    @Mock
    private GenerateDomainService generator;

    @InjectMocks
    private GenerationMediator generationMediator;

    @Test
    @DisplayName("performFullGeneration 应委托给 generator")
    void performFullGeneration_shouldDelegateToGenerator() {
        when(generator.generate("sys_user")).thenReturn(Mono.empty());

        StepVerifier.create(generationMediator.performFullGeneration("sys_user"))
                .verifyComplete();

        verify(generator).generate("sys_user");
    }
}
