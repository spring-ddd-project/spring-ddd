package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.SysDictItemCommand;
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
class SysDictItemCommandServiceTest {

    @Mock
    private RepositoryFactory repositoryFactory;

    @Mock
    private SysDictItemDomainFactory sysDictItemDomainFactory;

    @Mock
    private WipeSysDictItemByIdsDomainService wipeSysDictItemByIdsDomainService;

    @Mock
    private DeleteSysDictItemByIdDomainService deleteSysDictItemByIdDomainService;

    @Mock
    private RestoreSysDictItemByIdDomainService restoreSysDictItemByIdDomainService;

    @Mock
    private SysDictItemDomainRepository sysDictItemDomainRepository;

    @InjectMocks
    private SysDictItemCommandService service;

    @Test
    @DisplayName("create 应创建字典项并返回 ID")
    void create_shouldCreateDictItemAndReturnId() {
        SysDictItemCommand command = new SysDictItemCommand();
        command.setDictId(1L);
        command.setItemLabel("Male");
        command.setItemValue(1);
        command.setSortOrder(1);
        command.setSortOrder(1);
        command.setItemStatus(true);

        SysDictItemDomain domain = new SysDictItemDomain();
        when(sysDictItemDomainFactory.newInstance(any(DictId.class), any(DictItemBasicInfo.class), any(DictItemExtendInfo.class))).thenReturn(domain);
        when(repositoryFactory.getSysDictItemDomainRepository()).thenReturn(sysDictItemDomainRepository);
        when(sysDictItemDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.create(command))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();

        verify(sysDictItemDomainFactory).newInstance(any(DictId.class), any(DictItemBasicInfo.class), any(DictItemExtendInfo.class));
        verify(sysDictItemDomainRepository).save(domain);
    }

    @Test
    @DisplayName("update 应更新字典项")
    void update_shouldUpdateDictItem() {
        SysDictItemCommand command = new SysDictItemCommand();
        command.setId(1L);
        command.setDictId(1L);
        command.setItemLabel("Updated");
        command.setItemValue(1);
        command.setSortOrder(1);
        command.setItemStatus(true);

        SysDictItemDomain domain = new SysDictItemDomain();
        when(repositoryFactory.getSysDictItemDomainRepository()).thenReturn(sysDictItemDomainRepository);
        when(sysDictItemDomainRepository.load(new DictItemId(1L))).thenReturn(Mono.just(domain));
        when(sysDictItemDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.update(command))
                .verifyComplete();

        verify(sysDictItemDomainRepository).load(new DictItemId(1L));
        verify(sysDictItemDomainRepository).save(domain);
    }

    @Test
    @DisplayName("delete 应调用 deleteByIds 领域服务")
    void delete_shouldCallDomainService() {
        List<Long> ids = List.of(1L, 2L);
        when(deleteSysDictItemByIdDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(service.delete(ids))
                .verifyComplete();

        verify(deleteSysDictItemByIdDomainService).deleteByIds(ids);
    }

    @Test
    @DisplayName("wipe 应调用 wipe 领域服务")
    void wipe_shouldCallDomainService() {
        List<Long> ids = List.of(1L, 2L);
        when(wipeSysDictItemByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(service.wipe(ids))
                .verifyComplete();

        verify(wipeSysDictItemByIdsDomainService).deleteByIds(ids);
    }

    @Test
    @DisplayName("restore 应调用 restore 领域服务")
    void restore_shouldCallDomainService() {
        List<Long> ids = List.of(1L, 2L);
        when(restoreSysDictItemByIdDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(service.restore(ids))
                .verifyComplete();

        verify(restoreSysDictItemByIdDomainService).restoreByIds(ids);
    }
}
