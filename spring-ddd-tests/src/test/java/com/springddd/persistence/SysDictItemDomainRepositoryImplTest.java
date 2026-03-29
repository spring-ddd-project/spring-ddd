package com.springddd.persistence;

import com.springddd.domain.dict.DictItemId;
import com.springddd.domain.dict.SysDictItemDomain;
import com.springddd.infrastructure.persistence.SysDictItemDomainRepositoryImpl;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SysDictItemDomainRepositoryImplTest {

    @Mock
    private SysDictItemRepository sysDictItemRepository;

    @InjectMocks
    private SysDictItemDomainRepositoryImpl sysDictItemDomainRepository;

    @Test
    void load_shouldReturnDomain_whenEntityExists() {
        SysDictItemEntity entity = createTestEntity();

        when(sysDictItemRepository.findById(1L)).thenReturn(Mono.just(entity));

        sysDictItemDomainRepository.load(new DictItemId(1L))
                .as(StepVerifier::create)
                .expectNextMatches(domain -> {
                    return domain.getId().value().equals(1L) &&
                            domain.getDictItemBasicInfo().itemText().equals("Test Item");
                })
                .verifyComplete();
    }

    @Test
    void load_shouldReturnEmpty_whenEntityNotFound() {
        when(sysDictItemRepository.findById(999L)).thenReturn(Mono.empty());

        sysDictItemDomainRepository.load(new DictItemId(999L))
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenSavingNewDomain() {
        SysDictItemEntity entity = createTestEntity();
        entity.setId(null);

        when(sysDictItemRepository.save(any(SysDictItemEntity.class))).thenReturn(Mono.just(entity));

        SysDictItemDomain domain = createTestDomain();
        domain.setId(null);

        sysDictItemDomainRepository.save(domain)
                .as(StepVerifier::create)
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenUpdatingDomain() {
        SysDictItemEntity entity = createTestEntity();

        when(sysDictItemRepository.save(any(SysDictItemEntity.class))).thenReturn(Mono.just(entity));

        SysDictItemDomain domain = createTestDomain();

        sysDictItemDomainRepository.save(domain)
                .as(StepVerifier::create)
                .expectNext(1L)
                .verifyComplete();
    }

    private SysDictItemEntity createTestEntity() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);
        entity.setDictCode("test_dict");
        entity.setItemText("Test Item");
        entity.setItemValue(1);
        entity.setSortOrder(1);
        entity.setDeleteStatus(false);
        entity.setCreateBy("admin");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("admin");
        entity.setUpdateTime(LocalDateTime.now());
        entity.setVersion(0);
        return entity;
    }

    private SysDictItemDomain createTestDomain() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setId(new DictItemId(1L));
        domain.setDictItemBasicInfo(new com.springddd.domain.dict.DictItemBasicInfo("test_dict", "Test Item", 1));
        domain.setDictItemExtendInfo(new com.springddd.domain.dict.DictItemExtendInfo(1));
        domain.setDeleteStatus(false);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setVersion(0);
        return domain;
    }
}
