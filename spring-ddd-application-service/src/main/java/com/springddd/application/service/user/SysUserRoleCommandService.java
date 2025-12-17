package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserRoleCommand;
import com.springddd.domain.role.RoleId;
import com.springddd.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SysUserRoleCommandService {

    private final SysUserRoleDomainRepository sysUserRoleDomainRepository;

    private final SysUserRoleDomainFactory sysUserRoleDomainFactory;

    public Mono<Long> create(SysUserRoleCommand command) {
        SysUserRoleDomain domain = sysUserRoleDomainFactory.newInstance(new UserId(command.getUserId()),
                new RoleId(command.getRoleId()), command.getDeptId(), "TODO");
        domain.create();
        return sysUserRoleDomainRepository.save(domain);
    }

    public Mono<Void> update(SysUserRoleCommand command) {
        return sysUserRoleDomainRepository.load(new UserRoleId(command.getId())).flatMap(domain -> {
            domain.update(new UserId(command.getUserId()), new RoleId(command.getRoleId()), command.getDeptId(), "TODO");
            return sysUserRoleDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> delete(SysUserRoleCommand command) {
        return sysUserRoleDomainRepository.load(new UserRoleId(command.getId())).flatMap(domain -> {
            domain.delete("TODO");
            return sysUserRoleDomainRepository.save(domain);
        }).then();
    }
}
