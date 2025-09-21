package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.entity.GenAggregateEntity;
import com.springddd.infrastructure.persistence.r2dbc.GenAggregateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GenAggregateDomainRepositoryImpl Tests")
class GenAggregateDomainRepositoryImplTest {

    @Mock
    private GenAggregateRepository genAggregateRepository;

    @InjectMocks
    private GenAggregateDomainRepositoryImpl genAggregateDomainRepository;

    private GenAggregateEntity testEntity;

    @BeforeEach
    void setUp() {
        testEntity = new GenAggregateEntity();
        testEntity.setId(1L);
        testEntity.setInfoId(10L);
        testEntity.setObjectName("TestObject");
        testEntity.setObjectValue("test_value");
        testEntity.setObjectType((byte) 1);
        testEntity.setHasCreated(true);
        testEntity.setCreateBy("admin");
        testEntity.setCreateTime(LocalDateTime.now());
        testEntity.setUpdateBy("admin");
        testEntity.setUpdateTime(LocalDateTime.now());
        testEntity.setVersion(1);
    }

    @Test
    @DisplayName("load() should return domain when entity exists")
    void load_WhenEntityExists_ReturnsDomain() {
        when(genAggregateRepository.findById(1L)).thenReturn(Mono.just(testEntity));

        StepVerifier.create(genAggregateDomainRepository.load(new AggregateId(1L)))
                .assertNext(domain -> {
                    assertThat(domain.getAggregateId().value()).isEqualTo(1L);
                    assertThat(domain.getInfoId().value()).isEqualTo(10L);
                    assertThat(domain.getValueObject().objectName()).isEqualTo("TestObject");
                    assertThat(domain.getValueObject().objectValue()).isEqualTo("test_value");
                    assertThat(domain.getValueObject().objectType()).isEqualTo((byte) 1);
                    assertThat(domain.getExtendInfo().hasCreated()).isTrue();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("load() should return empty when entity not found")
    void load_WhenEntityNotFound_ReturnsEmpty() {
        when(genAggregateRepository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(genAggregateDomainRepository.load(new AggregateId(999L)))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() should persist domain and return id")
    void save_WhenValidDomain_ReturnsId() {
        when(genAggregateRepository.save(any(GenAggregateEntity.class))).thenReturn(Mono.just(testEntity));

        GenAggregateDomain domain = new GenAggregateDomain();
        domain.setAggregateId(new AggregateId(1L));
        domain.setInfoId(new InfoId(10L));
        domain.setValueObject(new GenAggregateValueObject("TestObject", "test_value", (byte) 1));
        domain.setExtendInfo(new GenAggregateExtendInfo(true));
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setVersion(1);

        StepVerifier.create(genAggregateDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() with null id should create new entity")
    void save_WithNullId_CreatesNewEntity() {
        GenAggregateEntity newEntity = new GenAggregateEntity();
        newEntity.setId(2L);
        when(genAggregateRepository.save(any(GenAggregateEntity.class))).thenReturn(Mono.just(newEntity));

        GenAggregateDomain domain = new GenAggregateDomain();
        domain.setAggregateId(null);
        domain.setInfoId(new InfoId(10L));
        domain.setValueObject(new GenAggregateValueObject("NewObject", "new_value", (byte) 2));
        domain.setExtendInfo(new GenAggregateExtendInfo(false));

        StepVerifier.create(genAggregateDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(2L))
                .verifyComplete();
    }
}
