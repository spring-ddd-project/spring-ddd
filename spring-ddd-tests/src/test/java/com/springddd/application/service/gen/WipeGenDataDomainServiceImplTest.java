package com.springddd.application.service.gen;

import com.springddd.infrastructure.persistence.entity.GenAggregateEntity;
import com.springddd.infrastructure.persistence.entity.GenColumnsEntity;
import com.springddd.infrastructure.persistence.entity.GenProjectInfoEntity;
import com.springddd.infrastructure.persistence.factory.QueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.core.ReactiveDeleteOperation;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeGenDataDomainServiceImplTest {

    @Mock
    private QueryFactory queryFactory;

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private ReactiveDeleteOperation.ReactiveDelete deleteOp;

    @InjectMocks
    private WipeGenDataDomainServiceImpl service;

    @Test
    @DisplayName("wipe 应删除所有生成相关数据")
    void wipe_shouldDeleteAllGenData() {
        when(queryFactory.getR2dbcEntityTemplate()).thenReturn(r2dbcEntityTemplate);
        when(r2dbcEntityTemplate.delete(any(Class.class))).thenReturn(deleteOp);
        when(deleteOp.all()).thenReturn(Mono.just(1L));

        StepVerifier.create(service.wipe())
                .verifyComplete();

        verify(r2dbcEntityTemplate).delete(GenProjectInfoEntity.class);
        verify(r2dbcEntityTemplate).delete(GenColumnsEntity.class);
        verify(r2dbcEntityTemplate).delete(GenAggregateEntity.class);
    }
}
