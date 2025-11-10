package com.springddd.application.service.gen;

import com.springddd.domain.gen.WipeGenProjectInfoByIdsDomainService;
import com.springddd.infrastructure.persistence.entity.GenProjectInfoEntity;
import com.springddd.infrastructure.persistence.factory.QueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeGenProjectInfoByIdsDomainServiceImpl implements WipeGenProjectInfoByIdsDomainService {

    private final QueryFactory queryFactory;

    @Override
    public Mono<Void> wipeByIds(List<Long> ids) {
        return queryFactory.getR2dbcEntityTemplate().delete(GenProjectInfoEntity.class)
                .matching(Query.query(Criteria.where("id").in(ids)))
                .all()
                .then();
    }
}














