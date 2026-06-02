package com.springddd.domain.leaf;

public interface LeafAllocDomainFactory {

    LeafAllocDomain newInstance(BizTag bizTag, MaxId maxId, Step step, Description description, Long deptId);
}
