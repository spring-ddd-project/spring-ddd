package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserCommand;
import com.springddd.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysUserCommandService {

    private final SysUserDomainRepository sysUserDomainRepository;

    private final SysUserDomainFactory sysUserDomainFactory;

    private final DeleteSysUserByIdsDomainService deleteSysUserByIdsDomainService;

    public Mono<Void> createUser(SysUserCommand command) {
        Account account = new Account();
        account.setUsername(new Username(command.getUsername()));
        // TODO Passwords will be encrypted using BCryptPasswordEncoder going forward.
        account.setPassword(new Password(command.getPassword()));
        account.setPhone(command.getPhone());
        account.setEmail(command.getEmail());
        account.setLockStatus(command.getLockStatus());

        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar(command.getAvatar());
        extendInfo.setSex(command.getSex());

        // TODO CreateBy and UpdateBy will be retrieved from the JWT token going forward.
        SysUserDomain sysUserDomain = sysUserDomainFactory.newInstance(account, extendInfo, command.getDeptId(), "TODO");
        sysUserDomain.create();

        return sysUserDomainRepository.save(sysUserDomain);
    }

    public Mono<Void> updateUser(SysUserCommand command) {
        return sysUserDomainRepository.load(new UserId(command.getId())).flatMap(domain -> {
            Account account = new Account();
            account.setUsername(new Username(command.getUsername()));
            // TODO Passwords will be encrypted using BCryptPasswordEncoder going forward.
            account.setPassword(new Password(domain.getAccount().getPassword().value()));
            account.setEmail(command.getEmail());
            account.setPhone(command.getPhone());
            account.setLockStatus(command.getLockStatus());

            ExtendInfo extendInfo = new ExtendInfo();
            extendInfo.setAvatar(command.getAvatar());
            extendInfo.setSex(command.getSex());

            // TODO CreateBy and UpdateBy will be retrieved from the JWT token going forward.
            domain.updateUser(account, extendInfo, command.getDeptId(), "TODO");
            return sysUserDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> deleteUser(SysUserCommand command) {
        return sysUserDomainRepository.load(new UserId(command.getId())).flatMap(domain -> {

            // TODO CreateBy and UpdateBy will be retrieved from the JWT token going forward.
            domain.delete("TODO");
            return sysUserDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> wipe(List<Long> ids) {
        return deleteSysUserByIdsDomainService.deleteByIds(ids);
    }
}
