package com.springddd.infrastructure.persistence;

import com.springddd.domain.dict.*;
import com.springddd.infrastructure.persistence.entity.SysDictEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysDictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SysDictDomainRepositoryImpl implements SysDictDomainRepository {

    private final SysDictRepository sysDictRepository;

    @Override
    public Mono<SysDictDomain> load(DictId aggregateRootId) {
        return sysDictRepository.findById(aggregateRootId.value()).map(e -> {
            SysDictDomain sysDictDomain = new SysDictDomain();

            DictBasicInfo dictBasicInfo = new DictBasicInfo(new DictName(e.getDictName()), new DictCode(e.getDictCode()));
            sysDictDomain.setDictBasicInfo(dictBasicInfo);

            DictExtendInfo dictExtendInfo = new DictExtendInfo(e.getSortOrder(), e.getDictStatus());
            sysDictDomain.setDictExtendInfo(dictExtendInfo);

            sysDictDomain.setDeleteStatus(e.getDeleteStatus());
            sysDictDomain.setVersion(e.getVersion());
            sysDictDomain.setCreateBy(e.getCreateBy());
            sysDictDomain.setCreateTime(e.getCreateTime());
            sysDictDomain.setUpdateBy(e.getUpdateBy());
            sysDictDomain.setUpdateTime(e.getUpdateTime());
            return sysDictDomain;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Long> save(SysDictDomain aggregateRoot) {
        SysDictEntity entity = new SysDictEntity();

        entity.setId(Optional.ofNullable(aggregateRoot.getDictId()).map(DictId::value).orElse(null));

        entity.setDictName(aggregateRoot.getDictBasicInfo().dictName().value());
        entity.setDictCode(aggregateRoot.getDictBasicInfo().dictCode().value());

        entity.setSortOrder(aggregateRoot.getDictExtendInfo().sortOrder());
        entity.setDictStatus(aggregateRoot.getDictExtendInfo().dictStatus());

        entity.setDeleteStatus(aggregateRoot.getDeleteStatus());
        entity.setVersion(aggregateRoot.getVersion());
        entity.setCreateBy(aggregateRoot.getCreateBy());
        entity.setCreateTime(aggregateRoot.getCreateTime());
        entity.setUpdateBy(aggregateRoot.getUpdateBy());
        entity.setUpdateTime(aggregateRoot.getUpdateTime());
        return sysDictRepository.save(entity).map(SysDictEntity::getId);
    }
}
