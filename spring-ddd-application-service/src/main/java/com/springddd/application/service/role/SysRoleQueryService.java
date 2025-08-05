package com.springddd.application.service.role;


import com.springddd.application.service.role.dto.SysRoleQuery;
import com.springddd.application.service.role.dto.SysRoleView;
import com.springddd.application.service.role.dto.SysRoleViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.SysRoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SysRoleQueryService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final SysRoleViewMapStruct sysRoleViewMapStruct;

    public Mono<PageResponse<SysRoleView>> page(SysRoleQuery query) {
        Criteria criteria = Criteria.where("delete_status").is("0");
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());

        Mono<List<SysRoleView>> list = r2dbcEntityTemplate.select(SysRoleEntity.class).matching(qry).all().collectList().map(sysRoleViewMapStruct::toViewList);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), SysRoleEntity.class);
        return Mono.zip(list, count)
                .map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }

    public Mono<List<SysRoleView>> queryAllRoles() {
        Criteria criteria = Criteria.where("delete_status").is("0");
        return r2dbcEntityTemplate.select(SysRoleEntity.class).matching(Query.query(criteria)).all().collectList().map(sysRoleViewMapStruct::toViewList);
    }
}
