package com.springddd.infrastructure.persistence;

import com.springddd.domain.user.*;
import com.springddd.infrastructure.persistence.entity.SysUserPostEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysUserPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SysUserPostDomainRepositoryImpl implements SysUserPostDomainRepository {

    private final SysUserPostRepository sysUserPostRepository;

    @Override
    public Mono<SysUserPostDomain> load(UserPostId aggregateRootId) {
        return sysUserPostRepository.findById(aggregateRootId.value()).map(entity -> {
            SysUserPostDomain domain = new SysUserPostDomain();

            domain.setId(new UserPostId(entity.getId()));

            UserPostInfo info = new UserPostInfo(entity.getUserId(), entity.getPostId());
            domain.setUserPostInfo(info);

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
    public Mono<Long> save(SysUserPostDomain aggregateRoot) {
        SysUserPostEntity entity = new SysUserPostEntity();

        entity.setId(Optional.ofNullable(aggregateRoot.getId()).map(UserPostId::value).orElse(null));

        UserPostInfo info = aggregateRoot.getUserPostInfo();
        entity.setUserId(info.userId());
        entity.setPostId(info.postId());

        entity.setDeleteStatus(aggregateRoot.getDeleteStatus());
        entity.setCreateBy(aggregateRoot.getCreateBy());
        entity.setCreateTime(aggregateRoot.getCreateTime());
        entity.setUpdateBy(aggregateRoot.getUpdateBy());
        entity.setUpdateTime(aggregateRoot.getUpdateTime());
        entity.setVersion(aggregateRoot.getVersion());

        return sysUserPostRepository.save(entity).map(SysUserPostEntity::getId);
    }
}
