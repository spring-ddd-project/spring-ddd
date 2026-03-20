package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenTemplateCommand;
import com.springddd.domain.gen.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

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

    @InjectMocks
    private GenTemplateCommandService genTemplateCommandService;

    private GenTemplateCommand createCommand;
    private GenTemplateDomain mockDomain;

    @BeforeEach
    void setUp() {
        createCommand = new GenTemplateCommand();
        createCommand.setTemplateName("Test Template");
        createCommand.setTemplateContent("Template Content");

        mockDomain = new GenTemplateDomain();
        mockDomain.setTemplateInfo(new TemplateInfo("Test Template", "Template Content"));
        mockDomain.setDeleteStatus(false);
    }

    @Test
    void create_shouldCreateTemplate() {
        when(genTemplateDomainFactory.newInstance(any(TemplateInfo.class)))
                .thenReturn(mockDomain);
        when(genTemplateDomainRepository.save(any(GenTemplateDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(genTemplateCommandService.create(createCommand))
                .expectNext(1L)
                .verifyComplete();

        verify(genTemplateDomainFactory).newInstance(any(TemplateInfo.class));
        verify(genTemplateDomainRepository).save(any(GenTemplateDomain.class));
    }

    @Test
    void delete_shouldCallDeleteDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(deleteGenTemplateDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(genTemplateCommandService.delete(ids))
                .verifyComplete();

        verify(deleteGenTemplateDomainService).deleteByIds(ids);
    }

    @Test
    void restore_shouldCallRestoreDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(restoreGenTemplateDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(genTemplateCommandService.restore(ids))
                .verifyComplete();

        verify(restoreGenTemplateDomainService).restoreByIds(ids);
    }

    @Test
    void wipe_shouldCallWipeDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeGenTemplateDomainService.wipeByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(genTemplateCommandService.wipe(ids))
                .verifyComplete();

        verify(wipeGenTemplateDomainService).wipeByIds(ids);
    }
}
