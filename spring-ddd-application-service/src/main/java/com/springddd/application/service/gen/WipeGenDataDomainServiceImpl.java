package com.springddd.application.service.gen;

import com.springddd.domain.gen.WipeGenDataDomainService;
import com.springddd.infrastructure.persistence.entity.GenAggregateEntity;
import com.springddd.infrastructure.persistence.entity.GenColumnsEntity;
import com.springddd.infrastructure.persistence.entity.GenProjectInfoEntity;
import com.springddd.infrastructure.persistence.factory.QueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class WipeGenDataDomainServiceImpl implements WipeGenDataDomainService {

    private final QueryFactory queryFactory;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> wipe() {
        return Mono.when(
                queryFactory.getR2dbcEntityTemplate().delete(GenProjectInfoEntity.class).all(),
                queryFactory.getR2dbcEntityTemplate().delete(GenColumnsEntity.class).all(),
                queryFactory.getR2dbcEntityTemplate().delete(GenAggregateEntity.class).all()
        ).then();
    }
}











