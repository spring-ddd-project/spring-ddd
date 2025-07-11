package com.springddd.application.service.dict;

import com.springddd.domain.dict.*;
import org.springframework.stereotype.Component;

@Component
public class SysDictItemDomainFactoryImpl implements SysDictItemDomainFactory {

    @Override
    public SysDictItemDomain newInstance(DictId dictId, DictItemBasicInfo basicInfo, DictItemExtendInfo extendInfo) {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setDictId(dictId);
        domain.setItemBasicInfo(basicInfo);
        domain.setItemExtendInfo(extendInfo);
        domain.setDeleteStatus(false);
        return domain;
    }
}
