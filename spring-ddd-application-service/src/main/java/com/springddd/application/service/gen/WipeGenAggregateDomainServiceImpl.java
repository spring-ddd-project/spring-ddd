package com.springddd.application.service.gen;

import com.springddd.domain.gen.WipeGenAggregateDomainService;
import com.springddd.infrastructure.persistence.entity.GenAggregateEntity;
import com.springddd.infrastructure.persistence.factory.QueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeGenAggregateDomainServiceImpl implements WipeGenAggregateDomainService {

    private final QueryFactory queryFactory;

    @Override
    public Mono<Void> wipe(List<Long> ids) {
        return queryFactory.getR2dbcEntityTemplate().delete(GenAggregateEntity.class)
                .matching(Query.query(Criteria.where("id").in(ids)))
                .all()
                .then();
    }
}
































