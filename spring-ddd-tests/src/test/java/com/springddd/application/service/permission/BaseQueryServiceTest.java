package com.springddd.application.service.permission;

import com.springddd.infrastructure.persistence.factory.QueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.query.Criteria;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseQueryServiceTest {

    @Mock
    private QueryFactory queryFactory;

    @Mock
    private DataScopeCriteriaBuilder dataScopeCriteriaBuilder;

    @InjectMocks
    private TestQueryService service;

    @Test
    @DisplayName("applyDataScope 应调用 DataScopeCriteriaBuilder")
    void applyDataScope_shouldCallBuilder() {
        Criteria criteria = Criteria.where("id").is(1);
        Criteria scoped = Criteria.where("id").is(1).and("deptId").is(1);

        when(dataScopeCriteriaBuilder.apply(criteria, "test_entity")).thenReturn(Mono.just(scoped));

        StepVerifier.create(service.applyDataScopePublic(criteria, "test_entity"))
                .assertNext(result -> assertThat(result.toString()).contains("deptId"))
                .verifyComplete();
    }

    @Test
    @DisplayName("applyDataScope 通过 Class 应提取表名")
    void applyDataScope_byClass_shouldExtractTableName() {
        Criteria criteria = Criteria.where("id").is(1);
        Criteria scoped = Criteria.where("id").is(1).and("deptId").is(1);

        when(dataScopeCriteriaBuilder.apply(any(Criteria.class), eq("test_entity"))).thenReturn(Mono.just(scoped));

        StepVerifier.create(service.applyDataScopePublic(criteria, TestEntity.class))
                .assertNext(result -> assertThat(result.toString()).contains("deptId"))
                .verifyComplete();
    }

    @Table("test_entity")
    static class TestEntity {}

    static class TestQueryService extends BaseQueryService<TestEntity> {
        public Mono<Criteria> applyDataScopePublic(Criteria criteria, String entityCode) {
            return applyDataScope(criteria, entityCode);
        }
        public Mono<Criteria> applyDataScopePublic(Criteria criteria, Class<TestEntity> entityClass) {
            return applyDataScope(criteria, entityClass);
        }
    }
}
