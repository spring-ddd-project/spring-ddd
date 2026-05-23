package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.AggregateId;
import com.springddd.domain.gen.GenAggregateDomain;
import com.springddd.infrastructure.persistence.entity.GenAggregateEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.GenAggregateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GenAggregateDomainRepositoryImplTest {

    @Mock
    private GenAggregateRepository genAggregateRepository;

    @Mock
    private EntityFactory entityFactory;

    @InjectMocks
    private GenAggregateDomainRepositoryImpl repository;

    @Test
    @DisplayName("load 应通过 findById 和 entityFactory 返回 domain")
    void load_shouldReturnDomain() {
        AggregateId aggregateId = new AggregateId(1L);
        GenAggregateEntity entity = new GenAggregateEntity();
        GenAggregateDomain domain = new GenAggregateDomain();

        given(genAggregateRepository.findById(1L)).willReturn(Mono.just(entity));
        given(entityFactory.createGenAggregateDomain(entity)).willReturn(domain);

        StepVerifier.create(repository.load(aggregateId))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("load 当记录不存在时应返回空 Mono")
    void load_whenNotFound_shouldReturnEmpty() {
        AggregateId aggregateId = new AggregateId(1L);

        given(genAggregateRepository.findById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.load(aggregateId))
                .verifyComplete();
    }

    @Test
    @DisplayName("save 应通过 entityFactory 转换并返回 id")
    void save_shouldReturnId() {
        GenAggregateDomain domain = new GenAggregateDomain();
        domain.setAggregateId(new AggregateId(1L));
        GenAggregateEntity entity = new GenAggregateEntity();
        GenAggregateEntity savedEntity = new GenAggregateEntity();
        savedEntity.setId(1L);

        given(entityFactory.createGenAggregateEntity(domain)).willReturn(entity);
        given(genAggregateRepository.save(entity)).willReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete 应调用 deleteById 并返回 Mono<Void>")
    void delete_shouldCallDeleteById() {
        GenAggregateDomain domain = new GenAggregateDomain();
        domain.setAggregateId(new AggregateId(1L));

        given(genAggregateRepository.deleteById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.delete(domain))
                .verifyComplete();

        verify(genAggregateRepository).deleteById(1L);
    }
}
