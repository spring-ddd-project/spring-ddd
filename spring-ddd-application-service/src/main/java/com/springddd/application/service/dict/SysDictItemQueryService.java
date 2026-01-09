package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.*;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.SysDictItemEntity;
import com.springddd.infrastructure.persistence.factory.CriteriaFlyweightFactory;
import com.springddd.infrastructure.persistence.factory.QueryFactory;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class SysDictItemQueryService extends com.springddd.application.service.AbstractQueryService<SysDictItemEntity, SysDictItemView, SysDictItemPageQuery> {

    private final SysDictItemViewMapStruct sysDictItemViewMapStruct;

    public SysDictItemQueryService(QueryFactory queryFactory, SysDictItemViewMapStruct sysDictItemViewMapStruct) {
        super(queryFactory);
        this.sysDictItemViewMapStruct = sysDictItemViewMapStruct;
    }

    @Override
    protected Criteria buildIndexCriteria(SysDictItemPageQuery query) {
        Criteria criteria = com.springddd.infrastructure.persistence.factory.CriteriaFlyweightFactory.getDeleteStatusCriteria(false);
        if (!ObjectUtils.isEmpty(query) && !ObjectUtils.isEmpty(query.getDictId())) {
            criteria = criteria.and(SysDictItemQuery.Fields.dictId).is(query.getDictId());
        }
        return criteria;
    }

    @Override
    protected Criteria buildRecycleCriteria(SysDictItemPageQuery query) {
        Criteria criteria = com.springddd.infrastructure.persistence.factory.CriteriaFlyweightFactory.getDeleteStatusCriteria(true);
        if (!ObjectUtils.isEmpty(query) && !ObjectUtils.isEmpty(query.getDictId())) {
            criteria = criteria.and(SysDictItemQuery.Fields.dictId).is(query.getDictId());
        }
        return criteria;
    }

    @Override
    protected Class<SysDictItemEntity> getEntityClass() {
        return SysDictItemEntity.class;
    }

    @Override
    protected List<SysDictItemView> mapToViews(List<SysDictItemEntity> entities) {
        return sysDictItemViewMapStruct.toViews(entities);
    }

    public Mono<SysDictItemView> queryItemLabelByItemValueAndDictId(Long dictId, Integer itemValue) {
        Criteria criteria = Criteria
                .where(SysDictItemQuery.Fields.deleteStatus).is(false)
                .and(SysDictItemQuery.Fields.dictId).is(dictId)
                .and(SysDictItemQuery.Fields.itemValue).is(itemValue);
        Query qry = Query.query(criteria);
        return queryFactory.getR2dbcEntityTemplate().select(SysDictItemEntity.class).matching(qry).one().map(sysDictItemViewMapStruct::toView);
    }

    public Mono<List<SysDictItemView>> queryItemLabelByDictId(Long dictId) {
        Criteria criteria = Criteria
                .where(SysDictItemQuery.Fields.deleteStatus).is(false)
                .and(SysDictItemQuery.Fields.dictId).is(dictId);
        Query qry = Query.query(criteria);
        return queryFactory.getR2dbcEntityTemplate().select(SysDictItemEntity.class).matching(qry).all().collectList().map(sysDictItemViewMapStruct::toViews);
    }
}






































