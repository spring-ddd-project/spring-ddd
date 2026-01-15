package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRoleMenuQuery;
import com.springddd.application.service.role.dto.SysRoleMenuView;
import com.springddd.application.service.role.dto.SysRoleMenuViewMapStruct;
import com.springddd.infrastructure.persistence.entity.SysRoleMenuEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleMenuQueryService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final SysRoleMenuViewMapStruct sysRoleMenuViewMapStruct;

    public Mono<List<SysRoleMenuView>> queryLinkRoleAndMenus(Long roleId) {
        Criteria criteria = Criteria
                .where(SysRoleMenuQuery.Fields.roleId).is(roleId)
                .and(SysRoleMenuQuery.Fields.deleteStatus).is(false);
        Query qry = Query.query(criteria);
        return r2dbcEntityTemplate.select(SysRoleMenuEntity.class).matching(qry).all().collectList().map(sysRoleMenuViewMapStruct::toViewList);
    }
}
