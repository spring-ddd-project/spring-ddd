package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.SysDictCommand;
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

    private SysDictCommandService sysDictCommandService;

    @BeforeEach
    void setUp() {
        sysDictCommandService = new SysDictCommandService(
                sysDictDomainRepository,
                sysDictDomainFactory,
                wipeSysDictByIdsDomainService,
                deleteSysDictByIdDomainService,
                restoreSysDictByIdDomainService
        );
    }

    @Test
    void create_shouldReturnId_whenValidCommand() {
        SysDictCommand command = new SysDictCommand();
        command.setDictName("Test Dict");
        command.setDictCode("TEST");
        command.setSortOrder(1);
        command.setDictStatus(true);

        SysDictDomain mockDomain = new SysDictDomain();
        when(sysDictDomainFactory.newInstance(any(), any())).thenReturn(mockDomain);
        when(sysDictDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(sysDictCommandService.create(command))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void update_shouldComplete_whenValidCommand() {
        SysDictCommand command = new SysDictCommand();
        command.setId(1L);
        command.setDictName("Updated Dict");
        command.setDictCode("UPDATED");
        command.setSortOrder(2);
        command.setDictStatus(false);

        SysDictDomain mockDomain = new SysDictDomain();
        when(sysDictDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(sysDictDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(sysDictCommandService.update(command))
                .verifyComplete();
    }

    @Test
    void delete_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(deleteSysDictByIdDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysDictCommandService.delete(ids))
                .verifyComplete();
    }

    @Test
    void wipe_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeSysDictByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysDictCommandService.wipe(ids))
                .verifyComplete();
    }

    @Test
    void restore_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(restoreSysDictByIdDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysDictCommandService.restore(ids))
                .verifyComplete();
    }
}
