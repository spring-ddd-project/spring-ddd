package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.entity.GenAggregateEntity;
import com.springddd.infrastructure.persistence.r2dbc.GenAggregateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenAggregateDomainRepositoryImplTest {

    @Mock
    private GenAggregateRepository genAggregateRepository;

    @InjectMocks
    private GenAggregateDomainRepositoryImpl repository;

    @Test
    void load_shouldReturnDomain_whenEntityExists() {
        GenAggregateEntity entity = new GenAggregateEntity();
        entity.setId(1L);
        entity.setInfoId(1L);
        entity.setObjectName("User");
        entity.setObjectValue("value");
        entity.setObjectType((byte) 1);
        entity.setHasCreated(true);
        entity.setVersion(0);
        entity.setCreateBy("system");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("system");
        entity.setUpdateTime(LocalDateTime.now());

        when(genAggregateRepository.findById(1L)).thenReturn(Mono.just(entity));

        StepVerifier.create(repository.load(new AggregateId(1L)))
                .assertNext(domain -> {
                    assertEquals(1L, domain.getAggregateId().value());
                    assertEquals("User", domain.getValueObject().objectName());
                    assertEquals(true, domain.getExtendInfo().hasCreated());
                })
                .verifyComplete();
    }

    @Test
    void load_shouldReturnEmpty_whenEntityNotFound() {
        when(genAggregateRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(repository.load(new AggregateId(1L)))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenSavingNewAggregate() {
        GenAggregateDomain domain = new GenAggregateDomain();
        domain.setAggregateId(null);
        domain.setInfoId(new InfoId(1L));
        domain.setValueObject(new GenAggregateValueObject("User", "value", (byte) 1));
        domain.setExtendInfo(new GenAggregateExtendInfo(true));
        domain.setVersion(0);

        GenAggregateEntity savedEntity = new GenAggregateEntity();
        savedEntity.setId(1L);

        when(genAggregateRepository.save(any(GenAggregateEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenUpdatingExistingAggregate() {
        GenAggregateDomain domain = new GenAggregateDomain();
        domain.setAggregateId(new AggregateId(1L));
        domain.setInfoId(new InfoId(1L));
        domain.setValueObject(new GenAggregateValueObject("User", "value", (byte) 1));
        domain.setExtendInfo(new GenAggregateExtendInfo(true));
        domain.setVersion(1);

        GenAggregateEntity savedEntity = new GenAggregateEntity();
        savedEntity.setId(1L);

        when(genAggregateRepository.save(any(GenAggregateEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }
}
