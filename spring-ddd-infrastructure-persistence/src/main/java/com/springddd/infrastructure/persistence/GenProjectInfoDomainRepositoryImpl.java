package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.entity.GenProjectInfoEntity;
import com.springddd.infrastructure.persistence.r2dbc.GenProjectInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenProjectInfoDomainRepositoryImpl implements GenProjectInfoDomainRepository {

    private final GenProjectInfoRepository genProjectInfoRepository;

    @Override
    public Mono<GenProjectInfoDomain> load(InfoId aggregateRootId) {
        return genProjectInfoRepository.findById(aggregateRootId.value()).map(e -> {
            GenProjectInfoDomain genInfoDomain = new GenProjectInfoDomain();
            genInfoDomain.setId(new InfoId(e.getId()));

            ProjectInfo projectInfo = new ProjectInfo(e.getTableName(), e.getPackageName(), e.getClassName(), e.getModuleName(), e.getProjectName());
            genInfoDomain.setProjectInfo(projectInfo);

            GenProjectInfoExtendInfo extendInfo = new GenProjectInfoExtendInfo(e.getRequestName());
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
    public Mono<Long> save(GenProjectInfoDomain aggregateRoot) {
        GenProjectInfoEntity entity = new GenProjectInfoEntity();

        entity.setId(Optional.ofNullable(aggregateRoot.getId()).map(InfoId::value).orElse(null));

        entity.setTableName(aggregateRoot.getProjectInfo().tableName());
        entity.setPackageName(aggregateRoot.getProjectInfo().packageName());
        entity.setClassName(aggregateRoot.getProjectInfo().className());
        entity.setModuleName(aggregateRoot.getProjectInfo().moduleName());
        entity.setProjectName(aggregateRoot.getProjectInfo().projectName());

        entity.setRequestName(aggregateRoot.getExtendInfo().requestName());

        entity.setDeleteStatus(aggregateRoot.getDeleteStatus());
        entity.setCreateBy(aggregateRoot.getCreateBy());
        entity.setCreateTime(aggregateRoot.getCreateTime());
        entity.setUpdateBy(aggregateRoot.getUpdateBy());
        entity.setUpdateTime(aggregateRoot.getUpdateTime());
        entity.setVersion(aggregateRoot.getVersion());

        return genProjectInfoRepository.save(entity).map(GenProjectInfoEntity::getId);
    }
}
