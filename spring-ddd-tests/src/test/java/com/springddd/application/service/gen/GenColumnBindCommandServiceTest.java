package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenColumnBindCommand;
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
class GenColumnBindCommandServiceTest {

    @Mock
    private RepositoryFactory repositoryFactory;

    @Mock
    private GenColumnBindDomainFactory genColumnBindDomainFactory;

    @Mock
    private WipeGenColumnBindByIdsDomainService wipeGenColumnBindByIdsDomainService;

    @Mock
    private DeleteGenColumnBindDomainService deleteGenColumnBindDomainService;

    @Mock
    private RestoreGenColumnBindDomainService restoreGenColumnBindDomainService;

    @Mock
    private GenColumnBindDomainRepository genColumnBindDomainRepository;

    @InjectMocks
    private GenColumnBindCommandService service;

    @Test
    @DisplayName("create 应创建列绑定并返回 ID")
    void create_shouldCreateAndReturnId() {
        GenColumnBindCommand command = new GenColumnBindCommand();
        command.setColumnType("varchar");
        command.setEntityType("String");
        command.setComponentType((byte) 1);
        command.setTypescriptType((byte) 1);

        GenColumnBindDomain domain = new GenColumnBindDomain();
        when(genColumnBindDomainFactory.newInstance(any(GenColumnBindBasicInfo.class))).thenReturn(domain);
        when(repositoryFactory.getGenColumnBindDomainRepository()).thenReturn(genColumnBindDomainRepository);
        when(genColumnBindDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.create(command))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();

        verify(genColumnBindDomainFactory).newInstance(any(GenColumnBindBasicInfo.class));
        verify(genColumnBindDomainRepository).save(domain);
    }

    @Test
    @DisplayName("update 应更新列绑定")
    void update_shouldUpdate() {
        GenColumnBindCommand command = new GenColumnBindCommand();
        command.setId(1L);
        command.setColumnType("int");
        command.setEntityType("Integer");
        command.setComponentType((byte) 1);
        command.setTypescriptType((byte) 1);

        GenColumnBindDomain domain = new GenColumnBindDomain();
        when(repositoryFactory.getGenColumnBindDomainRepository()).thenReturn(genColumnBindDomainRepository);
        when(genColumnBindDomainRepository.load(new ColumnBindId(1L))).thenReturn(Mono.just(domain));
        when(genColumnBindDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.update(command))
                .verifyComplete();

        verify(genColumnBindDomainRepository).load(new ColumnBindId(1L));
        verify(genColumnBindDomainRepository).save(domain);
    }

    @Test
    @DisplayName("delete 应调用 delete 领域服务")
    void delete_shouldCallDomainService() {
        List<Long> ids = List.of(1L, 2L);
        when(deleteGenColumnBindDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(service.delete(ids))
                .verifyComplete();

        verify(deleteGenColumnBindDomainService).deleteByIds(ids);
    }

    @Test
    @DisplayName("wipe 应调用 wipe 领域服务")
    void wipe_shouldCallDomainService() {
        List<Long> ids = List.of(1L, 2L);
        when(wipeGenColumnBindByIdsDomainService.wipeByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(service.wipe(ids))
                .verifyComplete();

        verify(wipeGenColumnBindByIdsDomainService).wipeByIds(ids);
    }

    @Test
    @DisplayName("restore 应调用 restore 领域服务")
    void restore_shouldCallDomainService() {
        List<Long> ids = List.of(1L, 2L);
        when(restoreGenColumnBindDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(service.restore(ids))
                .verifyComplete();

        verify(restoreGenColumnBindDomainService).restoreByIds(ids);
    }
}
