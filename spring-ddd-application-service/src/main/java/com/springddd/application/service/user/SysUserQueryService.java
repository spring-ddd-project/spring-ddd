package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserQuery;
import com.springddd.application.service.user.dto.SysUserView;
import com.springddd.application.service.user.dto.SysUserViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.SysUserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysUserQueryService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final SysUserViewMapStruct sysUserViewMapStruct;

    public Mono<PageResponse<SysUserView>> page(SysUserQuery query) {
        Criteria criteria = Criteria.where("delete_status").is(0);
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());

        Mono<List<SysUserView>> list = r2dbcEntityTemplate.select(SysUserEntity.class).matching(qry).all().collectList().map(sysUserViewMapStruct::toViewList);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), SysUserEntity.class);
        return Mono.zip(list, count)
                .map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));

    }

    public Mono<PageResponse<SysUserView>> recycle(SysUserQuery query) {
        Criteria criteria = Criteria.where("delete_status").is(1);
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());
        Mono<List<SysUserView>> list = r2dbcEntityTemplate.select(SysUserEntity.class).matching(qry).all().collectList().map(sysUserViewMapStruct::toViewList);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), SysUserEntity.class);
        return Mono.zip(list, count)
                .map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }

    public Mono<SysUserView> queryUserByUsername(String username) {
        return r2dbcEntityTemplate.selectOne(Query.query(Criteria.where("username").is(username)), SysUserEntity.class)
                .map(sysUserViewMapStruct::toView);
    }
}
