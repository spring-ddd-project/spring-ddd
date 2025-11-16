package com.springddd.infrastructure.persistence.factory;

import com.springddd.domain.dept.SysDeptDomain;
import com.springddd.domain.dict.SysDictDomain;
import com.springddd.domain.gen.GenAggregateDomain;
import com.springddd.domain.menu.SysMenuDomain;
import com.springddd.domain.role.SysRoleDomain;
import com.springddd.domain.user.SysUserDomain;
import com.springddd.infrastructure.persistence.entity.*;

/**
 * Factory for creating persistence entities.
 */
public interface EntityFactory extends InfrastructureFactory {
    GenAggregateEntity createGenAggregateEntity(GenAggregateDomain domain);
    GenAggregateDomain createGenAggregateDomain(GenAggregateEntity entity);

    SysUserEntity createSysUserEntity(SysUserDomain domain);
    SysUserDomain createSysUserDomain(SysUserEntity entity);

    SysDeptEntity createSysDeptEntity(SysDeptDomain domain);
    SysDeptDomain createSysDeptDomain(SysDeptEntity entity);

    SysDictEntity createSysDictEntity(SysDictDomain domain);
    SysDictDomain createSysDictDomain(SysDictEntity entity);

    SysMenuEntity createSysMenuEntity(SysMenuDomain domain);
    SysMenuDomain createSysMenuDomain(SysMenuEntity entity);

    SysRoleEntity createSysRoleEntity(SysRoleDomain domain);
    SysRoleDomain createSysRoleDomain(SysRoleEntity entity);

    SysDictItemEntity createSysDictItemEntity(SysDictItemDomain domain);
    SysDictItemDomain createSysDictItemDomain(SysDictItemEntity entity);

    GenColumnBindEntity createGenColumnBindEntity(GenColumnBindDomain domain);
    GenColumnBindDomain createGenColumnBindDomain(GenColumnBindEntity entity);

    GenColumnsEntity createGenColumnsEntity(GenColumnsDomain domain);
    GenColumnsDomain createGenColumnsDomain(GenColumnsEntity entity);

    GenProjectInfoEntity createGenProjectInfoEntity(GenProjectInfoDomain domain);
    GenProjectInfoDomain createGenProjectInfoDomain(GenProjectInfoEntity entity);

    GenTemplateEntity createGenTemplateEntity(GenTemplateDomain domain);
    GenTemplateDomain createGenTemplateDomain(GenTemplateEntity entity);
}
