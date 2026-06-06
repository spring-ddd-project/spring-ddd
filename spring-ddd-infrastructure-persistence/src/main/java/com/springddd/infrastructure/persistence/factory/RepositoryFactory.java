package com.springddd.infrastructure.persistence.factory;

import com.springddd.domain.dept.SysDeptDomainRepository;
import com.springddd.domain.dict.SysDictDomainRepository;
import com.springddd.domain.dict.SysDictItemDomainRepository;
import com.springddd.domain.gen.*;
import com.springddd.domain.menu.SysMenuDomainRepository;
import com.springddd.domain.role.SysRoleDomainRepository;
import com.springddd.domain.user.SysUserDomainRepository;

/**
 * Factory for repositories.
 */
public interface RepositoryFactory extends InfrastructureFactory {
    GenAggregateDomainRepository getGenAggregateDomainRepository();
    SysUserDomainRepository getSysUserDomainRepository();
    SysDeptDomainRepository getSysDeptDomainRepository();
    SysDictDomainRepository getSysDictDomainRepository();
    SysDictItemDomainRepository getSysDictItemDomainRepository();
    SysMenuDomainRepository getSysMenuDomainRepository();
    SysRoleDomainRepository getSysRoleDomainRepository();
    GenColumnBindDomainRepository getGenColumnBindDomainRepository();
    GenColumnsDomainRepository getGenColumnsDomainRepository();
    GenProjectInfoDomainRepository getGenProjectInfoDomainRepository();
    GenTemplateDomainRepository getGenTemplateDomainRepository();
}
