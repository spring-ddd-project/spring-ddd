package com.springddd.infrastructure.persistence;

import com.springddd.domain.dept.*;
import com.springddd.infrastructure.persistence.entity.SysDeptEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysDeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SysDeptDomainRepositoryImpl implements SysDeptDomainRepository {

    private final SysDeptRepository sysDeptRepository;

    @Override
    public Mono<SysDeptDomain> load(DeptId aggregateRootId) {
        return sysDeptRepository.findById(aggregateRootId.value()).map(entity -> {
            SysDeptDomain sysDeptDomain = new SysDeptDomain();

            sysDeptDomain.setId(new DeptId(entity.getId()));
            sysDeptDomain.setParentId(new DeptId(entity.getParentId()));

            DeptBasicInfo basicInfo = new DeptBasicInfo(new DeptName(entity.getDeptName()));
            sysDeptDomain.setDeptBasicInfo(basicInfo);

            DeptExtendInfo extendInfo = new DeptExtendInfo(entity.getSortOrder(), entity.getDeptStatus());
            sysDeptDomain.setDeptExtendInfo(extendInfo);

            sysDeptDomain.setDeleteStatus(entity.getDeleteStatus());
            sysDeptDomain.setDeptId(entity.getId());
            sysDeptDomain.setCreateBy(entity.getCreateBy());
            sysDeptDomain.setCreateTime(entity.getCreateTime());
            sysDeptDomain.setUpdateBy(entity.getUpdateBy());
            sysDeptDomain.setUpdateTime(entity.getUpdateTime());
            sysDeptDomain.setVersion(entity.getVersion());
            return sysDeptDomain;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Long> save(SysDeptDomain aggregateRoot) {
        SysDeptEntity entity = new SysDeptEntity();

        entity.setId(Optional.ofNullable(aggregateRoot.getId()).map(DeptId::value).orElse(null));
        entity.setParentId(Optional.ofNullable(aggregateRoot.getParentId()).map(DeptId::value).orElse(null));

        DeptBasicInfo basicInfo = aggregateRoot.getDeptBasicInfo();
        entity.setDeptName(basicInfo.deptName().value());

        DeptExtendInfo extendInfo = aggregateRoot.getDeptExtendInfo();
        entity.setSortOrder(extendInfo.sortOrder());
        entity.setDeptStatus(extendInfo.deptStatus());

        entity.setDeleteStatus(aggregateRoot.getDeleteStatus());
        entity.setCreateBy(aggregateRoot.getCreateBy());
        entity.setCreateTime(aggregateRoot.getCreateTime());
        entity.setUpdateBy(aggregateRoot.getUpdateBy());
        entity.setUpdateTime(aggregateRoot.getUpdateTime());
        entity.setVersion(aggregateRoot.getVersion());

        return sysDeptRepository.save(entity).map(SysDeptEntity::getId);
    }
}
