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
import java.util.List;

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
        GenColumnBindCommand command = createCommand();

        GenColumnBindDomain mockDomain = new GenColumnBindDomain();
        when(genColumnBindDomainFactory.newInstance(any())).thenReturn(mockDomain);
        when(genColumnBindDomainRepository.save(any())).thenReturn(Mono.just(1L));

        Mono<Long> result = genColumnBindCommandService.create(command);

        StepVerifier.create(result)
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void update_shouldComplete_whenValidCommand() {
        GenColumnBindCommand command = createCommand();
        command.setId(1L);

        GenColumnBindDomain mockDomain = new GenColumnBindDomain();
        when(genColumnBindDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(genColumnBindDomainRepository.save(any())).thenReturn(Mono.empty());

        Mono<Void> result = genColumnBindCommandService.update(command);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void delete_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(deleteGenColumnBindDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = genColumnBindCommandService.delete(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipe_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeGenColumnBindByIdsDomainService.wipeByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = genColumnBindCommandService.wipe(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void restore_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(restoreGenColumnBindDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = genColumnBindCommandService.restore(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    private GenColumnBindCommand createCommand() {
        GenColumnBindCommand command = new GenColumnBindCommand();
        command.setId(0L);
        command.setColumnType("varchar");
        command.setEntityType("String");
        command.setComponentType((byte) 1);
        command.setTypescriptType((byte) 1);
        return command;
    }
}
