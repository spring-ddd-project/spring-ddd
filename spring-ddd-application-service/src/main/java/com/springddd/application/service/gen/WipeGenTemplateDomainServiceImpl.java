package com.springddd.application.service.gen;

import com.springddd.domain.gen.WipeGenTemplateDomainService;
import com.springddd.infrastructure.persistence.entity.GenTemplateEntity;
import com.springddd.infrastructure.persistence.factory.QueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeGenTemplateDomainServiceImpl implements WipeGenTemplateDomainService {

    private final QueryFactory queryFactory;

    @Override
    public Mono<Void> wipeByIds(List<Long> ids) {
        return queryFactory.getR2dbcEntityTemplate().delete(GenTemplateEntity.class)
                .matching(Query.query(Criteria.where("id").in(ids)))
                .all()
                .then();
    }
}


























































