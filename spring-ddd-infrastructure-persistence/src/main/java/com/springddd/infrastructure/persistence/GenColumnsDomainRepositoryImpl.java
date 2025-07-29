package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.entity.GenColumnsEntity;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenColumnsDomainRepositoryImpl implements GenColumnsDomainRepository {

    private final GenColumnsRepository genColumnsRepository;

    @Override
    public Mono<GenColumnsDomain> load(GenColumnsId aggregateRootId) {
        return genColumnsRepository.findById(aggregateRootId.value()).map(e -> {
            GenColumnsDomain genColumnsDomain = new GenColumnsDomain();

            genColumnsDomain.setId(new GenColumnsId(e.getId()));
            genColumnsDomain.setInfoId(new GenInfoId(e.getInfoId()));

            GenColumnsBasicInfo basicInfo = new GenColumnsBasicInfo(new PropValueObject(e.getPropValueObject()), new PropColumnKey(e.getPropColumnKey()), new PropColumnName(e.getPropColumnName()), new PropColumnType(e.getPropColumnType()), new PropColumnComment(e.getPropColumnComment()), new PropJavaEntity(e.getPropJavaEntity()));
            genColumnsDomain.setBasicInfo(basicInfo);

            GenColumnsExtendInfo extendInfo = new GenColumnsExtendInfo(e.getPropDictId(), e.getTableVisible(), e.getTableOrder(), e.getTableFilter(), e.getTableFilterComponent(), e.getTableFilterType(), e.getFormComponent(), e.getFormVisible(), e.getFormRequired());
            genColumnsDomain.setExtendInfo(extendInfo);

            genColumnsDomain.setDeleteStatus(e.getDeleteStatus());
            genColumnsDomain.setVersion(e.getVersion());
            genColumnsDomain.setCreateBy(e.getCreateBy());
            genColumnsDomain.setCreateTime(e.getCreateTime());
            genColumnsDomain.setUpdateBy(e.getUpdateBy());
            genColumnsDomain.setUpdateTime(e.getUpdateTime());
            return genColumnsDomain;
        });
    }

    @Override
    public Mono<Long> save(GenColumnsDomain aggregateRoot) {
        GenColumnsEntity entity = new GenColumnsEntity();

        entity.setId(Optional.ofNullable(aggregateRoot.getId()).map(GenColumnsId::value).orElse(null));
        entity.setInfoId(aggregateRoot.getInfoId().value());

        entity.setPropValueObject(aggregateRoot.getBasicInfo().valueObject().value());
        entity.setPropColumnKey(aggregateRoot.getBasicInfo().key().value());
        entity.setPropColumnName(aggregateRoot.getBasicInfo().name().value());
        entity.setPropColumnType(aggregateRoot.getBasicInfo().type().value());
        entity.setPropColumnComment(aggregateRoot.getBasicInfo().comment().value());
        entity.setPropJavaEntity(aggregateRoot.getBasicInfo().entity().value());

        entity.setPropDictId(aggregateRoot.getExtendInfo().propDictId());
        entity.setTableVisible(aggregateRoot.getExtendInfo().tableVisible());
        entity.setTableFilter(aggregateRoot.getExtendInfo().tableFilter());
        entity.setTableOrder(aggregateRoot.getExtendInfo().tableOrder());
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

        return genColumnsRepository.save(entity).map(GenColumnsEntity::getId);
    }
}
