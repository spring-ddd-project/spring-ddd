package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.SysDictItemQuery;
import com.springddd.domain.dict.WipeSysDictByIdsDomainService;
import com.springddd.infrastructure.persistence.entity.SysDictItemEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysDictItemRepository;
import com.springddd.infrastructure.persistence.r2dbc.SysDictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WipeSysDictByIdsDomainServiceImpl implements WipeSysDictByIdsDomainService {

    private final SysDictRepository sysDictRepository;

    private final SysDictItemRepository sysDictItemRepository;

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @Override
    public Mono<Void> deleteByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Mono.empty();
        }
        return r2dbcEntityTemplate
                .select(Query.query(Criteria.where(SysDictItemQuery.Fields.dictId).in(ids)), SysDictItemEntity.class)
                .map(SysDictItemEntity::getId)
                .filter(Objects::nonNull)
                .collectList()
                .flatMap(sysDictItemRepository::deleteAllById
                )
                .then(
                        sysDictRepository.deleteAllById(ids)
                )
                .then();
    }

}
