package com.springddd.infrastructure.persistence;

import com.springddd.domain.dict.*;
import com.springddd.infrastructure.persistence.entity.SysDictItemEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysDictItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SysDictItemDomainRepositoryImpl implements SysDictItemDomainRepository {

    private final SysDictItemRepository sysDictItemRepository;

    @Override
    public Mono<SysDictItemDomain> load(DictItemId aggregateRootId) {
        return sysDictItemRepository.findById(aggregateRootId.value()).map(e -> {
            SysDictItemDomain domain = new SysDictItemDomain();

            domain.setItemId(new DictItemId(e.getId()));
            domain.setDictId(new DictId(e.getDictId()));

            DictItemBasicInfo basicInfo = new DictItemBasicInfo(new ItemLabel(e.getItemLabel()), new ItemValue(e.getItemValue()));
            domain.setItemBasicInfo(basicInfo);

            DictItemExtendInfo extendInfo = new DictItemExtendInfo(e.getSortOrder(), e.getItemStatus());
            domain.setItemExtendInfo(extendInfo);

            domain.setDeleteStatus(e.getDeleteStatus());
            domain.setVersion(e.getVersion());
            domain.setCreateBy(e.getCreateBy());
            domain.setCreateTime(e.getCreateTime());
            domain.setUpdateBy(e.getUpdateBy());
            domain.setUpdateTime(e.getUpdateTime());
            return domain;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Long> save(SysDictItemDomain aggregateRoot) {
        SysDictItemEntity entity = new SysDictItemEntity();

        entity.setId(Optional.ofNullable(aggregateRoot.getItemId()).map(DictItemId::value).orElse(null));
        entity.setDictId(aggregateRoot.getDictId().value());

        entity.setItemLabel(aggregateRoot.getItemBasicInfo().itemLabel().value());
        entity.setItemValue(aggregateRoot.getItemBasicInfo().itemValue().value());
        entity.setSortOrder(aggregateRoot.getItemExtendInfo().sortOrder());
        entity.setItemStatus(aggregateRoot.getItemExtendInfo().itemStatus());
        entity.setDeleteStatus(aggregateRoot.getDeleteStatus());

        return sysDictItemRepository.save(entity).map(SysDictItemEntity::getId);
    }
}
