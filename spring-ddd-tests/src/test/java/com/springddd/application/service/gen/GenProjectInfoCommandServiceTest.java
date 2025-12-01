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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    void create_shouldReturnId_whenValidCommand() {
        GenProjectInfoCommand command = new GenProjectInfoCommand();
        command.setTableName("test_table");
        command.setPackageName("com.test");
        command.setClassName("TestClass");
        command.setModuleName("test-module");
        command.setProjectName("test-project");
        command.setRequestName("TestRequest");

        GenProjectInfoDomain mockDomain = new GenProjectInfoDomain();
        when(genProjectInfoDomainFactory.newInstance(any(), any())).thenReturn(mockDomain);
        when(genProjectInfoDomainRepository.save(any())).thenReturn(Mono.just(1L));

        Mono<Long> result = genProjectInfoCommandService.create(command);

        StepVerifier.create(result)
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void wipeByIds_shouldDelegateToDomainService() {
        when(wipeGenInfoByIdsDomainService.wipeByIds(Arrays.asList(1L, 2L)))
                .thenReturn(Mono.empty());

        Mono<Void> result = genProjectInfoCommandService.wipeByIds(Arrays.asList(1L, 2L));

        StepVerifier.create(result)
                .verifyComplete();
    }
}
