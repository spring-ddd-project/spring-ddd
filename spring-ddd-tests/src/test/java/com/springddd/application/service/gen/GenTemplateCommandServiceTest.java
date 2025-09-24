package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenTemplateCommand;
import com.springddd.domain.gen.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenTemplateCommandServiceTest {

    @Mock
    private GenTemplateDomainRepository genTemplateDomainRepository;

    @Mock
    private GenTemplateDomainFactory genTemplateDomainFactory;

    @Mock
    private DeleteGenTemplateDomainService deleteGenTemplateDomainService;

    @Mock
    private RestoreGenTemplateDomainService restoreGenTemplateDomainService;

    @Mock
    private WipeGenTemplateDomainService wipeGenTemplateDomainService;

    private GenTemplateCommandService genTemplateCommandService;

    @BeforeEach
    void setUp() {
        genTemplateCommandService = new GenTemplateCommandService(
                genTemplateDomainRepository,
                genTemplateDomainFactory,
                deleteGenTemplateDomainService,
                restoreGenTemplateDomainService,
                wipeGenTemplateDomainService
        );
    }

    @Test
    void create_shouldReturnId_whenValidCommand() {
        GenTemplateCommand command = new GenTemplateCommand();
        command.setTemplateName("Test Template");
        command.setTemplateContent("Template content");

        GenTemplateDomain mockDomain = new GenTemplateDomain();
        when(genTemplateDomainFactory.newInstance(any())).thenReturn(mockDomain);
        when(genTemplateDomainRepository.save(any())).thenReturn(Mono.just(1L));

        Mono<Long> result = genTemplateCommandService.create(command);

        StepVerifier.create(result)
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void update_shouldComplete_whenValidCommand() {
        GenTemplateCommand command = new GenTemplateCommand();
        command.setId(1L);
        command.setTemplateName("Updated Template");
        command.setTemplateContent("Updated content");

        GenTemplateDomain mockDomain = new GenTemplateDomain();
        when(genTemplateDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(genTemplateDomainRepository.save(any())).thenReturn(Mono.just(1L));

        Mono<Void> result = genTemplateCommandService.update(command);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void delete_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(deleteGenTemplateDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = genTemplateCommandService.delete(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void restore_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(restoreGenTemplateDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = genTemplateCommandService.restore(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipe_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeGenTemplateDomainService.wipeByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = genTemplateCommandService.wipe(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
