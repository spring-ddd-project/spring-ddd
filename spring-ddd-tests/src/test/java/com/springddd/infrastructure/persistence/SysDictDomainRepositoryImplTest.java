package com.springddd.infrastructure.persistence;

import com.springddd.domain.dict.*;
import com.springddd.infrastructure.persistence.entity.SysDictEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysDictRepository;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SysDictDomainRepositoryImpl Tests")
class SysDictDomainRepositoryImplTest {

    @Mock
    private SysDictRepository sysDictRepository;

    @InjectMocks
    private SysDictDomainRepositoryImpl sysDictDomainRepository;

    private SysDictEntity testEntity;

    @BeforeEach
    void setUp() {
        testEntity = new SysDictEntity();
        testEntity.setId(1L);
        testEntity.setDictName("Test Dict");
        testEntity.setDictCode("test_dict");
        testEntity.setSortOrder(1);
        testEntity.setDictStatus(true);
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
        when(sysDictRepository.findById(1L)).thenReturn(Mono.just(testEntity));

        StepVerifier.create(sysDictDomainRepository.load(new DictId(1L)))
                .assertNext(domain -> {
                    org.assertj.core.api.Assertions.assertThat(domain.getDictId().value()).isEqualTo(1L);
                    org.assertj.core.api.Assertions.assertThat(domain.getDictBasicInfo().dictName()).isEqualTo("Test Dict");
                    org.assertj.core.api.Assertions.assertThat(domain.getDictBasicInfo().dictCode()).isEqualTo("test_dict");
                    org.assertj.core.api.Assertions.assertThat(domain.getDictExtendInfo().sortOrder()).isEqualTo(1);
                    org.assertj.core.api.Assertions.assertThat(domain.getDictExtendInfo().dictStatus()).isTrue();
                    org.assertj.core.api.Assertions.assertThat(domain.getDeleteStatus()).isFalse();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("load() should return empty when entity not found")
    void load_WhenEntityNotFound_ReturnsEmpty() {
        when(sysDictRepository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(sysDictDomainRepository.load(new DictId(999L)))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() should persist domain and return id")
    void save_WhenValidDomain_ReturnsId() {
        when(sysDictRepository.save(any(SysDictEntity.class))).thenReturn(Mono.just(testEntity));

        SysDictDomain domain = new SysDictDomain();
        domain.setDictId(new DictId(1L));
        DictBasicInfo basicInfo = new DictBasicInfo("Test Dict", "test_dict");
        domain.setDictBasicInfo(basicInfo);
        DictExtendInfo extendInfo = new DictExtendInfo(1, true);
        domain.setDictExtendInfo(extendInfo);
        domain.setDeleteStatus(false);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setVersion(1);

        StepVerifier.create(sysDictDomainRepository.save(domain))
                .assertNext(id -> org.assertj.core.api.Assertions.assertThat(id).isEqualTo(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() with null id should create new entity")
    void save_WithNullId_CreatesNewEntity() {
        SysDictEntity newEntity = new SysDictEntity();
        newEntity.setId(2L);
        newEntity.setDictName("New Dict");

        when(sysDictRepository.save(any(SysDictEntity.class))).thenReturn(Mono.just(newEntity));

        SysDictDomain domain = new SysDictDomain();
        domain.setDictId(null);
        DictBasicInfo basicInfo = new DictBasicInfo("New Dict", "new_dict");
        domain.setDictBasicInfo(basicInfo);
        DictExtendInfo extendInfo = new DictExtendInfo(1, true);
        domain.setDictExtendInfo(extendInfo);

        StepVerifier.create(sysDictDomainRepository.save(domain))
                .assertNext(id -> org.assertj.core.api.Assertions.assertThat(id).isEqualTo(2L))
                .verifyComplete();
    }
}
