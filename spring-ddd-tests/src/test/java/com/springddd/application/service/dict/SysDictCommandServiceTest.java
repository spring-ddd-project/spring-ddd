package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.SysDictCommand;
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
class SysDictCommandServiceTest {

    @Mock
    private SysDictDomainRepository sysDictDomainRepository;

    @Mock
    private SysDictDomainFactory sysDictDomainFactory;

    @Mock
    private WipeSysDictByIdsDomainService wipeSysDictByIdsDomainService;

    @Mock
    private DeleteSysDictByIdDomainService deleteSysDictByIdDomainService;

    @Mock
    private RestoreSysDictByIdDomainService restoreSysDictByIdDomainService;

    @InjectMocks
    private SysDictCommandService sysDictCommandService;

    private SysDictCommand createCommand;
    private SysDictDomain mockDomain;

    @BeforeEach
    void setUp() {
        createCommand = new SysDictCommand();
        createCommand.setDictName("Test Dict");
        createCommand.setDictCode("TEST_DICT");
        createCommand.setSortOrder(1);
        createCommand.setDictStatus(true);

        mockDomain = new SysDictDomain();
        mockDomain.setDictBasicInfo(new DictBasicInfo("Test Dict", "TEST_DICT"));
        mockDomain.setDictExtendInfo(new DictExtendInfo(1, true));
        mockDomain.setDeleteStatus(false);
    }

    @Test
    void create_shouldCreateDictionary() {
        when(sysDictDomainFactory.newInstance(any(DictBasicInfo.class), any(DictExtendInfo.class)))
                .thenReturn(mockDomain);
        when(sysDictDomainRepository.save(any(SysDictDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(sysDictCommandService.create(createCommand))
                .expectNext(1L)
                .verifyComplete();

        verify(sysDictDomainFactory).newInstance(any(DictBasicInfo.class), any(DictExtendInfo.class));
        verify(sysDictDomainRepository).save(any(SysDictDomain.class));
    }

    @Test
    void update_shouldUpdateDictionary() {
        SysDictCommand updateCommand = new SysDictCommand();
        updateCommand.setId(1L);
        updateCommand.setDictName("Updated Dict");
        updateCommand.setDictCode("UPDATED_DICT");
        updateCommand.setSortOrder(2);
        updateCommand.setDictStatus(false);

        when(sysDictDomainRepository.load(any(DictId.class))).thenReturn(Mono.just(mockDomain));
        when(sysDictDomainRepository.save(any(SysDictDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(sysDictCommandService.update(updateCommand))
                .verifyComplete();

        verify(sysDictDomainRepository).load(any(DictId.class));
        verify(sysDictDomainRepository).save(any(SysDictDomain.class));
    }

    @Test
    void update_shouldCompleteWhenDomainNotFound() {
        SysDictCommand updateCommand = new SysDictCommand();
        updateCommand.setId(999L);
        updateCommand.setDictName("Updated Dict");
        updateCommand.setDictCode("UPDATED_DICT");

        when(sysDictDomainRepository.load(any(DictId.class))).thenReturn(Mono.empty());

        StepVerifier.create(sysDictCommandService.update(updateCommand))
                .verifyComplete();

        verify(sysDictDomainRepository, never()).save(any());
    }

    @Test
    void delete_shouldCallDeleteDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(deleteSysDictByIdDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysDictCommandService.delete(ids))
                .verifyComplete();

        verify(deleteSysDictByIdDomainService).deleteByIds(ids);
    }

    @Test
    void wipe_shouldCallWipeDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeSysDictByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysDictCommandService.wipe(ids))
                .verifyComplete();

        verify(wipeSysDictByIdsDomainService).deleteByIds(ids);
    }

    @Test
    void restore_shouldCallRestoreDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(restoreSysDictByIdDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysDictCommandService.restore(ids))
                .verifyComplete();

        verify(restoreSysDictByIdDomainService).restoreByIds(ids);
    }
}
