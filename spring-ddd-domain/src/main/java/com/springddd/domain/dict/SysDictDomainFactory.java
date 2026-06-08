package com.springddd.domain.dict;

public interface SysDictDomainFactory {

    SysDictDomain newInstance(DictBasicInfo dictBasicInfo, DictExtendInfo dictExtendInfo);
}
