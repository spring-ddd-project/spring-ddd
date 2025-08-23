package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.entity.GenColumnBindEntity;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnBindRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenColumnBindDomainRepositoryImpl implements GenColumnBindDomainRepository {

    private final GenColumnBindRepository genColumnBindRepository;

    @Override
    public Mono<GenColumnBindDomain> load(ColumnBindId aggregateRootId) {
        return genColumnBindRepository.findById(aggregateRootId.value()).map(e -> {
            GenColumnBindDomain genColumnBindDomain = new GenColumnBindDomain();
            genColumnBindDomain.setBindId(new ColumnBindId(e.getId()));

            GenColumnBindBasicInfo basicInfo = new GenColumnBindBasicInfo(new ColumnName(e.getColumnName()), new EntityName(e.getEntityName()), new ComponentName(e.getComponentName()));
            genColumnBindDomain.setBasicInfo(basicInfo);

            genColumnBindDomain.setDeleteStatus(e.getDeleteStatus());
            genColumnBindDomain.setCreateBy(e.getCreateBy());
            genColumnBindDomain.setCreateTime(e.getCreateTime());
            genColumnBindDomain.setUpdateBy(e.getUpdateBy());
            genColumnBindDomain.setUpdateTime(e.getUpdateTime());
            genColumnBindDomain.setVersion(e.getVersion());

            return genColumnBindDomain;
        });
    }

    @Override
    public Mono<Long> save(GenColumnBindDomain aggregateRoot) {
        GenColumnBindEntity entity = new GenColumnBindEntity();
        entity.setId(Optional.ofNullable(aggregateRoot.getBindId()).map(ColumnBindId::value).orElse(null));

        GenColumnBindBasicInfo basicInfo = aggregateRoot.getBasicInfo();
        entity.setColumnName(basicInfo.columnName().value());
        entity.setEntityName(basicInfo.entityName().value());
        entity.setComponentName(basicInfo.componentName().value());

        entity.setDeleteStatus(aggregateRoot.getDeleteStatus());
        entity.setCreateBy(aggregateRoot.getCreateBy());
        entity.setCreateTime(aggregateRoot.getCreateTime());
        entity.setUpdateBy(aggregateRoot.getUpdateBy());
        entity.setUpdateTime(aggregateRoot.getUpdateTime());
        entity.setVersion(aggregateRoot.getVersion());

        return genColumnBindRepository.save(entity).map(GenColumnBindEntity::getId);
    }
}
