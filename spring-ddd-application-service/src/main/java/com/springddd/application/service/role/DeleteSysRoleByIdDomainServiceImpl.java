package com.springddd.application.service.role;

import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.role.DeleteSysRoleByIdDomainService;
import com.springddd.domain.role.RoleId;
import com.springddd.domain.role.SysRoleDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteSysRoleByIdDomainServiceImpl implements DeleteSysRoleByIdDomainService {

    private final SysRoleDomainRepository sysRoleDomainRepository;

    @Override
    public Mono<Void> deleteByIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(id -> sysRoleDomainRepository.load(new RoleId(id))
                        .flatMap(domain -> {
                            domain.delete();
                            return sysRoleDomainRepository.save(domain);
                        }), SecurityUtils.concurrency()).then();
    }
}
