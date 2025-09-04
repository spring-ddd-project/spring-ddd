package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenColumnsBatchSaveDomainService;
import com.springddd.domain.gen.GenColumnsDomain;
import com.springddd.domain.gen.ColumnsId;
import com.springddd.infrastructure.persistence.entity.GenColumnsEntity;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GenColumnsBatchSaveDomainServiceImpl implements GenColumnsBatchSaveDomainService {
    
    private final GenColumnsRepository genColumnsRepository;

    @Override
    public Mono<Void> batchSave(List<GenColumnsDomain> domains) {
        List<GenColumnsEntity> entities = new ArrayList<>();

        for (GenColumnsDomain domain : domains) {
            GenColumnsEntity entity = new GenColumnsEntity();

            entity.setId(Optional.ofNullable(domain.getId()).map(ColumnsId::value).orElse(null));
            entity.setInfoId(domain.getInfoId().value());

            entity.setPropColumnKey(domain.getProp().propColumnKey());
            entity.setPropColumnName(domain.getProp().propColumnName());
            entity.setPropColumnType(domain.getProp().propColumnType());
            entity.setPropColumnComment(domain.getProp().propColumnComment());
            entity.setPropJavaEntity(domain.getProp().propJavaEntity());
            entity.setPropJavaType(domain.getProp().propJavaType());

            entity.setTableVisible(domain.getTable().tableVisible());
            entity.setTableFilter(domain.getTable().tableFilter());
            entity.setTableOrder(domain.getTable().tableOrder());
            entity.setTableFilterComponent(domain.getTable().tableFilterComponent());
            entity.setTableFilterType(domain.getTable().tableFilterType());

            entity.setFormComponent(domain.getForm().formComponent());
            entity.setFormVisible(domain.getForm().formVisible());
            entity.setFormRequired(domain.getForm().formRequired());

            entity.setPropDictId(domain.getExtendInfo().propDictId());
            entity.setTypescriptType(domain.getExtendInfo().typescriptType());

            entity.setDeleteStatus(domain.getDeleteStatus());
            entity.setCreateBy(domain.getCreateBy());
            entity.setCreateTime(domain.getCreateTime());
            entity.setUpdateBy(domain.getUpdateBy());
            entity.setUpdateTime(domain.getUpdateTime());
            entity.setVersion(domain.getVersion());

            entities.add(entity);
        }
        return genColumnsRepository.saveAll(entities).then();
    }
}
