package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenAggregateCommand;
import com.springddd.domain.gen.*;
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
class GenAggregateCommandServiceTest {

    @Mock
    private GenAggregateDomainRepository genAggregateDomainRepository;

    @Mock
    private GenAggregateDomainFactory aggregateDomainFactory;

    @Mock
    private WipeGenAggregateDomainService wipeGenAggregateDomainService;

    @InjectMocks
    private GenAggregateCommandService genAggregateCommandService;

    private GenAggregateCommand createCommand;
    private GenAggregateDomain mockDomain;

    @BeforeEach
    void setUp() {
        createCommand = new GenAggregateCommand();
        createCommand.setInfoId(1L);
        createCommand.setObjectName("testObject");
        createCommand.setObjectValue("value");
        createCommand.setObjectType((byte) 1);
        createCommand.setHasCreated(true);

        mockDomain = new GenAggregateDomain();
        mockDomain.setInfoId(new InfoId(1L));
    }

    @Test
    void create_shouldCreateAggregate() {
        when(aggregateDomainFactory.newInstance(any(InfoId.class), any(GenAggregateValueObject.class), any(GenAggregateExtendInfo.class)))
                .thenReturn(mockDomain);
        when(genAggregateDomainRepository.save(any(GenAggregateDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(genAggregateCommandService.create(createCommand))
                .expectNext(1L)
                .verifyComplete();

        verify(aggregateDomainFactory).newInstance(any(InfoId.class), any(GenAggregateValueObject.class), any(GenAggregateExtendInfo.class));
        verify(genAggregateDomainRepository).save(any(GenAggregateDomain.class));
    }

    @Test
    void wipe_shouldCallWipeDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeGenAggregateDomainService.wipe(ids)).thenReturn(Mono.empty());

        StepVerifier.create(genAggregateCommandService.wipe(ids))
                .verifyComplete();

        verify(wipeGenAggregateDomainService).wipe(ids);
    }
}
