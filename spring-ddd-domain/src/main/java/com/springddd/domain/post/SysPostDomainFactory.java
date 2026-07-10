package com.springddd.domain.post;

public interface SysPostDomainFactory {

    SysPostDomain newInstance(PostBasicInfo basicInfo, PostExtendInfo extendInfo);
}
