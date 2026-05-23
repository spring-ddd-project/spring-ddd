package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.SysDictCommand;
import com.springddd.domain.dict.*;
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
class SysDictCommandServiceTest {

    @Mock
    private RepositoryFactory repositoryFactory;

    @Mock
    private SysDictDomainFactory sysDictDomainFactory;

    @Mock
    private WipeSysDictByIdsDomainService wipeSysDictByIdsDomainService;

    @Mock
    private DeleteSysDictByIdDomainService deleteSysDictByIdDomainService;

    @Mock
    private RestoreSysDictByIdDomainService restoreSysDictByIdDomainService;

    @Mock
    private SysDictDomainRepository sysDictDomainRepository;

    @InjectMocks
    private SysDictCommandService service;

    @Test
    @DisplayName("create 应创建字典并返回 ID")
    void create_shouldCreateDictAndReturnId() {
        SysDictCommand command = new SysDictCommand();
        command.setDictName("Status");
        command.setDictCode("status");
        command.setSortOrder(1);
        command.setDictStatus(true);
        command.setDictStatus(true);

        SysDictDomain domain = new SysDictDomain();
        when(sysDictDomainFactory.newInstance(any(DictBasicInfo.class), any(DictExtendInfo.class))).thenReturn(domain);
        when(repositoryFactory.getSysDictDomainRepository()).thenReturn(sysDictDomainRepository);
        when(sysDictDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.create(command))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();

        verify(sysDictDomainFactory).newInstance(any(DictBasicInfo.class), any(DictExtendInfo.class));
        verify(sysDictDomainRepository).save(domain);
    }

    @Test
    @DisplayName("update 应更新字典")
    void update_shouldUpdateDict() {
        SysDictCommand command = new SysDictCommand();
        command.setId(1L);
        command.setDictName("Updated");
        command.setDictCode("updated");
        command.setSortOrder(1);
        command.setDictStatus(true);

        SysDictDomain domain = new SysDictDomain();
        when(repositoryFactory.getSysDictDomainRepository()).thenReturn(sysDictDomainRepository);
        when(sysDictDomainRepository.load(new DictId(1L))).thenReturn(Mono.just(domain));
        when(sysDictDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.update(command))
                .verifyComplete();

        verify(sysDictDomainRepository).load(new DictId(1L));
        verify(sysDictDomainRepository).save(domain);
    }

    @Test
    @DisplayName("delete 应调用 deleteByIds 领域服务")
    void delete_shouldCallDomainService() {
        List<Long> ids = List.of(1L, 2L);
        when(deleteSysDictByIdDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(service.delete(ids))
                .verifyComplete();

        verify(deleteSysDictByIdDomainService).deleteByIds(ids);
    }

    @Test
    @DisplayName("wipe 应调用 wipe 领域服务")
    void wipe_shouldCallDomainService() {
        List<Long> ids = List.of(1L, 2L);
        when(wipeSysDictByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(service.wipe(ids))
                .verifyComplete();

        verify(wipeSysDictByIdsDomainService).deleteByIds(ids);
    }

    @Test
    @DisplayName("restore 应调用 restore 领域服务")
    void restore_shouldCallDomainService() {
        List<Long> ids = List.of(1L, 2L);
        when(restoreSysDictByIdDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(service.restore(ids))
                .verifyComplete();

        verify(restoreSysDictByIdDomainService).restoreByIds(ids);
    }
}
