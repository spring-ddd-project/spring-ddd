package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenAggregateCommand;
import com.springddd.domain.gen.*;
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
class GenAggregateCommandServiceTest {

    @Mock
    private GenAggregateDomainRepository genAggregateDomainRepository;

    @Mock
    private GenAggregateDomainFactory aggregateDomainFactory;

    @Mock
    private WipeGenAggregateDomainService wipeGenAggregateDomainService;

    private GenAggregateCommandService genAggregateCommandService;

    @BeforeEach
    void setUp() {
        genAggregateCommandService = new GenAggregateCommandService(
                genAggregateDomainRepository,
                aggregateDomainFactory,
                wipeGenAggregateDomainService
        );
    }

    @Test
    void create_shouldReturnId_whenValidCommand() {
        GenAggregateCommand command = new GenAggregateCommand();
        command.setInfoId(1L);
        command.setObjectName("TestObject");
        command.setObjectValue("testValue");
        command.setObjectType((byte) 1);
        command.setHasCreated(true);

        GenAggregateDomain mockDomain = new GenAggregateDomain();
        when(aggregateDomainFactory.newInstance(any(), any(), any())).thenReturn(mockDomain);
        when(genAggregateDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(genAggregateCommandService.create(command))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void update_shouldComplete_whenValidCommand() {
        GenAggregateCommand command = new GenAggregateCommand();
        command.setId(1L);
        command.setInfoId(1L);
        command.setObjectName("UpdatedObject");
        command.setObjectValue("updatedValue");
        command.setObjectType((byte) 2);
        command.setHasCreated(false);

        GenAggregateDomain mockDomain = new GenAggregateDomain();
        when(genAggregateDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(genAggregateDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(genAggregateCommandService.update(command))
                .verifyComplete();
    }

    @Test
    void wipe_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeGenAggregateDomainService.wipe(ids)).thenReturn(Mono.empty());

        StepVerifier.create(genAggregateCommandService.wipe(ids))
                .verifyComplete();
    }

    @Test
    void delete_shouldComplete_whenValidIds() {
        List<Long> ids = Arrays.asList(1L);
        GenAggregateDomain mockDomain = new GenAggregateDomain();
        mockDomain.setDeleteStatus(false);
        when(genAggregateDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(genAggregateDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(genAggregateCommandService.delete(ids))
                .verifyComplete();
    }

    @Test
    void restore_shouldComplete_whenValidIds() {
        List<Long> ids = Arrays.asList(1L);
        GenAggregateDomain mockDomain = new GenAggregateDomain();
        mockDomain.setDeleteStatus(true);
        when(genAggregateDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(genAggregateDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(genAggregateCommandService.restore(ids))
                .verifyComplete();
    }
}
