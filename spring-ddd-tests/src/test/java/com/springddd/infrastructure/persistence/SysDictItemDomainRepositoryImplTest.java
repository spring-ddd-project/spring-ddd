package com.springddd.infrastructure.persistence;

import com.springddd.domain.dict.*;
import com.springddd.infrastructure.persistence.entity.SysDictItemEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysDictItemRepository;
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
class SysDictItemDomainRepositoryImplTest {

    @Mock
    private SysDictItemRepository sysDictItemRepository;

    @InjectMocks
    private SysDictItemDomainRepositoryImpl repository;

    @Test
    void load_shouldReturnDomain_whenEntityExists() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);
        entity.setDictId(1L);
        entity.setItemLabel("启用");
        entity.setItemValue(1);
        entity.setSortOrder(1);
        entity.setItemStatus(true);
        entity.setDeleteStatus(false);
        entity.setVersion(0);
        entity.setCreateBy("system");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("system");
        entity.setUpdateTime(LocalDateTime.now());

        when(sysDictItemRepository.findById(1L)).thenReturn(Mono.just(entity));

        StepVerifier.create(repository.load(new DictItemId(1L)))
                .assertNext(domain -> {
                    assertEquals(1L, domain.getItemId().value());
                    assertEquals(1L, domain.getDictId().value());
                    assertEquals("启用", domain.getItemBasicInfo().itemLabel());
                    assertEquals(1, domain.getItemBasicInfo().itemValue());
                })
                .verifyComplete();
    }

    @Test
    void load_shouldReturnEmpty_whenEntityNotFound() {
        when(sysDictItemRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(repository.load(new DictItemId(1L)))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenSavingNewAggregate() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemId(null);
        domain.setDictId(new DictId(1L));
        domain.setItemBasicInfo(new DictItemBasicInfo("启用", 1));
        domain.setItemExtendInfo(new DictItemExtendInfo(1, true));
        domain.setDeleteStatus(false);
        domain.setVersion(0);

        SysDictItemEntity savedEntity = new SysDictItemEntity();
        savedEntity.setId(1L);

        when(sysDictItemRepository.save(any(SysDictItemEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenUpdatingExistingAggregate() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemId(new DictItemId(1L));
        domain.setDictId(new DictId(1L));
        domain.setItemBasicInfo(new DictItemBasicInfo("启用", 1));
        domain.setItemExtendInfo(new DictItemExtendInfo(1, true));
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        SysDictItemEntity savedEntity = new SysDictItemEntity();
        savedEntity.setId(1L);

        when(sysDictItemRepository.save(any(SysDictItemEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }
}
