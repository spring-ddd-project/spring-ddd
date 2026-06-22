package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.*;
import org.springframework.stereotype.Component;

@Component
public class LeafWorkerDomainFactoryImpl implements LeafWorkerDomainFactory {

    @Override
    public LeafWorkerDomain newInstance(Worker worker, Address address, ExtendInfo extendInfo) {
        LeafWorkerDomain domain = new LeafWorkerDomain();
        domain.setWorker(worker);
        domain.setAddress(address);
        domain.setExtendInfo(extendInfo);
        return domain;
    }
}
