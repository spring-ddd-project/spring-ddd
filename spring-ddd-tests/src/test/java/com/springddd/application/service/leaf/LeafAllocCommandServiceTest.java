package com.springddd.application.service.leaf;

import com.springddd.application.service.leaf.dto.LeafAllocCommand;
import com.springddd.domain.leaf.*;
import com.springddd.domain.leaf.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeafAllocCommandServiceTest {

    @Mock
    private LeafAllocDomainRepository leafAllocDomainRepository;

    @Mock
    private LeafAllocDomainFactory leafAllocDomainFactory;

    @Mock
    private CreateLeafAllocDomainService createLeafAllocDomainService;

    @Mock
    private UpdateLeafAllocDomainService updateLeafAllocDomainService;

    @Mock
    private DeleteLeafAllocByIdDomainService deleteLeafAllocByIdDomainService;

    @Mock
    private RestoreLeafAllocByIdDomainService restoreLeafAllocByIdDomainService;

    @Mock
    private WipeLeafAllocByIdDomainService wipeLeafAllocByIdDomainService;

    private LeafAllocCommandService service;

    @BeforeEach
    void setUp() {
        service = new LeafAllocCommandService(
                leafAllocDomainRepository,
                leafAllocDomainFactory,
                createLeafAllocDomainService,
                updateLeafAllocDomainService,
                deleteLeafAllocByIdDomainService,
                restoreLeafAllocByIdDomainService,
                wipeLeafAllocByIdDomainService
        );
    }

    @Test
    void create_shouldReturnId_whenValidCommand() {
        LeafAllocCommand command = new LeafAllocCommand();
        command.setBizTag("test");
        command.setMaxId(1000L);
        command.setStep(100);
        command.setDescription("desc");
        command.setDeptId(1L);

        LeafAllocDomain domain = new LeafAllocDomain();
        domain.setLeafAllocId(new LeafAllocId(1L));
        when(leafAllocDomainFactory.newInstance(any(), any(), any(), any(), any())).thenReturn(domain);
        when(createLeafAllocDomainService.create(any(LeafAllocDomain.class))).thenReturn(Mono.just(domain));

        StepVerifier.create(service.create(command))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void update_shouldComplete_whenValidCommand() {
        LeafAllocCommand command = new LeafAllocCommand();
        command.setId(1L);
        command.setBizTag("test");
        command.setMaxId(1000L);
        command.setStep(100);
        command.setDescription("desc");
        command.setDeptId(1L);

        LeafAllocDomain domain = new LeafAllocDomain();
        when(leafAllocDomainRepository.load(any(LeafAllocId.class))).thenReturn(Mono.just(domain));
        when(updateLeafAllocDomainService.update(any(), any(), any(), any(), any(), any())).thenReturn(Mono.just(domain));

        StepVerifier.create(service.update(command))
                .verifyComplete();
    }

    @Test
    void delete_shouldDelegateToDomainService() {
        when(deleteLeafAllocByIdDomainService.delete(any(LeafAllocId.class))).thenReturn(Mono.empty());

        StepVerifier.create(service.delete(1L))
                .verifyComplete();
    }

    @Test
    void restore_shouldDelegateToDomainService() {
        when(restoreLeafAllocByIdDomainService.restore(any(LeafAllocId.class))).thenReturn(Mono.empty());

        StepVerifier.create(service.restore(1L))
                .verifyComplete();
    }

    @Test
    void wipe_shouldDelegateToDomainService() {
        when(wipeLeafAllocByIdDomainService.wipe(any(LeafAllocId.class))).thenReturn(Mono.empty());

        StepVerifier.create(service.wipe(1L))
                .verifyComplete();
    }
}
