package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.SysDictItemCommand;
import com.springddd.domain.dict.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    private SysDictItemCommandService sysDictItemCommandService;

    @BeforeEach
    void setUp() {
        sysDictItemCommandService = new SysDictItemCommandService(
                sysDictItemDomainRepository,
                sysDictItemDomainFactory,
                wipeSysDictItemByIdsDomainService,
                deleteSysDictItemByIdDomainService,
                restoreSysDictItemByIdDomainService
        );
    }

    @Test
    void create_shouldReturnId_whenValidCommand() {
        SysDictItemCommand command = new SysDictItemCommand();
        command.setDictId(1L);
        command.setItemLabel("Label");
        command.setItemValue(1);
        command.setSortOrder(1);
        command.setItemStatus(true);

        SysDictItemDomain mockDomain = new SysDictItemDomain();
        when(sysDictItemDomainFactory.newInstance(any(), any(), any())).thenReturn(mockDomain);
        when(sysDictItemDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(sysDictItemCommandService.create(command))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void update_shouldComplete_whenValidCommand() {
        SysDictItemCommand command = new SysDictItemCommand();
        command.setId(1L);
        command.setDictId(1L);
        command.setItemLabel("Updated Label");
        command.setItemValue(2);
        command.setSortOrder(2);
        command.setItemStatus(false);

        SysDictItemDomain mockDomain = new SysDictItemDomain();
        when(sysDictItemDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(sysDictItemDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(sysDictItemCommandService.update(command))
                .verifyComplete();
    }

    @Test
    void delete_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(deleteSysDictItemByIdDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysDictItemCommandService.delete(ids))
                .verifyComplete();
    }

    @Test
    void wipe_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeSysDictItemByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysDictItemCommandService.wipe(ids))
                .verifyComplete();
    }

    @Test
    void restore_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(restoreSysDictItemByIdDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysDictItemCommandService.restore(ids))
                .verifyComplete();
    }
}
