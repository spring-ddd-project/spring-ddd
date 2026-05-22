package com.springddd.application.service.permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import com.springddd.infrastructure.persistence.factory.QueryFactory;
import reactor.core.publisher.Mono;

public abstract class BaseQueryService<T> {

    @Autowired
    protected QueryFactory queryFactory;

    @Autowired
    protected DataScopeCriteriaBuilder dataScopeCriteriaBuilder;

    protected Mono<Criteria> applyDataScope(Criteria criteria, String entityCode) {
        return dataScopeCriteriaBuilder.apply(criteria, entityCode);
    }

    protected Mono<Criteria> applyDataScope(Criteria criteria, Class<T> entityClass) {
        org.springframework.data.relational.core.mapping.Table tableAnnotation =
                entityClass.getAnnotation(org.springframework.data.relational.core.mapping.Table.class);
        String entityCode = tableAnnotation != null ? tableAnnotation.value() : entityClass.getSimpleName();
        return applyDataScope(criteria, entityCode);
    }
}
