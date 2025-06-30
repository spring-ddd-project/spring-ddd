package com.springddd.application.service.user;

import com.springddd.domain.user.DeleteSysUserByIdsDomainService;
import com.springddd.infrastructure.persistence.entity.SysUserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteSysUserByIdsDomainServiceImpl implements DeleteSysUserByIdsDomainService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> deleteByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Mono.empty();
        }
        return r2dbcEntityTemplate.delete(Query.query(Criteria.where("id").in(ids)), SysUserEntity.class).then();
    }
}
