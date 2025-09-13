package com.springddd.application.service.gen;

import com.springddd.domain.gen.*;
import org.springframework.stereotype.Component;

@Component
public class GenColumnsDomainFactoryImpl implements GenColumnsDomainFactory {
    @Override
    public GenColumnsDomain newInstance(InfoId infoId, GenColumnsProp prop, GenColumnsExtendInfo extendInfo) {
        GenColumnsDomain domain = new GenColumnsDomain();
        domain.setInfoId(infoId);
        domain.setProp(prop);
        domain.setExtendInfo(extendInfo);
        domain.setDeleteStatus(false);
        return domain;
    }
}
