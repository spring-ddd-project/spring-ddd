package com.springddd.application.service.role;

import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.role.RestoreSysRoleByIdDomainService;
import com.springddd.domain.role.RoleId;
import com.springddd.domain.role.SysRoleDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RestoreSysRoleByIdDomainServiceImpl implements RestoreSysRoleByIdDomainService {

    private final SysRoleDomainRepository sysRoleDomainRepository;

    @Override
    public Mono<Void> restoreByIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(id -> sysRoleDomainRepository.load(new RoleId(id))
                        .flatMap(domain -> {
                            domain.restore();
                            return sysRoleDomainRepository.save(domain);
                        }), SecurityUtils.concurrency()).then();
    }
}
