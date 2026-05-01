package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenProjectInfoCommand;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenProjectInfoCommandServiceTest {

    @Mock
    private GenProjectInfoDomainRepository genProjectInfoDomainRepository;

    @Mock
    private GenProjectInfoDomainFactory genProjectInfoDomainFactory;

    @Mock
    private WipeGenProjectInfoByIdsDomainService wipeGenInfoByIdsDomainService;

    private GenProjectInfoCommandService genProjectInfoCommandService;

    @BeforeEach
    void setUp() {
        genProjectInfoCommandService = new GenProjectInfoCommandService(
                genProjectInfoDomainRepository,
                genProjectInfoDomainFactory,
                wipeGenInfoByIdsDomainService
        );
    }

    @Test
    void create_shouldReturnId_whenCommandIsValid() {
        GenProjectInfoCommand command = new GenProjectInfoCommand();
        command.setTableName("test_table");
        command.setPackageName("com.example");
        command.setClassName("TestClass");
        command.setModuleName("testModule");
        command.setProjectName("testProject");
        command.setRequestName("testRequest");

        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        when(genProjectInfoDomainFactory.newInstance(any(ProjectInfo.class), any(GenProjectInfoExtendInfo.class)))
                .thenReturn(domain);
        when(genProjectInfoDomainRepository.save(any(GenProjectInfoDomain.class)))
                .thenReturn(Mono.just(1L));

        Mono<Long> result = genProjectInfoCommandService.create(command);

        StepVerifier.create(result)
                .expectNext(1L)
                .verifyComplete();

        verify(genProjectInfoDomainFactory).newInstance(any(ProjectInfo.class), any(GenProjectInfoExtendInfo.class));
        verify(genProjectInfoDomainRepository).save(domain);
    }

    @Test
    void update_shouldComplete_whenDomainExists() {
        GenProjectInfoCommand command = new GenProjectInfoCommand();
        command.setId(1L);
        command.setTableName("updated_table");
        command.setPackageName("com.updated");
        command.setClassName("UpdatedClass");
        command.setModuleName("updatedModule");
        command.setProjectName("updatedProject");
        command.setRequestName("updatedRequest");

        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        when(genProjectInfoDomainRepository.load(any(InfoId.class)))
                .thenReturn(Mono.just(domain));
        when(genProjectInfoDomainRepository.save(any(GenProjectInfoDomain.class)))
                .thenReturn(Mono.empty());

        Mono<Void> result = genProjectInfoCommandService.update(command);

        StepVerifier.create(result)
                .verifyComplete();

        verify(genProjectInfoDomainRepository).load(new InfoId(1L));
        verify(genProjectInfoDomainRepository).save(domain);
    }

    @Test
    void update_shouldComplete_whenDomainNotFound() {
        GenProjectInfoCommand command = new GenProjectInfoCommand();
        command.setId(999L);
        command.setTableName("updated_table");
        command.setPackageName("com.updated");
        command.setClassName("UpdatedClass");
        command.setModuleName("updatedModule");
        command.setProjectName("updatedProject");
        command.setRequestName("updatedRequest");

        when(genProjectInfoDomainRepository.load(any(InfoId.class)))
                .thenReturn(Mono.empty());

        Mono<Void> result = genProjectInfoCommandService.update(command);

        StepVerifier.create(result)
                .verifyComplete();

        verify(genProjectInfoDomainRepository).load(new InfoId(999L));
        verify(genProjectInfoDomainRepository, never()).save(any());
    }

    @Test
    void delete_shouldComplete_whenDomainExists() {
        GenProjectInfoCommand command = new GenProjectInfoCommand();
        command.setId(1L);

        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        when(genProjectInfoDomainRepository.load(any(InfoId.class)))
                .thenReturn(Mono.just(domain));
        when(genProjectInfoDomainRepository.save(any(GenProjectInfoDomain.class)))
                .thenReturn(Mono.empty());

        Mono<Void> result = genProjectInfoCommandService.delete(command);

        StepVerifier.create(result)
                .verifyComplete();

        verify(genProjectInfoDomainRepository).load(new InfoId(1L));
        verify(genProjectInfoDomainRepository).save(domain);
    }

    @Test
    void delete_shouldComplete_whenDomainNotFound() {
        GenProjectInfoCommand command = new GenProjectInfoCommand();
        command.setId(999L);

        when(genProjectInfoDomainRepository.load(any(InfoId.class)))
                .thenReturn(Mono.empty());

        Mono<Void> result = genProjectInfoCommandService.delete(command);

        StepVerifier.create(result)
                .verifyComplete();

        verify(genProjectInfoDomainRepository).load(new InfoId(999L));
        verify(genProjectInfoDomainRepository, never()).save(any());
    }

    @Test
    void wipeByIds_shouldDelegateToWipeService() {
        java.util.List<Long> ids = Arrays.asList(1L, 2L, 3L);

        when(wipeGenInfoByIdsDomainService.wipeByIds(ids))
                .thenReturn(Mono.empty());

        Mono<Void> result = genProjectInfoCommandService.wipeByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();

        verify(wipeGenInfoByIdsDomainService).wipeByIds(ids);
    }

    @Test
    void wipeByIds_shouldComplete_withEmptyList() {
        java.util.List<Long> ids = Collections.emptyList();

        when(wipeGenInfoByIdsDomainService.wipeByIds(ids))
                .thenReturn(Mono.empty());

        Mono<Void> result = genProjectInfoCommandService.wipeByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();

        verify(wipeGenInfoByIdsDomainService).wipeByIds(ids);
    }
}
