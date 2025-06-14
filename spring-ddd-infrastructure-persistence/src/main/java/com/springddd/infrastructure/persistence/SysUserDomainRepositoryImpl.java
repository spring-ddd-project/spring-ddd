package com.springddd.infrastructure.persistence;

import com.springddd.domain.user.*;
import com.springddd.infrastructure.persistence.entity.SysUserEntity;
import com.springddd.infrastructure.persistence.mapper.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class SysUserDomainRepositoryImpl implements SysUserDomainRepository {

    private final SysUserRepository sysUserRepository;

    @Override
    public Mono<SysUserDomain> load(UserId aggregateRootId) {
        return sysUserRepository.findById(aggregateRootId.getValue()).flatMap(e -> {
            SysUserDomain sysUserDomain = new SysUserDomain();

            sysUserDomain.setUserId(new UserId(e.getId()));

            Account account = new Account();
            account.setUsername(new Username(e.getUsername()));
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

        if (!ObjectUtils.isEmpty(aggregateRoot.getUserId())) {
            entity.setId(aggregateRoot.getUserId().getValue());
        }

        Account account = aggregateRoot.getAccount();
        entity.setUsername(account.getUsername().getValue());
        if (!ObjectUtils.isEmpty(account.getPassword())) {
            entity.setPassword(account.getPassword().getValue());
        }
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
