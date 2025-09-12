package com.springddd.domain.gen;

public interface GenColumnsDomainFactory {

    GenColumnsDomain newInstance(GenProjectInfoId infoId, GenColumnsProp basicInfo, GenColumnsExtendInfo extendInfo);
}
