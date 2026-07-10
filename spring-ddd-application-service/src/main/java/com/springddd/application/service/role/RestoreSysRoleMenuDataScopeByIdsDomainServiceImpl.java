package com.springddd.application.service.role;

import com.springddd.domain.auth.ReactiveSecurityUtils;
import com.springddd.domain.role.RestoreSysRoleMenuDataScopeByIdsDomainService;
import com.springddd.domain.role.RoleMenuDataScopeId;
import com.springddd.domain.role.SysRoleMenuDataScopeDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RestoreSysRoleMenuDataScopeByIdsDomainServiceImpl implements RestoreSysRoleMenuDataScopeByIdsDomainService {

    private final SysRoleMenuDataScopeDomainRepository sysRoleMenuDataScopeDomainRepository;

    @Override
    public Mono<Void> restoreByIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(id -> sysRoleMenuDataScopeDomainRepository.load(new RoleMenuDataScopeId(id))
                        .flatMap(domain -> {
                            domain.restore();
                            return sysRoleMenuDataScopeDomainRepository.save(domain);
                        }), ReactiveSecurityUtils.concurrency()).then();
    }
}
