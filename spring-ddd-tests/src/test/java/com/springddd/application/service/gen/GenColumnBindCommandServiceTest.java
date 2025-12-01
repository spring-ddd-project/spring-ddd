package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenColumnBindCommand;
import com.springddd.domain.gen.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenColumnBindCommandServiceTest {

    @Mock
    private GenColumnBindDomainRepository genColumnBindDomainRepository;

    @Mock
    private GenColumnBindDomainFactory genColumnBindDomainFactory;

    @Mock
    private WipeGenColumnBindByIdsDomainService wipeGenColumnBindByIdsDomainService;

    @Mock
    private DeleteGenColumnBindDomainService deleteGenColumnBindDomainService;

    @Mock
    private RestoreGenColumnBindDomainService restoreGenColumnBindDomainService;

    private GenColumnBindCommandService genColumnBindCommandService;

    @BeforeEach
    void setUp() {
        genColumnBindCommandService = new GenColumnBindCommandService(
                genColumnBindDomainRepository,
                genColumnBindDomainFactory,
                wipeGenColumnBindByIdsDomainService,
                deleteGenColumnBindDomainService,
                restoreGenColumnBindDomainService
        );
    }

    @Test
    void create_shouldReturnId_whenValidCommand() {
        GenColumnBindCommand command = new GenColumnBindCommand();
        command.setColumnType("varchar");
        command.setEntityType("String");
        command.setComponentType("Input");
        command.setTypescriptType("string");

        GenColumnBindDomain mockDomain = new GenColumnBindDomain();
        when(genColumnBindDomainFactory.newInstance(any())).thenReturn(mockDomain);
        when(genColumnBindDomainRepository.save(any())).thenReturn(Mono.just(1L));

        Mono<Long> result = genColumnBindCommandService.create(command);

        StepVerifier.create(result)
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void delete_shouldDelegateToDomainService() {
        when(deleteGenColumnBindDomainService.deleteByIds(Arrays.asList(1L, 2L)))
                .thenReturn(Mono.empty());

        Mono<Void> result = genColumnBindCommandService.delete(Arrays.asList(1L, 2L));

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipe_shouldDelegateToDomainService() {
        when(wipeGenColumnBindByIdsDomainService.wipeByIds(Arrays.asList(1L, 2L)))
                .thenReturn(Mono.empty());

        Mono<Void> result = genColumnBindCommandService.wipe(Arrays.asList(1L, 2L));

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void restore_shouldDelegateToDomainService() {
        when(restoreGenColumnBindDomainService.restoreByIds(Arrays.asList(1L, 2L)))
                .thenReturn(Mono.empty());

        Mono<Void> result = genColumnBindCommandService.restore(Arrays.asList(1L, 2L));

        StepVerifier.create(result)
                .verifyComplete();
    }
}
