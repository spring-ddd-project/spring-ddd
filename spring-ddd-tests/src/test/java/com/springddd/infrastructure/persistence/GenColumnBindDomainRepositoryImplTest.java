package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.entity.GenColumnBindEntity;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnBindRepository;
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
@DisplayName("GenColumnBindDomainRepositoryImpl Tests")
class GenColumnBindDomainRepositoryImplTest {

    @Mock
    private GenColumnBindRepository genColumnBindRepository;

    @InjectMocks
    private GenColumnBindDomainRepositoryImpl genColumnBindDomainRepository;

    private GenColumnBindEntity testEntity;

    @BeforeEach
    void setUp() {
        testEntity = new GenColumnBindEntity();
        testEntity.setId(1L);
        testEntity.setColumnType("varchar");
        testEntity.setEntityType("String");
        testEntity.setComponentType((byte) 1);
        testEntity.setTypescriptType((byte) 2);
        testEntity.setDeleteStatus(false);
        testEntity.setCreateBy("admin");
        testEntity.setCreateTime(LocalDateTime.now());
        testEntity.setUpdateBy("admin");
        testEntity.setUpdateTime(LocalDateTime.now());
        testEntity.setVersion(1);
    }

    @Test
    @DisplayName("load() should return domain when entity exists")
    void load_WhenEntityExists_ReturnsDomain() {
        when(genColumnBindRepository.findById(1L)).thenReturn(Mono.just(testEntity));

        StepVerifier.create(genColumnBindDomainRepository.load(new ColumnBindId(1L)))
                .assertNext(domain -> {
                    assertThat(domain.getBindId().value()).isEqualTo(1L);
                    assertThat(domain.getBasicInfo().columnType()).isEqualTo("varchar");
                    assertThat(domain.getBasicInfo().entityType()).isEqualTo("String");
                    assertThat(domain.getBasicInfo().componentType()).isEqualTo((byte) 1);
                    assertThat(domain.getBasicInfo().typescriptType()).isEqualTo((byte) 2);
                    assertThat(domain.getDeleteStatus()).isFalse();
                    assertThat(domain.getVersion()).isEqualTo(1);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("load() should return empty when entity not found")
    void load_WhenEntityNotFound_ReturnsEmpty() {
        when(genColumnBindRepository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(genColumnBindDomainRepository.load(new ColumnBindId(999L)))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() should persist domain and return id")
    void save_WhenValidDomain_ReturnsId() {
        when(genColumnBindRepository.save(any(GenColumnBindEntity.class))).thenReturn(Mono.just(testEntity));

        GenColumnBindDomain domain = new GenColumnBindDomain();
        domain.setBindId(new ColumnBindId(1L));
        domain.setBasicInfo(new GenColumnBindBasicInfo("varchar", "String", (byte) 1, (byte) 2));
        domain.setDeleteStatus(false);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setVersion(1);

        StepVerifier.create(genColumnBindDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() with null id should create new entity")
    void save_WithNullId_CreatesNewEntity() {
        GenColumnBindEntity newEntity = new GenColumnBindEntity();
        newEntity.setId(2L);
        when(genColumnBindRepository.save(any(GenColumnBindEntity.class))).thenReturn(Mono.just(newEntity));

        GenColumnBindDomain domain = new GenColumnBindDomain();
        domain.setBindId(null);
        domain.setBasicInfo(new GenColumnBindBasicInfo("bigint", "Long", (byte) 2, (byte) 1));

        StepVerifier.create(genColumnBindDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(2L))
                .verifyComplete();
    }
}
