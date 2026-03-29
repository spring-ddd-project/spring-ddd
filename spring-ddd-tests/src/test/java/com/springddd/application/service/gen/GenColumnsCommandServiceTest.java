package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenColumnsCommand;
import com.springddd.domain.gen.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenColumnsCommandServiceTest {

    @Mock
    private GenColumnsDomainRepository genColumnsDomainRepository;

    @Mock
    private GenColumnsDomainFactory genColumnsDomainFactory;

    @Mock
    private WipeGenColumnsByIdsDomainService wipeGenColumnsByIdsDomainService;

    @Mock
    private GenColumnsBatchSaveDomainService genColumnsBatchSaveDomainService;

    private GenColumnsCommandService genColumnsCommandService;

    @BeforeEach
    void setUp() {
        genColumnsCommandService = new GenColumnsCommandService(
                genColumnsDomainRepository,
                genColumnsDomainFactory,
                wipeGenColumnsByIdsDomainService,
                genColumnsBatchSaveDomainService
        );
    }

    @Test
    void create_shouldReturnId_whenValidCommand() {
        GenColumnsCommand command = createValidCommand();

        GenColumnsDomain mockDomain = new GenColumnsDomain();
        when(genColumnsDomainFactory.newInstance(any(), any(), any(), any(), any(), any())).thenReturn(mockDomain);
        when(genColumnsDomainRepository.save(any())).thenReturn(Mono.just(1L));

        Mono<Long> result = genColumnsCommandService.create(command);

        StepVerifier.create(result)
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void update_shouldComplete_whenValidCommand() {
        GenColumnsCommand command = createValidCommand();
        command.setId(1L);

        GenColumnsDomain mockDomain = new GenColumnsDomain();
        when(genColumnsDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(genColumnsDomainRepository.save(any())).thenReturn(Mono.just(1L));

        Mono<Void> result = genColumnsCommandService.update(command);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void delete_shouldMarkAsDeleted() {
        GenColumnsCommand command = createValidCommand();
        command.setId(1L);

        GenColumnsDomain mockDomain = new GenColumnsDomain();
        when(genColumnsDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(genColumnsDomainRepository.save(any())).thenReturn(Mono.just(1L));

        Mono<Void> result = genColumnsCommandService.delete(command);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipe_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeGenColumnsByIdsDomainService.wipeByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = genColumnsCommandService.wipe(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void batchSave_shouldComplete_whenValidCommands() {
        GenColumnsCommand command1 = createValidCommand();
        GenColumnsCommand command2 = createValidCommand();
        List<GenColumnsCommand> commands = Arrays.asList(command1, command2);

        GenColumnsDomain mockDomain = new GenColumnsDomain();
        when(genColumnsDomainFactory.newInstance(any(), any(), any(), any(), any(), any())).thenReturn(mockDomain);
        when(genColumnsBatchSaveDomainService.batchSave(any())).thenReturn(Mono.empty());

        Mono<Void> result = genColumnsCommandService.batchSave(commands);

        StepVerifier.create(result)
                .verifyComplete();
    }

    private GenColumnsCommand createValidCommand() {
        GenColumnsCommand command = new GenColumnsCommand();
        command.setInfoId(1L);
        command.setPropColumnKey("testKey");
        command.setPropColumnName("testColumn");
        command.setPropColumnType("string");
        command.setPropColumnComment("test comment");
        command.setPropJavaType("String");
        command.setPropJavaEntity("String");
        command.setTableVisible(true);
        command.setTableOrder(1);
        command.setTableFilter(false);
        command.setTableFilterComponent("input");
        command.setTableFilterType("string");
        command.setFormComponent("input");
        command.setFormVisible(true);
        command.setFormRequired(false);
        command.setEn("test");
        command.setLocale("en");
        command.setPropDictId(1L);
        command.setTypescriptType("string");
        return command;
    }
}
