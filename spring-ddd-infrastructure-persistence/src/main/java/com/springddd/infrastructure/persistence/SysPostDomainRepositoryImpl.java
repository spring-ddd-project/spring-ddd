package com.springddd.infrastructure.persistence;

import com.springddd.domain.post.*;
import com.springddd.infrastructure.persistence.entity.SysPostEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SysPostDomainRepositoryImpl implements SysPostDomainRepository {

    private final SysPostRepository sysPostRepository;

    @Override
    public Mono<SysPostDomain> load(PostId aggregateRootId) {
        return sysPostRepository.findById(aggregateRootId.value()).map(entity -> {
            SysPostDomain domain = new SysPostDomain();

            domain.setId(new PostId(entity.getId()));

            PostBasicInfo basicInfo = new PostBasicInfo(entity.getPostCode(), entity.getPostName());
            domain.setPostBasicInfo(basicInfo);

            PostExtendInfo extendInfo = new PostExtendInfo(entity.getParentId(), entity.getSortOrder(), entity.getPostStatus());
            domain.setPostExtendInfo(extendInfo);

            domain.setDeleteStatus(entity.getDeleteStatus());
            domain.setCreateBy(entity.getCreateBy());
            domain.setCreateTime(entity.getCreateTime());
            domain.setUpdateBy(entity.getUpdateBy());
            domain.setUpdateTime(entity.getUpdateTime());
            domain.setVersion(entity.getVersion());
            return domain;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Long> save(SysPostDomain aggregateRoot) {
        SysPostEntity entity = new SysPostEntity();

        entity.setId(Optional.ofNullable(aggregateRoot.getId()).map(PostId::value).orElse(null));

        PostBasicInfo basicInfo = aggregateRoot.getPostBasicInfo();
        entity.setPostCode(basicInfo.postCode());
        entity.setPostName(basicInfo.postName());

        PostExtendInfo extendInfo = aggregateRoot.getPostExtendInfo();
        entity.setParentId(extendInfo.parentId());
        entity.setSortOrder(extendInfo.sortOrder());
        entity.setPostStatus(extendInfo.postStatus());

        entity.setDeleteStatus(aggregateRoot.getDeleteStatus());
        entity.setCreateBy(aggregateRoot.getCreateBy());
        entity.setCreateTime(aggregateRoot.getCreateTime());
        entity.setUpdateBy(aggregateRoot.getUpdateBy());
        entity.setUpdateTime(aggregateRoot.getUpdateTime());
        entity.setVersion(aggregateRoot.getVersion());

        return sysPostRepository.save(entity).map(SysPostEntity::getId);
    }
}
