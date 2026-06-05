package com.springddd.infrastructure.persistence;

import com.springddd.domain.user.*;
import com.springddd.infrastructure.persistence.entity.SysUserEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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

            Account account = new Account(
                    new Username(e.getUsername()),
                    new Password(e.getPassword()),
                    e.getEmail(),
                    e.getPhone(),
                    e.getLockStatus()
            );
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
    @Transactional(rollbackFor = Exception.class)
    public Mono<Long> save(SysUserDomain aggregateRoot) {
        SysUserEntity entity = new SysUserEntity();

        entity.setId(Optional.ofNullable(aggregateRoot.getUserId()).map(UserId::value).orElse(null));

        Account account = aggregateRoot.getAccount();
        entity.setUsername(account.username() != null ? account.username().value() : null);
        entity.setPassword(account.password() != null ? account.password().value() : null);
        entity.setPhone(account.phone());
        entity.setEmail(account.email());
        entity.setLockStatus(account.lockStatus());

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

        return sysUserRepository.save(entity).map(SysUserEntity::getId);
    }
}
