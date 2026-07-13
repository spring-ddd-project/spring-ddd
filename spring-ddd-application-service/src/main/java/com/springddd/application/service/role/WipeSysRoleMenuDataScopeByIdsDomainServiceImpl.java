package com.springddd.application.service.role;

import com.springddd.domain.role.WipeSysRoleMenuDataScopeByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleMenuDataScopeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeSysRoleMenuDataScopeByIdsDomainServiceImpl implements WipeSysRoleMenuDataScopeByIdsDomainService {

    private final SysRoleMenuDataScopeRepository sysRoleMenuDataScopeRepository;

    @Override
    public Mono<Void> wipeByIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(sysRoleMenuDataScopeRepository::deleteById).then();
    }
}
