package com.springddd.persistence;

import com.springddd.domain.dict.DictId;
import com.springddd.domain.dict.SysDictDomain;
import com.springddd.domain.dict.SysDictDomainRepository;
import com.springddd.infrastructure.persistence.SysDictDomainRepositoryImpl;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SysDictDomainRepositoryImplTest {

    @Mock
    private SysDictRepository sysDictRepository;

    @InjectMocks
    private SysDictDomainRepositoryImpl sysDictDomainRepository;

    @Test
    void load_shouldReturnDomain_whenEntityExists() {
        SysDictEntity entity = createTestEntity();

        when(sysDictRepository.findById(1L)).thenReturn(Mono.just(entity));

        sysDictDomainRepository.load(new DictId(1L))
                .as(StepVerifier::create)
                .expectNextMatches(domain -> {
                    return domain.getId().value().equals(1L) &&
                            domain.getDictBasicInfo().dictName().equals("Test Dict");
                })
                .verifyComplete();
    }

    @Test
    void load_shouldReturnEmpty_whenEntityNotFound() {
        when(sysDictRepository.findById(999L)).thenReturn(Mono.empty());

        sysDictDomainRepository.load(new DictId(999L))
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenSavingNewDomain() {
        SysDictEntity entity = createTestEntity();
        entity.setId(null);

        when(sysDictRepository.save(any(SysDictEntity.class))).thenReturn(Mono.just(entity));

        SysDictDomain domain = createTestDomain();
        domain.setId(null);

        sysDictDomainRepository.save(domain)
                .as(StepVerifier::create)
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenUpdatingDomain() {
        SysDictEntity entity = createTestEntity();

        when(sysDictRepository.save(any(SysDictEntity.class))).thenReturn(Mono.just(entity));

        SysDictDomain domain = createTestDomain();

        sysDictDomainRepository.save(domain)
                .as(StepVerifier::create)
                .expectNext(1L)
                .verifyComplete();
    }

    private SysDictEntity createTestEntity() {
        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        entity.setDictName("Test Dict");
        entity.setDictCode("test_dict");
        entity.setDictDesc("Test Description");
        entity.setDeleteStatus(false);
        entity.setCreateBy("admin");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("admin");
        entity.setUpdateTime(LocalDateTime.now());
        entity.setVersion(0);
        return entity;
    }

    private SysDictDomain createTestDomain() {
        SysDictDomain domain = new SysDictDomain();
        domain.setId(new DictId(1L));
        domain.setDictBasicInfo(new com.springddd.domain.dict.DictBasicInfo("Test Dict", "test_dict"));
        domain.setDictExtendInfo(new com.springddd.domain.dict.DictExtendInfo("Test Description"));
        domain.setDeleteStatus(false);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setVersion(0);
        return domain;
    }
}
