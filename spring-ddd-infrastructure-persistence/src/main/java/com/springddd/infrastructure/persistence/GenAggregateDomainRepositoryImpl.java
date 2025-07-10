package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.entity.GenAggregateEntity;
import com.springddd.infrastructure.persistence.r2dbc.GenAggregateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenAggregateDomainRepositoryImpl implements GenAggregateDomainRepository {

    private final GenAggregateRepository genAggregateRepository;

    @Override
    public Mono<GenAggregateDomain> load(AggregateId aggregateRootId) {
        return genAggregateRepository.findById(aggregateRootId.value()).map(e -> {
            GenAggregateDomain genAggregateDomain = new GenAggregateDomain();

            genAggregateDomain.setAggregateId(new AggregateId(e.getId()));
            genAggregateDomain.setInfoId(new GenProjectInfoId(e.getInfoId()));

            GenAggregateValueObject valueObject = new GenAggregateValueObject(e.getObjectName(), e.getObjectValue(), e.getObjectType());
            genAggregateDomain.setValueObject(valueObject);

            GenAggregateExtendInfo extendInfo = new GenAggregateExtendInfo(e.getHasCreated());
            genAggregateDomain.setExtendInfo(extendInfo);

            genAggregateDomain.setCreateBy(e.getCreateBy());
            genAggregateDomain.setCreateTime(e.getCreateTime());
            genAggregateDomain.setUpdateBy(e.getUpdateBy());
            genAggregateDomain.setUpdateTime(e.getUpdateTime());
            genAggregateDomain.setVersion(e.getVersion());

            return genAggregateDomain;
        });
    }

    @Override
    public Mono<Long> save(GenAggregateDomain aggregateRoot) {
        GenAggregateEntity entity = new GenAggregateEntity();

        entity.setId(Optional.ofNullable(aggregateRoot.getAggregateId()).map(AggregateId::value).orElse(null));
        entity.setInfoId(aggregateRoot.getInfoId().value());

        entity.setObjectName(aggregateRoot.getValueObject().objectName());
        entity.setObjectValue(aggregateRoot.getValueObject().objectValue());
        entity.setObjectType(aggregateRoot.getValueObject().objectType());

        entity.setHasCreated(aggregateRoot.getExtendInfo().hasCreated());

        entity.setCreateBy(aggregateRoot.getCreateBy());
        entity.setCreateTime(aggregateRoot.getCreateTime());
        entity.setUpdateBy(aggregateRoot.getUpdateBy());
        entity.setUpdateTime(aggregateRoot.getUpdateTime());
        entity.setVersion(aggregateRoot.getVersion());

        return genAggregateRepository.save(entity).map(GenAggregateEntity::getId);
    }
}
