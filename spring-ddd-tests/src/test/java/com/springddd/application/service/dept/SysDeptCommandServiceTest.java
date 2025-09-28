package com.springddd.application.service.dept;

import com.springddd.application.service.dept.dto.SysDeptCommand;
import com.springddd.domain.dept.*;
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
class SysDeptCommandServiceTest {

    @Mock
    private SysDeptDomainRepository sysDeptDomainRepository;

    @Mock
    private SysDeptDomainFactory sysDeptDomainFactory;

    @Mock
    private WipeSysDeptByIdsDomainService wipeSysDeptByIdsDomainService;

    @Mock
    private DeleteSysDeptByIdDomainService deleteSysDeptByIdDomainService;

    @Mock
    private RestoreSysDeptByIdDomainService restoreSysDeptByIdDomainService;

    private SysDeptCommandService sysDeptCommandService;

    @BeforeEach
    void setUp() {
        sysDeptCommandService = new SysDeptCommandService(
                sysDeptDomainRepository,
                sysDeptDomainFactory,
                wipeSysDeptByIdsDomainService,
                deleteSysDeptByIdDomainService,
                restoreSysDeptByIdDomainService
        );
    }

    @Test
    void create_shouldReturnId_whenValidCommand() {
        SysDeptCommand command = new SysDeptCommand();
        command.setParentId(0L);
        command.setDeptName("Test Department");
        command.setSortOrder(1);
        command.setDeptStatus(true);

        SysDeptDomain mockDomain = new SysDeptDomain();
        when(sysDeptDomainFactory.newInstance(any(), any(), any())).thenReturn(mockDomain);
        when(sysDeptDomainRepository.save(any())).thenReturn(Mono.just(1L));

        Mono<Long> result = sysDeptCommandService.create(command);

        StepVerifier.create(result)
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void update_shouldComplete_whenValidCommand() {
        SysDeptCommand command = new SysDeptCommand();
        command.setId(1L);
        command.setParentId(0L);
        command.setDeptName("Updated Department");
        command.setSortOrder(2);
        command.setDeptStatus(false);

        SysDeptDomain mockDomain = new SysDeptDomain();
        when(sysDeptDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(sysDeptDomainRepository.save(any())).thenReturn(Mono.just(1L));

        Mono<Void> result = sysDeptCommandService.update(command);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void delete_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(deleteSysDeptByIdDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = sysDeptCommandService.delete(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipe_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeSysDeptByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = sysDeptCommandService.wipe(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void restore_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(restoreSysDeptByIdDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = sysDeptCommandService.restore(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
