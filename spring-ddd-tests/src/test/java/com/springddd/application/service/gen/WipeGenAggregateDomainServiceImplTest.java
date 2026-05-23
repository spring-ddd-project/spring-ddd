package com.springddd.application.service.gen;

import com.springddd.infrastructure.persistence.entity.GenAggregateEntity;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeGenAggregateDomainServiceImplTest {

    @Mock
    private QueryFactory queryFactory;

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private ReactiveDeleteOperation.ReactiveDelete deleteOp;

    @InjectMocks
    private WipeGenAggregateDomainServiceImpl service;

    @Test
    @DisplayName("wipe 应删除指定聚合")
    void wipe_shouldDelete() {
        when(queryFactory.getR2dbcEntityTemplate()).thenReturn(r2dbcEntityTemplate);
        when(r2dbcEntityTemplate.delete(GenAggregateEntity.class)).thenReturn(deleteOp);
        when(deleteOp.matching(any(Query.class))).thenReturn(deleteOp);
        when(deleteOp.all()).thenReturn(Mono.just(1L));

        StepVerifier.create(service.wipe(List.of(1L)))
                .verifyComplete();
    }
}
