package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.*;
import org.springframework.stereotype.Component;

@Component
public class LeafAllocDomainFactoryImpl implements LeafAllocDomainFactory {
    @Override
    public LeafAllocDomain newInstance(LeafProp leafProp, ExtendInfo extendInfo) {
        LeafAllocDomain domain = new LeafAllocDomain();
        domain.setLeafProp(leafProp);
        domain.setExtendInfo(extendInfo);
        return domain;
    }
}
