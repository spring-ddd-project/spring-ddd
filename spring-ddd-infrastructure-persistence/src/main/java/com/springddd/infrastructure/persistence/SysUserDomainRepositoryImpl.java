package com.springddd.infrastructure.persistence;

import com.springddd.domain.user.*;
import com.springddd.infrastructure.persistence.entity.SysUserEntity;
import com.springddd.infrastructure.persistence.mapper.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SysUserDomainRepositoryImpl implements SysUserDomainRepository {

    private final SysUserRepository sysUserRepository;

    @Override
    public Mono<SysUserDomain> load(UserId aggregateRootId) {
        return sysUserRepository.findById(aggregateRootId.value()).flatMap(e -> {
            SysUserDomain sysUserDomain = new SysUserDomain();

            sysUserDomain.setUserId(new UserId(e.getId()));

            Account account = new Account();
            account.setUsername(new Username(e.getUsername()));
            account.setPassword(new Password(e.getPassword()));
            account.setPhone(e.getPhone());
            account.setEmail(e.getEmail());
            account.setLockStatus(e.getLockStatus());
            sysUserDomain.setAccount(account);

            ExtendInfo extendInfo = new ExtendInfo();
            extendInfo.setAvatar(e.getAvatar());
            extendInfo.setSex(e.getSex());
            sysUserDomain.setExtendInfo(extendInfo);

            sysUserDomain.setDeptId(e.getDeptId());
            sysUserDomain.setDeleteStatus(e.getDeleteStatus());
            sysUserDomain.setCreateBy(e.getCreateBy());
            sysUserDomain.setCreateTime(e.getCreateTime());
            sysUserDomain.setUpdateBy(e.getUpdateBy());
            sysUserDomain.setUpdateTime(e.getUpdateTime());
            sysUserDomain.setVersion(e.getVersion());
            return Mono.just(sysUserDomain);
        });
    }

    @Override
    public Mono<Void> save(SysUserDomain aggregateRoot) {
        SysUserEntity entity = new SysUserEntity();

        entity.setId(Optional.ofNullable(aggregateRoot.getUserId()).map(UserId::value).orElse(null));

        Account account = aggregateRoot.getAccount();
        entity.setUsername(account.getUsername().value());
        entity.setPassword(account.getPassword().value());
        entity.setPhone(account.getPhone());
        entity.setEmail(account.getEmail());
        entity.setLockStatus(account.getLockStatus());

        ExtendInfo extendInfo = aggregateRoot.getExtendInfo();
        entity.setAvatar(extendInfo.getAvatar());
        entity.setSex(extendInfo.getSex());

        entity.setDeptId(aggregateRoot.getDeptId());
        entity.setDeleteStatus(aggregateRoot.getDeleteStatus());
        entity.setVersion(aggregateRoot.getVersion());
        entity.setCreateBy(aggregateRoot.getCreateBy());
        entity.setCreateTime(aggregateRoot.getCreateTime());
        entity.setUpdateBy(aggregateRoot.getUpdateBy());
        entity.setUpdateTime(aggregateRoot.getUpdateTime());

        return sysUserRepository.save(entity).then();
    }
}
