package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenColumnBindBasicInfo;
import com.springddd.domain.gen.GenColumnBindDomain;
import com.springddd.domain.gen.GenColumnBindDomainFactory;
import org.springframework.stereotype.Component;

@Component
public class GenColumnBindDomainFactoryImpl implements GenColumnBindDomainFactory {
    @Override
    public GenColumnBindDomain newInstance(GenColumnBindBasicInfo basicInfo) {
        GenColumnBindDomain genColumnBindDomain = new GenColumnBindDomain();
        genColumnBindDomain.setBasicInfo(basicInfo);
        genColumnBindDomain.setDeleteStatus(false);
        return genColumnBindDomain;
    }
}
