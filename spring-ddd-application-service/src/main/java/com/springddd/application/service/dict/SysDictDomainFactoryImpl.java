package com.springddd.application.service.dict;

import com.springddd.domain.dict.DictBasicInfo;
import com.springddd.domain.dict.DictExtendInfo;
import com.springddd.domain.dict.SysDictDomain;
import com.springddd.domain.dict.SysDictDomainFactory;
import org.springframework.stereotype.Component;

@Component
public class SysDictDomainFactoryImpl implements SysDictDomainFactory {
    @Override
    public SysDictDomain newInstance(DictBasicInfo dictBasicInfo, DictExtendInfo dictExtendInfo) {
        SysDictDomain sysDictDomain = new SysDictDomain();
        sysDictDomain.setDictBasicInfo(dictBasicInfo);
        sysDictDomain.setDictExtendInfo(dictExtendInfo);

        sysDictDomain.setDeleteStatus(false);
        return sysDictDomain;
    }
}
