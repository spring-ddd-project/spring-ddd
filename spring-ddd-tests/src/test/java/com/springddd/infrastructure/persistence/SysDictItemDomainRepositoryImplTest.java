package com.springddd.infrastructure.persistence;

import com.springddd.domain.dict.*;
import com.springddd.infrastructure.persistence.entity.SysDictItemEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysDictItemRepository;
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
@DisplayName("SysDictItemDomainRepositoryImpl Tests")
class SysDictItemDomainRepositoryImplTest {

    @Mock
    private SysDictItemRepository sysDictItemRepository;

    @InjectMocks
    private SysDictItemDomainRepositoryImpl sysDictItemDomainRepository;

    private SysDictItemEntity testEntity;

    @BeforeEach
    void setUp() {
        testEntity = new SysDictItemEntity();
        testEntity.setId(1L);
        testEntity.setDictId(10L);
        testEntity.setItemLabel("Item Label");
        testEntity.setItemValue(1);
        testEntity.setSortOrder(1);
        testEntity.setItemStatus(true);
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
        when(sysDictItemRepository.findById(1L)).thenReturn(Mono.just(testEntity));

        StepVerifier.create(sysDictItemDomainRepository.load(new DictItemId(1L)))
                .assertNext(domain -> {
                    org.assertj.core.api.Assertions.assertThat(domain.getItemId().value()).isEqualTo(1L);
                    org.assertj.core.api.Assertions.assertThat(domain.getDictId().value()).isEqualTo(10L);
                    org.assertj.core.api.Assertions.assertThat(domain.getItemBasicInfo().itemLabel()).isEqualTo("Item Label");
                    org.assertj.core.api.Assertions.assertThat(domain.getItemBasicInfo().itemValue()).isEqualTo(1);
                    org.assertj.core.api.Assertions.assertThat(domain.getItemExtendInfo().sortOrder()).isEqualTo(1);
                    org.assertj.core.api.Assertions.assertThat(domain.getItemExtendInfo().itemStatus()).isTrue();
                    org.assertj.core.api.Assertions.assertThat(domain.getDeleteStatus()).isFalse();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("load() should return empty when entity not found")
    void load_WhenEntityNotFound_ReturnsEmpty() {
        when(sysDictItemRepository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(sysDictItemDomainRepository.load(new DictItemId(999L)))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() should persist domain and return id")
    void save_WhenValidDomain_ReturnsId() {
        when(sysDictItemRepository.save(any(SysDictItemEntity.class))).thenReturn(Mono.just(testEntity));

        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemId(new DictItemId(1L));
        domain.setDictId(new DictId(10L));
        DictItemBasicInfo basicInfo = new DictItemBasicInfo("Item Label", 1);
        domain.setItemBasicInfo(basicInfo);
        DictItemExtendInfo extendInfo = new DictItemExtendInfo(1, true);
        domain.setItemExtendInfo(extendInfo);
        domain.setDeleteStatus(false);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setVersion(1);

        StepVerifier.create(sysDictItemDomainRepository.save(domain))
                .assertNext(id -> org.assertj.core.api.Assertions.assertThat(id).isEqualTo(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() with null item id should create new entity")
    void save_WithNullId_CreatesNewEntity() {
        SysDictItemEntity newEntity = new SysDictItemEntity();
        newEntity.setId(2L);
        newEntity.setDictId(10L);

        when(sysDictItemRepository.save(any(SysDictItemEntity.class))).thenReturn(Mono.just(newEntity));

        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemId(null);
        domain.setDictId(new DictId(10L));
        DictItemBasicInfo basicInfo = new DictItemBasicInfo("New Item", 2);
        domain.setItemBasicInfo(basicInfo);
        DictItemExtendInfo extendInfo = new DictItemExtendInfo(2, true);
        domain.setItemExtendInfo(extendInfo);

        StepVerifier.create(sysDictItemDomainRepository.save(domain))
                .assertNext(id -> org.assertj.core.api.Assertions.assertThat(id).isEqualTo(2L))
                .verifyComplete();
    }
}
