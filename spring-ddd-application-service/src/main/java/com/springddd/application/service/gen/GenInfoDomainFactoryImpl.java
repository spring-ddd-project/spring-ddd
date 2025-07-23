package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenInfoBasicInfo;
import com.springddd.domain.gen.GenInfoDomain;
import com.springddd.domain.gen.GenInfoDomainFactory;
import com.springddd.domain.gen.GenInfoExtendInfo;
import org.springframework.stereotype.Component;

@Component
public class GenInfoDomainFactoryImpl implements GenInfoDomainFactory {
    @Override
    public GenInfoDomain newInstance(GenInfoBasicInfo basicInfo, GenInfoExtendInfo extendInfo) {
        GenInfoDomain domain = new GenInfoDomain();
        domain.setBasicInfo(basicInfo);
        domain.setExtendInfo(extendInfo);
        domain.setDeleteStatus(false);
        return domain;
    }
}
