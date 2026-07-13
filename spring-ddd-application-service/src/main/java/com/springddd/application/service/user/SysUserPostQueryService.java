package com.springddd.application.service.user;

import com.springddd.application.service.common.DataScopeQueryFilter;
import com.springddd.application.service.user.dto.SysUserPostPageQuery;
import com.springddd.application.service.user.dto.SysUserPostQuery;
import com.springddd.application.service.user.dto.SysUserPostView;
import com.springddd.application.service.user.dto.SysUserPostViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.SysUserPostEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysUserPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysUserPostQueryService {

    private final SysUserPostRepository sysUserPostRepository;

    private final SysUserPostViewMapStruct sysUserPostViewMapStruct;

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final DataScopeQueryFilter dataScopeQueryFilter;

    public Mono<List<SysUserPostView>> listByUserId(Long userId) {
        return sysUserPostRepository.findByUserIdAndDeleteStatusFalse(userId)
                .collectList()
                .map(sysUserPostViewMapStruct::toViews);
    }

    public Mono<PageResponse<SysUserPostView>> index(Long menuId, SysUserPostPageQuery query) {
        return dataScopeQueryFilter.apply(menuId)
                .flatMap(scopeResult -> {
                    Criteria criteria = Criteria.where(SysUserPostQuery.Fields.deleteStatus).is(false);
                    if (!ObjectUtils.isEmpty(query.getUserId())) {
                        criteria = criteria.and(SysUserPostQuery.Fields.userId).is(query.getUserId());
                    }
                    if (!ObjectUtils.isEmpty(query.getPostId())) {
                        criteria = criteria.and(SysUserPostQuery.Fields.postId).is(query.getPostId());
                    }
                    if (!scopeResult.isAll()) {
                        criteria = criteria.and(DataScopeQueryFilter.createByInCriteria(SysUserPostQuery.Fields.createBy, scopeResult.getVisibleUsernames()));
                    }
                    Query qry = Query.query(criteria)
                            .limit(query.getPageSize())
                            .offset((long) (query.getPageNum() - 1) * query.getPageSize());
                    Mono<List<SysUserPostView>> list = r2dbcEntityTemplate.select(SysUserPostEntity.class).matching(qry).all().collectList().map(sysUserPostViewMapStruct::toViews);
                    Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), SysUserPostEntity.class);
                    return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
                });
    }

    public Mono<PageResponse<SysUserPostView>> recycle(Long menuId, SysUserPostPageQuery query) {
        Criteria baseCriteria = Criteria.where(SysUserPostQuery.Fields.deleteStatus).is(true);
        return dataScopeQueryFilter.apply(menuId)
                .flatMap(scopeResult -> {
                    Criteria criteria = baseCriteria;
                    if (!scopeResult.isAll()) {
                        criteria = criteria.and(DataScopeQueryFilter.createByInCriteria(SysUserPostQuery.Fields.createBy, scopeResult.getVisibleUsernames()));
                    }
                    Query qry = Query.query(criteria)
                            .limit(query.getPageSize())
                            .offset((long) (query.getPageNum() - 1) * query.getPageSize());
                    Mono<List<SysUserPostView>> list = r2dbcEntityTemplate.select(SysUserPostEntity.class).matching(qry).all().collectList().map(sysUserPostViewMapStruct::toViews);
                    Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), SysUserPostEntity.class);
                    return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
                });
    }
}
