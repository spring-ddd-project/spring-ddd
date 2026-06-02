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
        GenColumnBindCommand command = new GenColumnBindCommand();
        command.setColumnType("varchar");
        command.setEntityType("String");
        command.setComponentType((byte) 1);
        command.setTypescriptType((byte) 1);

        GenColumnBindDomain mockDomain = new GenColumnBindDomain();
        when(genColumnBindDomainFactory.newInstance(any())).thenReturn(mockDomain);
        when(genColumnBindDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(genColumnBindCommandService.create(command))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void update_shouldComplete_whenValidCommand() {
        GenColumnBindCommand command = new GenColumnBindCommand();
        command.setId(1L);
        command.setColumnType("int");
        command.setEntityType("Integer");
        command.setComponentType((byte) 2);
        command.setTypescriptType((byte) 2);

        GenColumnBindDomain mockDomain = new GenColumnBindDomain();
        when(genColumnBindDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(genColumnBindDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(genColumnBindCommandService.update(command))
                .verifyComplete();
    }

    @Test
    void delete_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(deleteGenColumnBindDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(genColumnBindCommandService.delete(ids))
                .verifyComplete();
    }

    @Test
    void wipe_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeGenColumnBindByIdsDomainService.wipeByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(genColumnBindCommandService.wipe(ids))
                .verifyComplete();
    }

    @Test
    void restore_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(restoreGenColumnBindDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(genColumnBindCommandService.restore(ids))
                .verifyComplete();
    }
}
