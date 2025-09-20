package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.entity.GenColumnsEntity;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnsRepository;
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
@DisplayName("GenColumnsDomainRepositoryImpl Tests")
class GenColumnsDomainRepositoryImplTest {

    @Mock
    private GenColumnsRepository genColumnsRepository;

    @InjectMocks
    private GenColumnsDomainRepositoryImpl genColumnsDomainRepository;

    private GenColumnsEntity testEntity;

    @BeforeEach
    void setUp() {
        testEntity = new GenColumnsEntity();
        testEntity.setId(1L);
        testEntity.setInfoId(10L);
        testEntity.setPropColumnKey("id");
        testEntity.setPropColumnName("id");
        testEntity.setPropColumnType("bigint");
        testEntity.setPropColumnComment("Primary key");
        testEntity.setPropJavaEntity("Long");
        testEntity.setPropJavaType("Long");
        testEntity.setPropDictId(100L);
        testEntity.setTableVisible(true);
        testEntity.setTableOrder(true);
        testEntity.setTableFilter(false);
        testEntity.setTableFilterComponent((byte) 1);
        testEntity.setTableFilterType((byte) 0);
        testEntity.setTypescriptType((byte) 1);
        testEntity.setFormComponent((byte) 1);
        testEntity.setFormVisible(true);
        testEntity.setFormRequired(false);
        testEntity.setEn("en_US");
        testEntity.setLocale("zh_CN");
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
        when(genColumnsRepository.findById(1L)).thenReturn(Mono.just(testEntity));

        StepVerifier.create(genColumnsDomainRepository.load(new ColumnsId(1L)))
                .assertNext(domain -> {
                    assertThat(domain.getId().value()).isEqualTo(1L);
                    assertThat(domain.getInfoId().value()).isEqualTo(10L);
                    assertThat(domain.getProp().propColumnKey()).isEqualTo("id");
                    assertThat(domain.getProp().propColumnName()).isEqualTo("id");
                    assertThat(domain.getProp().propColumnType()).isEqualTo("bigint");
                    assertThat(domain.getProp().propColumnComment()).isEqualTo("Primary key");
                    assertThat(domain.getProp().propJavaEntity()).isEqualTo("Long");
                    assertThat(domain.getProp().propJavaType()).isEqualTo("Long");
                    assertThat(domain.getTable().tableVisible()).isTrue();
                    assertThat(domain.getTable().tableOrder()).isTrue();
                    assertThat(domain.getTable().tableFilter()).isFalse();
                    assertThat(domain.getTable().tableFilterComponent()).isEqualTo((byte) 1);
                    assertThat(domain.getTable().tableFilterType()).isEqualTo((byte) 0);
                    assertThat(domain.getForm().formComponent()).isEqualTo((byte) 1);
                    assertThat(domain.getForm().formVisible()).isTrue();
                    assertThat(domain.getForm().formRequired()).isFalse();
                    assertThat(domain.getI18n().en()).isEqualTo("en_US");
                    assertThat(domain.getI18n().locale()).isEqualTo("zh_CN");
                    assertThat(domain.getExtendInfo().propDictId()).isEqualTo(100L);
                    assertThat(domain.getExtendInfo().typescriptType()).isEqualTo((byte) 1);
                    assertThat(domain.getDeleteStatus()).isFalse();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("load() should return empty when entity not found")
    void load_WhenEntityNotFound_ReturnsEmpty() {
        when(genColumnsRepository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(genColumnsDomainRepository.load(new ColumnsId(999L)))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() should persist domain and return id")
    void save_WhenValidDomain_ReturnsId() {
        when(genColumnsRepository.save(any(GenColumnsEntity.class))).thenReturn(Mono.just(testEntity));

        GenColumnsDomain domain = new GenColumnsDomain();
        domain.setId(new ColumnsId(1L));
        domain.setInfoId(new InfoId(10L));
        domain.setProp(new Prop("id", "id", "bigint", "Primary key", "Long", "Long"));
        domain.setTable(new Table(true, true, false, (byte) 1, (byte) 0));
        domain.setForm(new Form((byte) 1, true, false));
        domain.setI18n(new I18n("en_US", "zh_CN"));
        domain.setExtendInfo(new GenColumnsExtendInfo(100L, (byte) 1));
        domain.setDeleteStatus(false);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setVersion(1);

        StepVerifier.create(genColumnsDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() with null id should create new entity")
    void save_WithNullId_CreatesNewEntity() {
        GenColumnsEntity newEntity = new GenColumnsEntity();
        newEntity.setId(2L);
        when(genColumnsRepository.save(any(GenColumnsEntity.class))).thenReturn(Mono.just(newEntity));

        GenColumnsDomain domain = new GenColumnsDomain();
        domain.setId(null);
        domain.setInfoId(new InfoId(10L));
        domain.setProp(new Prop("name", "name", "varchar", "Name", "String", "String"));
        domain.setTable(new Table(false, false, true, (byte) 0, (byte) 1));
        domain.setForm(new Form((byte) 2, false, true));
        domain.setI18n(new I18n("en_GB", "en_US"));
        domain.setExtendInfo(new GenColumnsExtendInfo(null, (byte) 2));

        StepVerifier.create(genColumnsDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(2L))
                .verifyComplete();
    }
}
