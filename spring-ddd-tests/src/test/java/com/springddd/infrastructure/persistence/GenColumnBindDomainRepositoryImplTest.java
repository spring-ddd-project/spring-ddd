package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.entity.GenColumnBindEntity;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnBindRepository;
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
class GenColumnBindDomainRepositoryImplTest {

    @Mock
    private GenColumnBindRepository genColumnBindRepository;

    @InjectMocks
    private GenColumnBindDomainRepositoryImpl repository;

    @Test
    void load_shouldReturnDomain_whenEntityExists() {
        GenColumnBindEntity entity = new GenColumnBindEntity();
        entity.setId(1L);
        entity.setColumnType("varchar");
        entity.setEntityType("String");
        entity.setComponentType((byte) 1);
        entity.setTypescriptType((byte) 1);
        entity.setDeleteStatus(false);
        entity.setVersion(0);
        entity.setCreateBy("system");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("system");
        entity.setUpdateTime(LocalDateTime.now());

        when(genColumnBindRepository.findById(1L)).thenReturn(Mono.just(entity));

        StepVerifier.create(repository.load(new ColumnBindId(1L)))
                .assertNext(domain -> {
                    assertEquals(1L, domain.getBindId().value());
                    assertEquals("varchar", domain.getBasicInfo().columnType());
                    assertEquals("String", domain.getBasicInfo().entityType());
                })
                .verifyComplete();
    }

    @Test
    void load_shouldReturnEmpty_whenEntityNotFound() {
        when(genColumnBindRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(repository.load(new ColumnBindId(1L)))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenSavingNewAggregate() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        domain.setBindId(null);
        domain.setBasicInfo(new GenColumnBindBasicInfo("varchar", "String", (byte) 1, (byte) 1));
        domain.setDeleteStatus(false);
        domain.setVersion(0);

        GenColumnBindEntity savedEntity = new GenColumnBindEntity();
        savedEntity.setId(1L);

        when(genColumnBindRepository.save(any(GenColumnBindEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenUpdatingExistingAggregate() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        domain.setBindId(new ColumnBindId(1L));
        domain.setBasicInfo(new GenColumnBindBasicInfo("varchar", "String", (byte) 1, (byte) 1));
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        GenColumnBindEntity savedEntity = new GenColumnBindEntity();
        savedEntity.setId(1L);

        when(genColumnBindRepository.save(any(GenColumnBindEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }
}
