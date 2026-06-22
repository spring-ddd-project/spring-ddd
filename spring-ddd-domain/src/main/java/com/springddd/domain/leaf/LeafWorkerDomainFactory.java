package com.springddd.domain.leaf;

public interface LeafWorkerDomainFactory {

    LeafWorkerDomain newInstance(Worker worker, Address address, ExtendInfo extendInfo);
}