package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserRoleQuery;
import com.springddd.application.service.user.dto.SysUserRoleView;
import com.springddd.application.service.user.dto.SysUserRoleViewMapStruct;
import com.springddd.infrastructure.persistence.entity.SysUserRoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysUserRoleQueryService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final SysUserRoleViewMapStruct sysUserRoleViewMapStruct;

    public Mono<List<SysUserRoleView>> queryLinkUserAndRole(Long userId) {
        Criteria criteria = Criteria.
                where(SysUserRoleQuery.Fields.userId).is(userId).and(SysUserRoleQuery.Fields.deleteStatus).is(false);
        Query qry = Query.query(criteria);
        return r2dbcEntityTemplate.select(SysUserRoleEntity.class).matching(qry).all().collectList().map(sysUserRoleViewMapStruct::toViewList);
    }
}
