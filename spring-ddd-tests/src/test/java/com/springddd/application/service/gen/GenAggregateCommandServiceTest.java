package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenAggregateCommand;
import com.springddd.domain.gen.*;
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
class GenAggregateCommandServiceTest {

    @Mock
    private RepositoryFactory repositoryFactory;

    @Mock
    private GenAggregateDomainFactory aggregateDomainFactory;

    @Mock
    private WipeGenAggregateDomainService wipeGenAggregateDomainService;

    @Mock
    private GenAggregateDomainRepository genAggregateDomainRepository;

    @InjectMocks
    private GenAggregateCommandService service;

    @Test
    @DisplayName("create 应创建聚合并返回 ID")
    void create_shouldCreateAndReturnId() {
        GenAggregateCommand command = new GenAggregateCommand();
        command.setInfoId(1L);
        command.setObjectName("User");
        command.setObjectValue("user");
        command.setObjectType((byte) 1);
        command.setHasCreated(false);

        GenAggregateDomain domain = new GenAggregateDomain();
        when(aggregateDomainFactory.newInstance(any(InfoId.class), any(GenAggregateValueObject.class), any(GenAggregateExtendInfo.class))).thenReturn(domain);
        when(repositoryFactory.getGenAggregateDomainRepository()).thenReturn(genAggregateDomainRepository);
        when(genAggregateDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.create(command))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();

        verify(aggregateDomainFactory).newInstance(any(InfoId.class), any(GenAggregateValueObject.class), any(GenAggregateExtendInfo.class));
        verify(genAggregateDomainRepository).save(domain);
    }

    @Test
    @DisplayName("update 应更新聚合")
    void update_shouldUpdate() {
        GenAggregateCommand command = new GenAggregateCommand();
        command.setId(1L);
        command.setInfoId(1L);
        command.setObjectName("Updated");
        command.setObjectValue("updated");
        command.setObjectType((byte) 1);

        GenAggregateDomain domain = new GenAggregateDomain();
        domain.setDeleteStatus(false);
        when(repositoryFactory.getGenAggregateDomainRepository()).thenReturn(genAggregateDomainRepository);
        when(genAggregateDomainRepository.load(new AggregateId(1L))).thenReturn(Mono.just(domain));
        when(genAggregateDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.update(command))
                .verifyComplete();

        verify(genAggregateDomainRepository).load(new AggregateId(1L));
        verify(genAggregateDomainRepository).save(domain);
    }

    @Test
    @DisplayName("wipe 应调用 wipe 领域服务")
    void wipe_shouldCallDomainService() {
        List<Long> ids = List.of(1L, 2L);
        when(wipeGenAggregateDomainService.wipe(ids)).thenReturn(Mono.empty());

        StepVerifier.create(service.wipe(ids))
                .verifyComplete();

        verify(wipeGenAggregateDomainService).wipe(ids);
    }

    @Test
    @DisplayName("delete 应软删除聚合")
    void delete_shouldSoftDelete() {
        List<Long> ids = List.of(1L);
        GenAggregateDomain domain = new GenAggregateDomain();
        domain.setDeleteStatus(false);
        when(repositoryFactory.getGenAggregateDomainRepository()).thenReturn(genAggregateDomainRepository);
        when(genAggregateDomainRepository.load(new AggregateId(1L))).thenReturn(Mono.just(domain));
        when(genAggregateDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.delete(ids))
                .verifyComplete();

        verify(genAggregateDomainRepository).load(new AggregateId(1L));
        verify(genAggregateDomainRepository).save(domain);
    }

    @Test
    @DisplayName("restore 应恢复聚合")
    void restore_shouldRestore() {
        List<Long> ids = List.of(1L);
        GenAggregateDomain domain = new GenAggregateDomain();
        domain.setDeleteStatus(true);
        when(repositoryFactory.getGenAggregateDomainRepository()).thenReturn(genAggregateDomainRepository);
        when(genAggregateDomainRepository.load(new AggregateId(1L))).thenReturn(Mono.just(domain));
        when(genAggregateDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.restore(ids))
                .verifyComplete();

        verify(genAggregateDomainRepository).load(new AggregateId(1L));
        verify(genAggregateDomainRepository).save(domain);
    }
}
