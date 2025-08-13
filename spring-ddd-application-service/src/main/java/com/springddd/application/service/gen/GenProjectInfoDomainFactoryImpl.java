package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenProjectInfoBasicInfo;
import com.springddd.domain.gen.GenProjectInfoDomain;
import com.springddd.domain.gen.GenProjectInfoDomainFactory;
import com.springddd.domain.gen.GenProjectInfoExtendInfo;
import org.springframework.stereotype.Component;

@Component
public class GenProjectInfoDomainFactoryImpl implements GenProjectInfoDomainFactory {
    @Override
    public GenProjectInfoDomain newInstance(GenProjectInfoBasicInfo basicInfo, GenProjectInfoExtendInfo extendInfo) {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.setBasicInfo(basicInfo);
        domain.setExtendInfo(extendInfo);
        domain.setDeleteStatus(false);
        return domain;
    }
}
