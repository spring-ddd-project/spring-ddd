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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        command.setTemplateName("testTemplate");
        command.setTemplateContent("template content");

        TemplateInfo info = new TemplateInfo("testTemplate", "template content");
        GenTemplateDomain mockDomain = new GenTemplateDomain();
        when(genTemplateDomainFactory.newInstance(any(TemplateInfo.class))).thenReturn(mockDomain);
        when(genTemplateDomainRepository.save(any(GenTemplateDomain.class))).thenReturn(Mono.just(1L));

        Mono<Long> result = genTemplateCommandService.create(command);

        StepVerifier.create(result)
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void update_shouldComplete_whenValidCommand() {
        GenTemplateCommand command = new GenTemplateCommand();
        command.setId(1L);
        command.setTemplateName("updatedTemplate");
        command.setTemplateContent("updated content");

        GenTemplateDomain mockDomain = new GenTemplateDomain();
        when(genTemplateDomainRepository.load(any(TemplateId.class))).thenReturn(Mono.just(mockDomain));
        when(genTemplateDomainRepository.save(any(GenTemplateDomain.class))).thenReturn(Mono.empty());

        Mono<Void> result = genTemplateCommandService.update(command);

        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void delete_shouldDelegateToDeleteService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(deleteGenTemplateDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = genTemplateCommandService.delete(ids);

        StepVerifier.create(result).verifyComplete();
        verify(deleteGenTemplateDomainService).deleteByIds(ids);
    }

    @Test
    void restore_shouldDelegateToRestoreService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(restoreGenTemplateDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = genTemplateCommandService.restore(ids);

        StepVerifier.create(result).verifyComplete();
        verify(restoreGenTemplateDomainService).restoreByIds(ids);
    }

    @Test
    void wipe_shouldDelegateToWipeService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeGenTemplateDomainService.wipeByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = genTemplateCommandService.wipe(ids);

        StepVerifier.create(result).verifyComplete();
        verify(wipeGenTemplateDomainService).wipeByIds(ids);
    }
}
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    void shouldCreateGenTemplate() {
        GenTemplateCommand command = new GenTemplateCommand();
        command.setTemplateName("test-template");
        command.setTemplateContent("template content");

        TemplateInfo templateInfo = new TemplateInfo("test-template", "template content");
        GenTemplateDomain domain = new GenTemplateDomain();

        when(genTemplateDomainFactory.newInstance(any(TemplateInfo.class))).thenReturn(domain);
        when(genTemplateDomainRepository.save(any(GenTemplateDomain.class))).thenReturn(Mono.just(1L));

        Mono<Long> result = genTemplateCommandService.create(command);

        StepVerifier.create(result)
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();

        verify(genTemplateDomainFactory).newInstance(any(TemplateInfo.class));
    }

    @Test
    void shouldUpdateGenTemplate() {
        GenTemplateCommand command = new GenTemplateCommand();
        command.setId(1L);
        command.setTemplateName("updated-template");
        command.setTemplateContent("updated content");

        TemplateInfo templateInfo = new TemplateInfo("updated-template", "updated content");
        GenTemplateDomain domain = new GenTemplateDomain();

        when(genTemplateDomainRepository.load(any(TemplateId.class))).thenReturn(Mono.just(domain));
        when(genTemplateDomainRepository.save(any(GenTemplateDomain.class))).thenReturn(Mono.just(1L));

        Mono<Void> result = genTemplateCommandService.update(command);

        StepVerifier.create(result)
                .verifyComplete();

        verify(genTemplateDomainRepository).load(any(TemplateId.class));
    }

    @Test
    void shouldDeleteGenTemplate() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(deleteGenTemplateDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = genTemplateCommandService.delete(ids);

        StepVerifier.create(result)
                .verifyComplete();

        verify(deleteGenTemplateDomainService).deleteByIds(ids);
    }

    @Test
    void shouldRestoreGenTemplate() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(restoreGenTemplateDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = genTemplateCommandService.restore(ids);

        StepVerifier.create(result)
                .verifyComplete();

        verify(restoreGenTemplateDomainService).restoreByIds(ids);
    }

    @Test
    void shouldWipeGenTemplate() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeGenTemplateDomainService.wipeByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = genTemplateCommandService.wipe(ids);

        StepVerifier.create(result)
                .verifyComplete();

        verify(wipeGenTemplateDomainService).wipeByIds(ids);
    }
}
