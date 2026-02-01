package com.springddd.application.service.gen;

import com.springddd.domain.gen.*;
import org.springframework.stereotype.Component;

@Component
public class GenColumnsDomainFactoryImpl implements GenColumnsDomainFactory {
    @Override
    public GenColumnsDomain newInstance(GenProjectInfoId infoId, GenColumnsBasicInfo basicInfo, GenColumnsExtendInfo extendInfo) {
        GenColumnsDomain domain = new GenColumnsDomain();
        domain.setInfoId(infoId);
        domain.setBasicInfo(basicInfo);
        domain.setExtendInfo(extendInfo);
        domain.setDeleteStatus(false);
        return domain;
    }
}
