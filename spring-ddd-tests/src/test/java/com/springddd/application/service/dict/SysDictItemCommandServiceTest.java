package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.SysDictItemCommand;
import com.springddd.domain.dict.*;
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
class SysDictItemCommandServiceTest {

    @Mock
    private SysDictItemDomainRepository sysDictItemDomainRepository;

    @Mock
    private SysDictItemDomainFactory sysDictItemDomainFactory;

    @Mock
    private WipeSysDictItemByIdsDomainService wipeSysDictItemByIdsDomainService;

    @Mock
    private DeleteSysDictItemByIdDomainService deleteSysDictItemByIdDomainService;

    @Mock
    private RestoreSysDictItemByIdDomainService restoreSysDictItemByIdDomainService;

    @InjectMocks
    private SysDictItemCommandService sysDictItemCommandService;

    private SysDictItemCommand createCommand;
    private SysDictItemDomain mockDomain;

    @BeforeEach
    void setUp() {
        createCommand = new SysDictItemCommand();
        createCommand.setDictId(1L);
        createCommand.setItemLabel("Test Item");
        createCommand.setItemValue(1);
        createCommand.setSortOrder(1);
        createCommand.setItemStatus(true);

        mockDomain = new SysDictItemDomain();
        mockDomain.setDictId(new DictId(1L));
        mockDomain.setItemBasicInfo(new DictItemBasicInfo("Test Item", 1));
        mockDomain.setItemExtendInfo(new DictItemExtendInfo(1, true));
        mockDomain.setDeleteStatus(false);
    }

    @Test
    void create_shouldCreateDictItem() {
        when(sysDictItemDomainFactory.newInstance(any(DictId.class), any(DictItemBasicInfo.class), any(DictItemExtendInfo.class)))
                .thenReturn(mockDomain);
        when(sysDictItemDomainRepository.save(any(SysDictItemDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(sysDictItemCommandService.create(createCommand))
                .expectNext(1L)
                .verifyComplete();

        verify(sysDictItemDomainFactory).newInstance(any(DictId.class), any(DictItemBasicInfo.class), any(DictItemExtendInfo.class));
        verify(sysDictItemDomainRepository).save(any(SysDictItemDomain.class));
    }

    @Test
    void update_shouldUpdateDictItem() {
        SysDictItemCommand updateCommand = new SysDictItemCommand();
        updateCommand.setId(1L);
        updateCommand.setDictId(1L);
        updateCommand.setItemLabel("Updated Item");
        updateCommand.setItemValue(2);
        updateCommand.setSortOrder(2);
        updateCommand.setItemStatus(false);

        when(sysDictItemDomainRepository.load(any(DictItemId.class))).thenReturn(Mono.just(mockDomain));
        when(sysDictItemDomainRepository.save(any(SysDictItemDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(sysDictItemCommandService.update(updateCommand))
                .verifyComplete();

        verify(sysDictItemDomainRepository).load(any(DictItemId.class));
        verify(sysDictItemDomainRepository).save(any(SysDictItemDomain.class));
    }

    @Test
    void update_shouldCompleteWhenDomainNotFound() {
        SysDictItemCommand updateCommand = new SysDictItemCommand();
        updateCommand.setId(999L);
        updateCommand.setDictId(1L);
        updateCommand.setItemLabel("Updated Item");
        updateCommand.setItemValue(2);

        when(sysDictItemDomainRepository.load(any(DictItemId.class))).thenReturn(Mono.empty());

        StepVerifier.create(sysDictItemCommandService.update(updateCommand))
                .verifyComplete();

        verify(sysDictItemDomainRepository, never()).save(any());
    }

    @Test
    void delete_shouldCallDeleteDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(deleteSysDictItemByIdDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysDictItemCommandService.delete(ids))
                .verifyComplete();

        verify(deleteSysDictItemByIdDomainService).deleteByIds(ids);
    }

    @Test
    void wipe_shouldCallWipeDomainService() {
        List<Long> ids = Arrays.asList(1L);
        when(wipeSysDictItemByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysDictItemCommandService.wipe(ids))
                .verifyComplete();

        verify(wipeSysDictItemByIdsDomainService).deleteByIds(ids);
    }

    @Test
    void restore_shouldCallRestoreDomainService() {
        List<Long> ids = Arrays.asList(1L);
        when(restoreSysDictItemByIdDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysDictItemCommandService.restore(ids))
                .verifyComplete();

        verify(restoreSysDictItemByIdDomainService).restoreByIds(ids);
    }
}
