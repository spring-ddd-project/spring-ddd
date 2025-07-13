package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserCommand;
import com.springddd.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysUserCommandService {

    private final SysUserDomainRepository sysUserDomainRepository;

    private final SysUserDomainFactory sysUserDomainFactory;

    private final WipeSysUserByIdsDomainService wipeSysUserByIdsDomainService;

    private final PasswordEncoder passwordEncoder;

    private final DeleteSysUserByIdDomainService deleteSysUserByIdDomainService;

    private final RestoreSysUserByIdDomainService restoreSysUserByIdDomainService;

    public Mono<Long> createUser(SysUserCommand command) {
        Account account = new Account();
        account.setUsername(new Username(command.getUsername()));
        account.setPassword(new Password(passwordEncoder.encode(command.getPassword())));
        account.setPhone(command.getPhone());
        account.setEmail(command.getEmail());
        account.setLockStatus(command.getLockStatus());

        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar(command.getAvatar());
        extendInfo.setSex(command.getSex());

        SysUserDomain sysUserDomain = sysUserDomainFactory.newInstance(account, extendInfo, command.getDeptId());
        sysUserDomain.create();

        return sysUserDomainRepository.save(sysUserDomain);
    }

    public Mono<Void> updateUser(SysUserCommand command) {
        return sysUserDomainRepository.load(new UserId(command.getId())).flatMap(domain -> {
            Account account = new Account();
            account.setUsername(new Username(command.getUsername()));
            account.setPassword(new Password(passwordEncoder.encode(domain.getAccount().getPassword().value())));
            account.setEmail(command.getEmail());
            account.setPhone(command.getPhone());
            account.setLockStatus(command.getLockStatus());

            ExtendInfo extendInfo = new ExtendInfo();
            extendInfo.setAvatar(command.getAvatar());
            extendInfo.setSex(command.getSex());

            domain.updateUser(account, extendInfo, command.getDeptId());
            return sysUserDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> wipe(List<Long> ids) {
        return wipeSysUserByIdsDomainService.deleteByIds(ids);
    }

    public Mono<Void> delete(List<Long> ids) {
        return deleteSysUserByIdDomainService.deleteByIds(ids);
    }

    public Mono<Void> restore(List<Long> ids) {
        return restoreSysUserByIdDomainService.restoreByIds(ids);
    }
}
