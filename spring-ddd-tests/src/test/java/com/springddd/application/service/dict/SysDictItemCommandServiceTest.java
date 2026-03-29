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
        command.setItemLabel("Item Label");
        command.setItemValue(1);
        command.setSortOrder(1);
        command.setItemStatus(true);

        SysDictItemDomain mockDomain = new SysDictItemDomain();
        when(sysDictItemDomainFactory.newInstance(any(), any(), any())).thenReturn(mockDomain);
        when(sysDictItemDomainRepository.save(any())).thenReturn(Mono.just(1L));

        Mono<Long> result = sysDictItemCommandService.create(command);

        StepVerifier.create(result)
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void update_shouldComplete_whenValidCommand() {
        SysDictItemCommand command = new SysDictItemCommand();
        command.setId(1L);
        command.setDictId(1L);
        command.setItemLabel("Updated Item");
        command.setItemValue(2);
        command.setSortOrder(2);
        command.setItemStatus(false);

        SysDictItemDomain mockDomain = new SysDictItemDomain();
        when(sysDictItemDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(sysDictItemDomainRepository.save(any())).thenReturn(Mono.just(1L));

        Mono<Void> result = sysDictItemCommandService.update(command);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void delete_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(deleteSysDictItemByIdDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = sysDictItemCommandService.delete(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipe_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeSysDictItemByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = sysDictItemCommandService.wipe(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void restore_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(restoreSysDictItemByIdDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = sysDictItemCommandService.restore(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
