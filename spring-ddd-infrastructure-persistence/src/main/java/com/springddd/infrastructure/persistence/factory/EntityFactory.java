package com.springddd.infrastructure.persistence.factory;

import com.springddd.domain.dept.SysDeptDomain;
import com.springddd.domain.dict.SysDictDomain;
import com.springddd.domain.gen.GenAggregateDomain;
import com.springddd.domain.user.SysUserDomain;
import com.springddd.infrastructure.persistence.entity.GenAggregateEntity;
import com.springddd.infrastructure.persistence.entity.SysDeptEntity;
import com.springddd.infrastructure.persistence.entity.SysDictEntity;
import com.springddd.infrastructure.persistence.entity.SysUserEntity;

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
}
