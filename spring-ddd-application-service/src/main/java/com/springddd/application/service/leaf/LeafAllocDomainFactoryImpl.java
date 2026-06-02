package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.*;
import org.springframework.stereotype.Component;

@Component
public class LeafAllocDomainFactoryImpl implements LeafAllocDomainFactory {

    @Override
    public LeafAllocDomain newInstance(BizTag bizTag, MaxId maxId, Step step, Description description, Long deptId) {
        LeafAllocDomain domain = new LeafAllocDomain();
        domain.setBizTag(bizTag);
        domain.setMaxId(maxId);
        domain.setStep(step);
        domain.setDescription(description);
        domain.setDeptId(deptId);
        domain.create();
        return domain;
    }
}
