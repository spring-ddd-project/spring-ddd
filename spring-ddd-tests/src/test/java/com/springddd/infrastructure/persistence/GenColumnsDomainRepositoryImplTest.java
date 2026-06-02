package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.entity.GenColumnsEntity;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnsRepository;
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
class GenColumnsDomainRepositoryImplTest {

    @Mock
    private GenColumnsRepository genColumnsRepository;

    @InjectMocks
    private GenColumnsDomainRepositoryImpl repository;

    @Test
    void load_shouldReturnDomain_whenEntityExists() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setId(1L);
        entity.setInfoId(1L);
        entity.setPropColumnKey("id");
        entity.setPropColumnName("主键");
        entity.setPropColumnType("bigint");
        entity.setPropColumnComment("主键ID");
        entity.setPropJavaType("Long");
        entity.setPropJavaEntity("Long");
        entity.setTableVisible(true);
        entity.setTableOrder(true);
        entity.setTableFilter(false);
        entity.setTableFilterComponent((byte) 0);
        entity.setTableFilterType((byte) 0);
        entity.setFormComponent((byte) 1);
        entity.setFormVisible(true);
        entity.setFormRequired(true);
        entity.setEn("id");
        entity.setLocale("zh-CN");
        entity.setPropDictId(1L);
        entity.setTypescriptType((byte) 1);
        entity.setDeleteStatus(false);
        entity.setVersion(0);
        entity.setCreateBy("system");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("system");
        entity.setUpdateTime(LocalDateTime.now());

        when(genColumnsRepository.findById(1L)).thenReturn(Mono.just(entity));

        StepVerifier.create(repository.load(new ColumnsId(1L)))
                .assertNext(domain -> {
                    assertEquals(1L, domain.getId().value());
                    assertEquals("id", domain.getProp().propColumnKey());
                    assertEquals("主键", domain.getProp().propColumnName());
                })
                .verifyComplete();
    }

    @Test
    void load_shouldReturnEmpty_whenEntityNotFound() {
        when(genColumnsRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(repository.load(new ColumnsId(1L)))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenSavingNewAggregate() {
        GenColumnsDomain domain = new GenColumnsDomain();
        domain.setId(null);
        domain.setInfoId(new InfoId(1L));
        domain.setProp(new Prop("id", "主键", "bigint", "主键ID", "Long", "Long"));
        domain.setTable(new Table(true, true, false, (byte) 0, (byte) 0));
        domain.setForm(new Form((byte) 1, true, true));
        domain.setI18n(new I18n("id", "zh-CN"));
        domain.setExtendInfo(new GenColumnsExtendInfo(1L, (byte) 1));
        domain.setDeleteStatus(false);
        domain.setVersion(0);

        GenColumnsEntity savedEntity = new GenColumnsEntity();
        savedEntity.setId(1L);

        when(genColumnsRepository.save(any(GenColumnsEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenUpdatingExistingAggregate() {
        GenColumnsDomain domain = new GenColumnsDomain();
        domain.setId(new ColumnsId(1L));
        domain.setInfoId(new InfoId(1L));
        domain.setProp(new Prop("id", "主键", "bigint", "主键ID", "Long", "Long"));
        domain.setTable(new Table(true, true, false, (byte) 0, (byte) 0));
        domain.setForm(new Form((byte) 1, true, true));
        domain.setI18n(new I18n("id", "zh-CN"));
        domain.setExtendInfo(new GenColumnsExtendInfo(1L, (byte) 1));
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        GenColumnsEntity savedEntity = new GenColumnsEntity();
        savedEntity.setId(1L);

        when(genColumnsRepository.save(any(GenColumnsEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }
}
