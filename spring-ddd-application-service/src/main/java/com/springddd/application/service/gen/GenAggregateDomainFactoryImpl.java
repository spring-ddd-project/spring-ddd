package com.springddd.application.service.gen;

import com.springddd.domain.gen.*;
import org.springframework.stereotype.Component;

@Component
public class GenAggregateDomainFactoryImpl implements GenAggregateDomainFactory {

    @Override
    public GenAggregateDomain newInstance(GenProjectInfoId infoId, GenAggregateValueObject valueObject, GenAggregateExtendInfo extendInfo) {
        GenAggregateDomain domain = new GenAggregateDomain();
        domain.setInfoId(infoId);
        domain.setValueObject(valueObject);
        domain.setExtendInfo(extendInfo);
        return domain;
    }
}
