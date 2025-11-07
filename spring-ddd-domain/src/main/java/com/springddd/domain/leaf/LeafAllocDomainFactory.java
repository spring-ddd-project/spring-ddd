package com.springddd.domain.leaf;

public interface LeafAllocDomainFactory {

    LeafAllocDomain newInstance(LeafProp leafProp, ExtendInfo extendInfo);
}