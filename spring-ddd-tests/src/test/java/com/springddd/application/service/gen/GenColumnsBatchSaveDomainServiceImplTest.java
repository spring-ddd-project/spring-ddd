package com.springddd.application.service.gen;

import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.entity.GenColumnsEntity;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenColumnsBatchSaveDomainServiceImplTest {

    @Mock
    private GenColumnsRepository genColumnsRepository;

    private GenColumnsBatchSaveDomainServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new GenColumnsBatchSaveDomainServiceImpl(genColumnsRepository);
    }

    @Test
    void batchSave_shouldSaveAllEntities() {
        GenColumnsDomain domain = new GenColumnsDomain();
        domain.setId(new ColumnsId(1L));
        domain.setInfoId(new InfoId(1L));
        domain.setProp(new Prop("id", "ID", "bigint", "Primary Key", "Long", "Long"));
        domain.setTable(new Table(true, true, true, (byte) 1, (byte) 1));
        domain.setForm(new Form((byte) 1, true, true));
        domain.setI18n(new I18n("Id", "Primary Key"));
        domain.setExtendInfo(new GenColumnsExtendInfo(1L, (byte) 1));
        domain.setDeleteStatus(false);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setVersion(1);

        when(genColumnsRepository.saveAll(anyList())).thenReturn(Flux.just(new GenColumnsEntity()));

        StepVerifier.create(service.batchSave(List.of(domain)))
                .verifyComplete();
    }

    @Test
    void batchSave_shouldHandleNullIds() {
        GenColumnsDomain domain = new GenColumnsDomain();
        domain.setId(null);
        domain.setInfoId(null);
        domain.setProp(new Prop("name", "Name", "varchar", "Name", "String", "String"));
        domain.setTable(new Table(true, false, false, (byte) 1, (byte) 1));
        domain.setForm(new Form((byte) 2, true, false));
        domain.setI18n(new I18n("Name", null));
        domain.setExtendInfo(new GenColumnsExtendInfo(null, (byte) 2));
        domain.setDeleteStatus(false);

        when(genColumnsRepository.saveAll(anyList())).thenReturn(Flux.just(new GenColumnsEntity()));

        StepVerifier.create(service.batchSave(List.of(domain)))
                .verifyComplete();
    }
}
