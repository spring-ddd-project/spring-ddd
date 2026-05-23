package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenTemplateCommand;
import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.factory.RepositoryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenTemplateCommandServiceTest {

    @Mock
    private RepositoryFactory repositoryFactory;

    @Mock
    private GenTemplateDomainFactory genTemplateDomainFactory;

    @Mock
    private DeleteGenTemplateDomainService deleteGenTemplateDomainService;

    @Mock
    private RestoreGenTemplateDomainService restoreGenTemplateDomainService;

    @Mock
    private WipeGenTemplateDomainService wipeGenTemplateDomainService;

    @Mock
    private GenTemplateDomainRepository genTemplateDomainRepository;

    @InjectMocks
    private GenTemplateCommandService service;

    @Test
    @DisplayName("create 应创建模板并返回 ID")
    void create_shouldCreateAndReturnId() {
        GenTemplateCommand command = new GenTemplateCommand();
        command.setTemplateName("domain");
        command.setTemplateContent("content");

        GenTemplateDomain domain = new GenTemplateDomain();
        when(genTemplateDomainFactory.newInstance(any(TemplateInfo.class))).thenReturn(domain);
        when(repositoryFactory.getGenTemplateDomainRepository()).thenReturn(genTemplateDomainRepository);
        when(genTemplateDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.create(command))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();

        verify(genTemplateDomainFactory).newInstance(any(TemplateInfo.class));
        verify(genTemplateDomainRepository).save(domain);
    }

    @Test
    @DisplayName("update 应更新模板")
    void update_shouldUpdate() {
        GenTemplateCommand command = new GenTemplateCommand();
        command.setId(1L);
        command.setTemplateName("updated");
        command.setTemplateContent("updated content");

        GenTemplateDomain domain = new GenTemplateDomain();
        when(repositoryFactory.getGenTemplateDomainRepository()).thenReturn(genTemplateDomainRepository);
        when(genTemplateDomainRepository.load(new TemplateId(1L))).thenReturn(Mono.just(domain));
        when(genTemplateDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.update(command))
                .verifyComplete();

        verify(genTemplateDomainRepository).load(new TemplateId(1L));
        verify(genTemplateDomainRepository).save(domain);
    }

    @Test
    @DisplayName("delete 应调用 delete 领域服务")
    void delete_shouldCallDomainService() {
        List<Long> ids = List.of(1L, 2L);
        when(deleteGenTemplateDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(service.delete(ids))
                .verifyComplete();

        verify(deleteGenTemplateDomainService).deleteByIds(ids);
    }

    @Test
    @DisplayName("restore 应调用 restore 领域服务")
    void restore_shouldCallDomainService() {
        List<Long> ids = List.of(1L, 2L);
        when(restoreGenTemplateDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(service.restore(ids))
                .verifyComplete();

        verify(restoreGenTemplateDomainService).restoreByIds(ids);
    }

    @Test
    @DisplayName("wipe 应调用 wipe 领域服务")
    void wipe_shouldCallDomainService() {
        List<Long> ids = List.of(1L, 2L);
        when(wipeGenTemplateDomainService.wipeByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(service.wipe(ids))
                .verifyComplete();

        verify(wipeGenTemplateDomainService).wipeByIds(ids);
    }
}
