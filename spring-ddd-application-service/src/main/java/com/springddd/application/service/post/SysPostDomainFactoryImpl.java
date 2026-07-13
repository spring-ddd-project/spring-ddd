package com.springddd.application.service.post;

import com.springddd.domain.post.*;
import org.springframework.stereotype.Component;

@Component
public class SysPostDomainFactoryImpl implements SysPostDomainFactory {

    @Override
    public SysPostDomain newInstance(PostBasicInfo basicInfo, PostExtendInfo extendInfo) {
        SysPostDomain domain = new SysPostDomain();

        domain.setPostBasicInfo(basicInfo);
        domain.setPostExtendInfo(extendInfo);

        domain.setDeleteStatus(false);
        return domain;
    }
}
