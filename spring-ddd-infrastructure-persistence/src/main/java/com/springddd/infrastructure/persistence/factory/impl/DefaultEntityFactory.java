package com.springddd.infrastructure.persistence.factory.impl;

import com.springddd.domain.dept.*;
import com.springddd.domain.dict.*;
import com.springddd.domain.gen.*;
import com.springddd.domain.user.*;
import com.springddd.infrastructure.persistence.entity.GenAggregateEntity;
import com.springddd.infrastructure.persistence.entity.SysDeptEntity;
import com.springddd.infrastructure.persistence.entity.SysDictEntity;
import com.springddd.infrastructure.persistence.entity.SysUserEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DefaultEntityFactory implements EntityFactory {

    @Override
    public GenAggregateEntity createGenAggregateEntity(GenAggregateDomain domain) {
        GenAggregateEntity entity = new GenAggregateEntity();
        entity.setId(Optional.ofNullable(domain.getAggregateId()).map(AggregateId::value).orElse(null));
        entity.setInfoId(domain.getInfoId().value());
        entity.setObjectName(domain.getValueObject().objectName());
        entity.setObjectValue(domain.getValueObject().objectValue());
        entity.setObjectType(domain.getValueObject().objectType());
        entity.setHasCreated(domain.getExtendInfo().hasCreated());
        entity.setCreateBy(domain.getCreateBy());
        entity.setCreateTime(domain.getCreateTime());
        entity.setUpdateBy(domain.getUpdateBy());
        entity.setUpdateTime(domain.getUpdateTime());
        entity.setVersion(domain.getVersion());
        return entity;
    }

    @Override
    public GenAggregateDomain createGenAggregateDomain(GenAggregateEntity entity) {
        GenAggregateDomain domain = new GenAggregateDomain();
        domain.setAggregateId(new AggregateId(entity.getId()));
        domain.setInfoId(new InfoId(entity.getInfoId()));
        domain.setValueObject(new GenAggregateValueObject(entity.getObjectName(), entity.getObjectValue(), entity.getObjectType()));
        domain.setExtendInfo(new GenAggregateExtendInfo(entity.getHasCreated()));
        domain.setCreateBy(entity.getCreateBy());
        domain.setCreateTime(entity.getCreateTime());
        domain.setUpdateBy(entity.getUpdateBy());
        domain.setUpdateTime(entity.getUpdateTime());
        domain.setVersion(entity.getVersion());
        return domain;
    }

    @Override
    public SysUserEntity createSysUserEntity(SysUserDomain domain) {
        SysUserEntity entity = new SysUserEntity();
        entity.setId(Optional.ofNullable(domain.getUserId()).map(UserId::value).orElse(null));
        
        if (domain.getAccount() != null) {
            entity.setUserName(domain.getAccount().getUsername() != null ? domain.getAccount().getUsername().value() : null);
            entity.setPassword(domain.getAccount().getPassword() != null ? domain.getAccount().getPassword().value() : null);
            entity.setPhone(domain.getAccount().getPhone());
            entity.setEmail(domain.getAccount().getEmail());
            entity.setLockStatus(domain.getAccount().getLockStatus());
        }
        
        if (domain.getExtendInfo() != null) {
            entity.setAvatar(domain.getExtendInfo().getAvatar());
            entity.setSex(domain.getExtendInfo().getSex());
        }

        entity.setDeptId(domain.getDeptId());
        entity.setDeleteStatus(domain.getDeleteStatus());
        entity.setCreateBy(domain.getCreateBy());
        entity.setCreateTime(domain.getCreateTime());
        entity.setUpdateBy(domain.getUpdateBy());
        entity.setUpdateTime(domain.getUpdateTime());
        entity.setVersion(domain.getVersion());
        return entity;
    }

    @Override
    public SysUserDomain createSysUserDomain(SysUserEntity entity) {
        SysUserDomain domain = new SysUserDomain();
        domain.setUserId(new UserId(entity.getId()));
        
        Account account = new Account();
        account.setUsername(new Username(entity.getUserName()));
        account.setPassword(new Password(entity.getPassword()));
        account.setPhone(entity.getPhone());
        account.setEmail(entity.getEmail());
        account.setLockStatus(entity.getLockStatus());
        domain.setAccount(account);
        
        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar(entity.getAvatar());
        extendInfo.setSex(entity.getSex());
        domain.setExtendInfo(extendInfo);
        
        domain.setDeptId(entity.getDeptId());
        domain.setDeleteStatus(entity.getDeleteStatus());
        domain.setCreateBy(entity.getCreateBy());
        domain.setCreateTime(entity.getCreateTime());
        domain.setUpdateBy(entity.getUpdateBy());
        domain.setUpdateTime(entity.getUpdateTime());
        domain.setVersion(entity.getVersion());
        return domain;
    @Override
    public SysDeptEntity createSysDeptEntity(SysDeptDomain domain) {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setId(Optional.ofNullable(domain.getId()).map(DeptId::value).orElse(null));
        entity.setParentId(Optional.ofNullable(domain.getParentId()).map(DeptId::value).orElse(null));
        if (domain.getDeptBasicInfo() != null) {
            entity.setDeptName(domain.getDeptBasicInfo().deptName());
        }
        if (domain.getDeptExtendInfo() != null) {
            entity.setSortOrder(domain.getDeptExtendInfo().sortOrder());
            entity.setDeptStatus(domain.getDeptExtendInfo().deptStatus());
        }
        entity.setDeleteStatus(domain.getDeleteStatus());
        entity.setCreateBy(domain.getCreateBy());
        entity.setCreateTime(domain.getCreateTime());
        entity.setUpdateBy(domain.getUpdateBy());
        entity.setUpdateTime(domain.getUpdateTime());
        entity.setVersion(domain.getVersion());
        return entity;
    }

    @Override
    public SysDeptDomain createSysDeptDomain(SysDeptEntity entity) {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setId(new DeptId(entity.getId()));
        domain.setParentId(new DeptId(entity.getParentId()));
        domain.setDeptBasicInfo(new DeptBasicInfo(entity.getDeptName()));
        domain.setDeptExtendInfo(new DeptExtendInfo(entity.getSortOrder(), entity.getDeptStatus()));
        domain.setDeleteStatus(entity.getDeleteStatus());
        domain.setCreateBy(entity.getCreateBy());
        domain.setCreateTime(entity.getCreateTime());
        domain.setUpdateBy(entity.getUpdateBy());
        domain.setUpdateTime(entity.getUpdateTime());
        domain.setVersion(entity.getVersion());
        return domain;
    @Override
    public SysDictEntity createSysDictEntity(SysDictDomain domain) {
        SysDictEntity entity = new SysDictEntity();
        entity.setId(Optional.ofNullable(domain.getDictId()).map(DictId::value).orElse(null));
        if (domain.getDictBasicInfo() != null) {
            entity.setDictName(domain.getDictBasicInfo().dictName());
            entity.setDictCode(domain.getDictBasicInfo().dictCode());
        }
        if (domain.getDictExtendInfo() != null) {
            entity.setSortOrder(domain.getDictExtendInfo().sortOrder());
            entity.setDictStatus(domain.getDictExtendInfo().dictStatus());
        }
        entity.setDeleteStatus(domain.getDeleteStatus());
        entity.setVersion(domain.getVersion());
        entity.setCreateBy(domain.getCreateBy());
        entity.setCreateTime(domain.getCreateTime());
        entity.setUpdateBy(domain.getUpdateBy());
        entity.setUpdateTime(domain.getUpdateTime());
        return entity;
    }

    @Override
    public SysDictDomain createSysDictDomain(SysDictEntity entity) {
        SysDictDomain domain = new SysDictDomain();
        domain.setDictId(new DictId(entity.getId()));
        domain.setDictBasicInfo(new DictBasicInfo(entity.getDictName(), entity.getDictCode()));
        domain.setDictExtendInfo(new DictExtendInfo(entity.getSortOrder(), entity.getDictStatus()));
        domain.setDeleteStatus(entity.getDeleteStatus());
        domain.setVersion(entity.getVersion());
        domain.setCreateBy(entity.getCreateBy());
        domain.setCreateTime(entity.getCreateTime());
        domain.setUpdateBy(entity.getUpdateBy());
        domain.setUpdateTime(entity.getUpdateTime());
        return domain;
    }
}
