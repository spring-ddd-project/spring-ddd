package com.springddd.infrastructure.persistence;

import com.springddd.domain.dict.*;
import com.springddd.infrastructure.persistence.entity.SysDictEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysDictRepository;
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
class SysDictDomainRepositoryImplTest {

    @Mock
    private SysDictRepository sysDictRepository;

    @InjectMocks
    private SysDictDomainRepositoryImpl repository;

    @Test
    void load_shouldReturnDomain_whenEntityExists() {
        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        entity.setDictName("状态");
        entity.setDictCode("status");
        entity.setSortOrder(1);
        entity.setDictStatus(true);
        entity.setDeleteStatus(false);
        entity.setVersion(0);
        entity.setCreateBy("system");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("system");
        entity.setUpdateTime(LocalDateTime.now());

        when(sysDictRepository.findById(1L)).thenReturn(Mono.just(entity));

        StepVerifier.create(repository.load(new DictId(1L)))
                .assertNext(domain -> {
                    assertEquals(1L, domain.getDictId().value());
                    assertEquals("状态", domain.getDictBasicInfo().dictName());
                    assertEquals("status", domain.getDictBasicInfo().dictCode());
                })
                .verifyComplete();
    }

    @Test
    void load_shouldReturnEmpty_whenEntityNotFound() {
        when(sysDictRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(repository.load(new DictId(1L)))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenSavingNewAggregate() {
        SysDictDomain domain = new SysDictDomain();
        domain.setDictId(null);
        domain.setDictBasicInfo(new DictBasicInfo("状态", "status"));
        domain.setDictExtendInfo(new DictExtendInfo(1, true));
        domain.setDeleteStatus(false);
        domain.setVersion(0);

        SysDictEntity savedEntity = new SysDictEntity();
        savedEntity.setId(1L);

        when(sysDictRepository.save(any(SysDictEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenUpdatingExistingAggregate() {
        SysDictDomain domain = new SysDictDomain();
        domain.setDictId(new DictId(1L));
        domain.setDictBasicInfo(new DictBasicInfo("状态", "status"));
        domain.setDictExtendInfo(new DictExtendInfo(1, true));
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        SysDictEntity savedEntity = new SysDictEntity();
        savedEntity.setId(1L);

        when(sysDictRepository.save(any(SysDictEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }
}
