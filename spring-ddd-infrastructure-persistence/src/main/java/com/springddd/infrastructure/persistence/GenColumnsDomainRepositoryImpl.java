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
    public Mono<GenColumnsDomain> load(ColumnsId aggregateRootId) {
        return genColumnsRepository.findById(aggregateRootId.value()).map(e -> {
            GenColumnsDomain genColumnsDomain = new GenColumnsDomain();

            genColumnsDomain.setId(new ColumnsId(e.getId()));
            genColumnsDomain.setInfoId(new InfoId(e.getInfoId()));

            Prop prop = new Prop(e.getPropColumnKey(), e.getPropColumnName(), e.getPropColumnType(), e.getPropColumnComment(), e.getPropJavaEntity(), e.getPropJavaType());
            genColumnsDomain.setProp(prop);

            Table table = new Table(e.getTableVisible(), e.getTableOrder(), e.getTableFilter(), e.getTableFilterComponent(), e.getTableFilterType());
            genColumnsDomain.setTable(table);

            Form form = new Form(e.getFormComponent(), e.getFormVisible(), e.getFormRequired());
            genColumnsDomain.setForm(form);

            I18n i18n = new I18n(e.getEn(), e.getLocale());
            genColumnsDomain.setI18n(i18n);

            GenColumnsExtendInfo extendInfo = new GenColumnsExtendInfo(e.getPropDictId(), e.getTypescriptType());
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

        entity.setId(Optional.ofNullable(aggregateRoot.getId()).map(ColumnsId::value).orElse(null));
        entity.setInfoId(aggregateRoot.getInfoId().value());

        entity.setPropColumnKey(aggregateRoot.getProp().propColumnKey());
        entity.setPropColumnName(aggregateRoot.getProp().propColumnName());
        entity.setPropColumnType(aggregateRoot.getProp().propColumnType());
        entity.setPropColumnComment(aggregateRoot.getProp().propColumnComment());
        entity.setPropJavaType(aggregateRoot.getProp().propJavaType());
        entity.setPropJavaEntity(aggregateRoot.getProp().propJavaEntity());

        entity.setTableVisible(aggregateRoot.getTable().tableVisible());
        entity.setTableFilter(aggregateRoot.getTable().tableFilter());
        entity.setTableOrder(aggregateRoot.getTable().tableOrder());
        entity.setTableFilterComponent(aggregateRoot.getTable().tableFilterComponent());
        entity.setTableFilterType(aggregateRoot.getTable().tableFilterType());

        entity.setFormComponent(aggregateRoot.getForm().formComponent());
        entity.setFormVisible(aggregateRoot.getForm().formVisible());
        entity.setFormRequired(aggregateRoot.getForm().formRequired());

        entity.setEn(aggregateRoot.getI18n().en());
        entity.setLocale(aggregateRoot.getI18n().locale());

        entity.setPropDictId(aggregateRoot.getExtendInfo().propDictId());
        entity.setTypescriptType(aggregateRoot.getExtendInfo().typescriptType());

        entity.setDeleteStatus(aggregateRoot.getDeleteStatus());
        entity.setCreateBy(aggregateRoot.getCreateBy());
        entity.setCreateTime(aggregateRoot.getCreateTime());
        entity.setUpdateBy(aggregateRoot.getUpdateBy());
        entity.setUpdateTime(aggregateRoot.getUpdateTime());
        entity.setVersion(aggregateRoot.getVersion());

        return genColumnsRepository.save(entity).map(GenColumnsEntity::getId);
    }
}
