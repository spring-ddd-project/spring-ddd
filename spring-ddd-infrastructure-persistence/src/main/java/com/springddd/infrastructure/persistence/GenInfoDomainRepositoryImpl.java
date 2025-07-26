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

            GenInfoBasicInfo basicInfo = new GenInfoBasicInfo(new TableName(e.getTableName()), new PackageName(e.getPackageName()), new ClassName(e.getClassName()), new RequestName(e.getRequestName()));
            genInfoDomain.setBasicInfo(basicInfo);

            GenInfoExtendInfo extendInfo = new GenInfoExtendInfo(e.getPropValueObject(),
                    e.getPropColumnKey(),
                    e.getPropColumnName(),
                    e.getPropColumnType(),
                    e.getPropColumnComment(),
                    e.getPropJavaEntity(),
                    e.getPropJavaType(),
                    e.getPropDictId(),
                    e.getTableVisible(),
                    e.getTableOrder(),
                    e.getTableFilter(),
                    e.getTableFilterComponent(),
                    e.getTableFilterType(),
                    e.getFormComponent(),
                    e.getFormVisible(),
                    e.getFormRequired()
            );
            genInfoDomain.setExtendInfo(extendInfo);

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
        entity.setRequestName(aggregateRoot.getBasicInfo().requestName().value());

        entity.setPropValueObject(aggregateRoot.getExtendInfo().propValueObject());
        entity.setPropColumnKey(aggregateRoot.getExtendInfo().propColumnKey());
        entity.setPropColumnName(aggregateRoot.getExtendInfo().propColumnName());
        entity.setPropColumnType(aggregateRoot.getExtendInfo().propColumnType());
        entity.setPropColumnComment(aggregateRoot.getExtendInfo().propColumnComment());
        entity.setPropJavaEntity(aggregateRoot.getExtendInfo().propJavaEntity());
        entity.setPropJavaType(aggregateRoot.getExtendInfo().propJavaType());
        entity.setPropDictId(aggregateRoot.getExtendInfo().propDictId());
        entity.setTableVisible(aggregateRoot.getExtendInfo().tableVisible());
        entity.setTableOrder(aggregateRoot.getExtendInfo().tableOrder());
        entity.setTableFilter(aggregateRoot.getExtendInfo().tableFilter());
        entity.setTableFilterComponent(aggregateRoot.getExtendInfo().tableFilterComponent());
        entity.setTableFilterType(aggregateRoot.getExtendInfo().tableFilterType());
        entity.setFormComponent(aggregateRoot.getExtendInfo().formComponent());
        entity.setFormVisible(aggregateRoot.getExtendInfo().formVisible());
        entity.setFormRequired(aggregateRoot.getExtendInfo().formRequired());
        entity.setDeleteStatus(aggregateRoot.getDeleteStatus());
        entity.setCreateBy(aggregateRoot.getCreateBy());
        entity.setCreateTime(aggregateRoot.getCreateTime());
        entity.setUpdateBy(aggregateRoot.getUpdateBy());
        entity.setUpdateTime(aggregateRoot.getUpdateTime());
        entity.setVersion(aggregateRoot.getVersion());

        return genInfoRepository.save(entity).map(GenInfoEntity::getId);
    }
}
