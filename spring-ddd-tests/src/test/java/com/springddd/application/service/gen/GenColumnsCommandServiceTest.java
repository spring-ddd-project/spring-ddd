package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenColumnsCommand;
import com.springddd.domain.gen.*;
import com.springddd.domain.gen.exception.I18nLocaleNullException;
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

    private GenColumnsCommand buildCommand() {
        GenColumnsCommand command = new GenColumnsCommand();
        command.setInfoId(1L);
        command.setPropColumnKey("id");
        command.setPropColumnName("ID");
        command.setPropColumnType("bigint");
        command.setPropColumnComment("Primary Key");
        command.setPropJavaEntity("Long");
        command.setPropJavaType("Long");
        command.setPropDictId(1L);
        command.setTableVisible(true);
        command.setTableOrder(true);
        command.setTableFilter(true);
        command.setTableFilterComponent((byte) 1);
        command.setTableFilterType((byte) 1);
        command.setTypescriptType((byte) 1);
        command.setFormComponent((byte) 1);
        command.setFormVisible(true);
        command.setFormRequired(true);
        command.setEn("Id");
        return command;
    }

    private GenColumnsDomain buildMockDomain(String en, String locale) {
        GenColumnsDomain mockDomain = new GenColumnsDomain();
        mockDomain.setI18n(new I18n(en, locale));
        return mockDomain;
    }

    @Test
    void create_shouldReturnId_whenValidCommand() {
        GenColumnsCommand command = buildCommand();
        command.setLocale("Primary Key");

        GenColumnsDomain mockDomain = buildMockDomain("Id", "Primary Key");
        when(genColumnsDomainFactory.newInstance(any(), any(), any(), any(), any(), any())).thenReturn(mockDomain);
        when(genColumnsDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(genColumnsCommandService.create(command))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void update_shouldComplete_whenValidCommand() {
        GenColumnsCommand command = buildCommand();
        command.setId(1L);
        command.setLocale("Updated");

        GenColumnsDomain mockDomain = buildMockDomain("Id", "Updated");
        when(genColumnsDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(genColumnsDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(genColumnsCommandService.update(command))
                .verifyComplete();
    }

    @Test
    void delete_shouldComplete_whenValidCommand() {
        GenColumnsCommand command = new GenColumnsCommand();
        command.setId(1L);

        GenColumnsDomain mockDomain = new GenColumnsDomain();
        when(genColumnsDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(genColumnsDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(genColumnsCommandService.delete(command))
                .verifyComplete();
    }

    @Test
    void wipe_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeGenColumnsByIdsDomainService.wipeByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(genColumnsCommandService.wipe(ids))
                .verifyComplete();
    }

    @Test
    void batchSave_shouldComplete_whenAllLocalesEmpty() {
        GenColumnsCommand command = buildCommand();
        command.setLocale(null);

        GenColumnsDomain mockDomain = buildMockDomain("Id", null);
        when(genColumnsDomainFactory.newInstance(any(), any(), any(), any(), any(), any())).thenReturn(mockDomain);
        when(genColumnsBatchSaveDomainService.batchSave(any())).thenReturn(Mono.empty());

        StepVerifier.create(genColumnsCommandService.batchSave(Collections.singletonList(command)))
                .verifyComplete();
    }

    @Test
    void batchSave_shouldComplete_whenAllLocalesNonEmpty() {
        GenColumnsCommand command = buildCommand();
        command.setLocale("Primary Key");

        GenColumnsDomain mockDomain = buildMockDomain("Id", "Primary Key");
        when(genColumnsDomainFactory.newInstance(any(), any(), any(), any(), any(), any())).thenReturn(mockDomain);
        when(genColumnsBatchSaveDomainService.batchSave(any())).thenReturn(Mono.empty());

        StepVerifier.create(genColumnsCommandService.batchSave(Collections.singletonList(command)))
                .verifyComplete();
    }

    @Test
    void batchSave_shouldError_whenLocalesMixed() {
        GenColumnsCommand cmd1 = buildCommand();
        cmd1.setLocale("Primary Key");

        GenColumnsCommand cmd2 = buildCommand();
        cmd2.setPropColumnKey("name");
        cmd2.setPropColumnName("Name");
        cmd2.setPropColumnType("varchar");
        cmd2.setPropColumnComment("Name");
        cmd2.setPropJavaEntity("String");
        cmd2.setPropJavaType("String");
        cmd2.setEn("Name");
        cmd2.setLocale(null);

        when(genColumnsDomainFactory.newInstance(any(), any(), any(), any(), any(), any()))
                .thenAnswer(invocation -> {
                    I18n i18n = invocation.getArgument(4);
                    return buildMockDomain(i18n.en(), i18n.locale());
                });

        StepVerifier.create(genColumnsCommandService.batchSave(Arrays.asList(cmd1, cmd2)))
                .expectError(I18nLocaleNullException.class)
                .verify();
    }

    @Test
    void batchUpdate_shouldComplete_whenAllLocalesEmpty() {
        GenColumnsCommand command = buildCommand();
        command.setId(1L);
        command.setLocale(null);

        GenColumnsDomain mockDomain = buildMockDomain("Id", null);
        when(genColumnsDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(genColumnsBatchSaveDomainService.batchSave(any())).thenReturn(Mono.empty());

        StepVerifier.create(genColumnsCommandService.batchUpdate(Collections.singletonList(command)))
                .verifyComplete();
    }

    @Test
    void batchUpdate_shouldError_whenLocalesMixed() {
        GenColumnsCommand cmd1 = buildCommand();
        cmd1.setId(1L);
        cmd1.setLocale("Primary Key");

        GenColumnsCommand cmd2 = buildCommand();
        cmd2.setId(2L);
        cmd2.setPropColumnKey("name");
        cmd2.setPropColumnName("Name");
        cmd2.setPropColumnType("varchar");
        cmd2.setPropColumnComment("Name");
        cmd2.setPropJavaEntity("String");
        cmd2.setPropJavaType("String");
        cmd2.setEn("Name");
        cmd2.setLocale(null);

        when(genColumnsDomainRepository.load(any()))
                .thenAnswer(invocation -> Mono.just(buildMockDomain("en", null)));

        StepVerifier.create(genColumnsCommandService.batchUpdate(Arrays.asList(cmd1, cmd2)))
                .expectError(I18nLocaleNullException.class)
                .verify();
    }

    @Test
    void batchUpdate_shouldComplete_whenAllLocalesNonEmpty() {
        GenColumnsCommand command = buildCommand();
        command.setId(1L);
        command.setLocale("Primary Key");

        GenColumnsDomain mockDomain = buildMockDomain("Id", "Primary Key");
        when(genColumnsDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(genColumnsBatchSaveDomainService.batchSave(any())).thenReturn(Mono.empty());

        StepVerifier.create(genColumnsCommandService.batchUpdate(Collections.singletonList(command)))
                .verifyComplete();
    }

    @Test
    void batchSave_shouldComplete_whenAllLocalesBlank() {
        GenColumnsCommand command = buildCommand();
        command.setLocale("");

        GenColumnsDomain mockDomain = buildMockDomain("Id", "");
        when(genColumnsDomainFactory.newInstance(any(), any(), any(), any(), any(), any())).thenReturn(mockDomain);
        when(genColumnsBatchSaveDomainService.batchSave(any())).thenReturn(Mono.empty());

        StepVerifier.create(genColumnsCommandService.batchSave(Collections.singletonList(command)))
                .verifyComplete();
    }

    @Test
    void batchUpdate_shouldComplete_whenAllLocalesBlank() {
        GenColumnsCommand command = buildCommand();
        command.setId(1L);
        command.setLocale("");

        GenColumnsDomain mockDomain = buildMockDomain("Id", "");
        when(genColumnsDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(genColumnsBatchSaveDomainService.batchSave(any())).thenReturn(Mono.empty());

        StepVerifier.create(genColumnsCommandService.batchUpdate(Collections.singletonList(command)))
                .verifyComplete();
    }
}
