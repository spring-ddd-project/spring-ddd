package com.springddd.infrastructure.persistence.factory.impl;

import com.springddd.domain.dept.SysDeptDomainRepository;
import com.springddd.domain.dict.SysDictDomainRepository;
import com.springddd.domain.dict.SysDictItemDomainRepository;
import com.springddd.domain.gen.*;
import com.springddd.domain.menu.SysMenuDomainRepository;
import com.springddd.domain.role.SysRoleDomainRepository;
import com.springddd.domain.user.SysUserDomainRepository;
import com.springddd.infrastructure.persistence.factory.RepositoryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultRepositoryFactory implements RepositoryFactory {

    private final GenAggregateDomainRepository genAggregateDomainRepository;
    private final SysUserDomainRepository sysUserDomainRepository;
    private final SysDeptDomainRepository sysDeptDomainRepository;
    private final SysDictDomainRepository sysDictDomainRepository;
    private final SysDictItemDomainRepository sysDictItemDomainRepository;
    private final SysMenuDomainRepository sysMenuDomainRepository;
    private final SysRoleDomainRepository sysRoleDomainRepository;
    private final GenColumnBindDomainRepository genColumnBindDomainRepository;
    private final GenColumnsDomainRepository genColumnsDomainRepository;
    private final GenProjectInfoDomainRepository genProjectInfoDomainRepository;
    private final GenTemplateDomainRepository genTemplateDomainRepository;

    @Override
    public GenAggregateDomainRepository getGenAggregateDomainRepository() {
        return genAggregateDomainRepository;
    }

    @Override
    public SysUserDomainRepository getSysUserDomainRepository() {
        return sysUserDomainRepository;
    }

    @Override
    public SysDeptDomainRepository getSysDeptDomainRepository() {
        return sysDeptDomainRepository;
    }

    @Override
    public SysDictDomainRepository getSysDictDomainRepository() {
        return sysDictDomainRepository;
    }

    @Override
    public SysDictItemDomainRepository getSysDictItemDomainRepository() {
        return sysDictItemDomainRepository;
    }

    @Override
    public SysMenuDomainRepository getSysMenuDomainRepository() {
        return sysMenuDomainRepository;
    }

    @Override
    public SysRoleDomainRepository getSysRoleDomainRepository() {
        return sysRoleDomainRepository;
    }

    @Override
    public GenColumnBindDomainRepository getGenColumnBindDomainRepository() {
        return genColumnBindDomainRepository;
    }

    @Override
    public GenColumnsDomainRepository getGenColumnsDomainRepository() {
        return genColumnsDomainRepository;
    }

    @Override
    public GenProjectInfoDomainRepository getGenProjectInfoDomainRepository() {
        return genProjectInfoDomainRepository;
    }

    @Override
    public GenTemplateDomainRepository getGenTemplateDomainRepository() {
        return genTemplateDomainRepository;
    }
}
