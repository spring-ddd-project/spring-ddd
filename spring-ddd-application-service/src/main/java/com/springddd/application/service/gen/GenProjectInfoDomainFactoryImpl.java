package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenProjectInfoDomain;
import com.springddd.domain.gen.GenProjectInfoDomainFactory;
import com.springddd.domain.gen.GenProjectInfoExtendInfo;
import com.springddd.domain.gen.ProjectInfo;
import org.springframework.stereotype.Component;

@Component
public class GenProjectInfoDomainFactoryImpl implements GenProjectInfoDomainFactory {
    @Override
    public GenProjectInfoDomain newInstance(ProjectInfo projectInfo, GenProjectInfoExtendInfo extendInfo) {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.setProjectInfo(projectInfo);
        domain.setExtendInfo(extendInfo);
        domain.setDeleteStatus(false);
        return domain;
    }
}
