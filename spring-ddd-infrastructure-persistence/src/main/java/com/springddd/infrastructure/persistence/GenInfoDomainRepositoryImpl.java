package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.entity.GenInfoEntity;
import com.springddd.infrastructure.persistence.r2dbc.GenInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenInfoDomainRepositoryImpl implements GenInfoDomainRepository {

    private final GenInfoRepository genInfoRepository;

    @Override
    public Mono<GenInfoDomain> load(GenInfoId aggregateRootId) {
        return genInfoRepository.findById(aggregateRootId.value()).map(e -> {
            GenInfoDomain genInfoDomain = new GenInfoDomain();
            genInfoDomain.setId(new GenInfoId(e.getId()));

            GenInfoBasicInfo basicInfo = new GenInfoBasicInfo(new TableName(e.getTableName()), new PackageName(e.getPackageName()), new ClassName(e.getClassName()));
            genInfoDomain.setBasicInfo(basicInfo);

            genInfoDomain.setId(new GenInfoId(e.getId()));

            genInfoDomain.setDeleteStatus(e.getDeleteStatus());
            genInfoDomain.setCreateBy(e.getCreateBy());
            genInfoDomain.setCreateTime(e.getCreateTime());
            genInfoDomain.setUpdateBy(e.getUpdateBy());
            genInfoDomain.setUpdateTime(e.getUpdateTime());
            genInfoDomain.setVersion(e.getVersion());

            return genInfoDomain;
        });
    }

    @Override
    public Mono<Long> save(GenInfoDomain aggregateRoot) {
        GenInfoEntity entity = new GenInfoEntity();

        entity.setId(Optional.ofNullable(aggregateRoot.getId()).map(GenInfoId::value).orElse(null));

        entity.setTableName(aggregateRoot.getBasicInfo().tableName().value());
        entity.setPackageName(aggregateRoot.getBasicInfo().packageName().value());
        entity.setClassName(aggregateRoot.getBasicInfo().className().value());

        entity.setRequestName(aggregateRoot.getExtendInfo().requestName());

        entity.setDeleteStatus(aggregateRoot.getDeleteStatus());
        entity.setCreateBy(aggregateRoot.getCreateBy());
        entity.setCreateTime(aggregateRoot.getCreateTime());
        entity.setUpdateBy(aggregateRoot.getUpdateBy());
        entity.setUpdateTime(aggregateRoot.getUpdateTime());
        entity.setVersion(aggregateRoot.getVersion());

        return genInfoRepository.save(entity).map(GenInfoEntity::getId);
    }
}
