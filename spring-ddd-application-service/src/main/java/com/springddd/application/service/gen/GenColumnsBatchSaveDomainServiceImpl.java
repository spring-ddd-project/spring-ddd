package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenColumnsBatchSaveDomainService;
import com.springddd.domain.gen.GenColumnsDomain;
import com.springddd.domain.gen.GenColumnsId;
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

            entity.setId(Optional.ofNullable(domain.getId()).map(GenColumnsId::value).orElse(null));
            entity.setInfoId(domain.getInfoId().value());

            entity.setPropColumnKey(domain.getBasicInfo().key().value());
            entity.setPropColumnName(domain.getBasicInfo().name().value());
            entity.setPropColumnType(domain.getBasicInfo().type().value());
            entity.setPropColumnComment(domain.getBasicInfo().comment().value());
            entity.setPropJavaEntity(domain.getBasicInfo().entity().value());
            entity.setPropJavaType(domain.getBasicInfo().javaType().value());

            entity.setPropDictId(domain.getExtendInfo().propDictId());
            entity.setTableVisible(domain.getExtendInfo().tableVisible());
            entity.setTableFilter(domain.getExtendInfo().tableFilter());
            entity.setTableOrder(domain.getExtendInfo().tableOrder());
            entity.setTableFilterComponent(domain.getExtendInfo().tableFilterComponent());
            entity.setTableFilterType(domain.getExtendInfo().tableFilterType());
            entity.setFormComponent(domain.getExtendInfo().formComponent());
            entity.setFormVisible(domain.getExtendInfo().formVisible());
            entity.setFormRequired(domain.getExtendInfo().formRequired());

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
