package com.springddd.application.service.gen;

import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.entity.GenColumnsEntity;
import com.springddd.infrastructure.persistence.factory.QueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenColumnsBatchSaveDomainServiceImplTest {

    @Mock
    private QueryFactory queryFactory;

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @InjectMocks
    private GenColumnsBatchSaveDomainServiceImpl service;

    @Test
    @DisplayName("batchSave 应插入新实体")
    void batchSave_shouldInsertNew() {
        when(queryFactory.getR2dbcEntityTemplate()).thenReturn(r2dbcEntityTemplate);
        when(r2dbcEntityTemplate.insert(any(GenColumnsEntity.class))).thenReturn(Mono.just(new GenColumnsEntity()));

        GenColumnsDomain domain = new GenColumnsDomain();
        domain.setProp(new Prop("id", "id", "bigint", "主键", "Long", "Id"));
        domain.setTable(new Table(true, true, true, (byte) 1, (byte) 2));
        domain.setForm(new Form((byte) 1, true, true));
        domain.setI18n(new I18n("Id", "主键"));
        domain.setExtendInfo(new GenColumnsExtendInfo(1L, (byte) 1));
        domain.setDeleteStatus(false);
        domain.setCreateBy("admin");
        domain.setUpdateBy("admin");
        domain.setVersion(1);

        StepVerifier.create(service.batchSave(List.of(domain)))
                .verifyComplete();

        verify(r2dbcEntityTemplate).insert(any(GenColumnsEntity.class));
    }

    @Test
    @DisplayName("batchSave 应更新已有实体")
    void batchSave_shouldUpdateExisting() {
        when(queryFactory.getR2dbcEntityTemplate()).thenReturn(r2dbcEntityTemplate);
        when(r2dbcEntityTemplate.update(any(GenColumnsEntity.class))).thenReturn(Mono.just(new GenColumnsEntity()));

        GenColumnsDomain domain = new GenColumnsDomain();
        domain.setId(new ColumnsId(1L));
        domain.setInfoId(new InfoId(2L));
        domain.setProp(new Prop("id", "id", "bigint", "主键", "Long", "Id"));
        domain.setTable(new Table(true, true, true, (byte) 1, (byte) 2));
        domain.setForm(new Form((byte) 1, true, true));
        domain.setI18n(new I18n("Id", "主键"));
        domain.setExtendInfo(new GenColumnsExtendInfo(1L, (byte) 1));
        domain.setDeleteStatus(false);
        domain.setCreateBy("admin");
        domain.setUpdateBy("admin");
        domain.setVersion(1);

        StepVerifier.create(service.batchSave(List.of(domain)))
                .verifyComplete();

        verify(r2dbcEntityTemplate).update(any(GenColumnsEntity.class));
    }
}
