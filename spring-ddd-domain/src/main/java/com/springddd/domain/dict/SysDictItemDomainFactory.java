package com.springddd.domain.dict;

public interface SysDictItemDomainFactory {

    SysDictItemDomain newInstance(DictId dictId, DictItemBasicInfo basicInfo, DictItemExtendInfo extendInfo);
}
