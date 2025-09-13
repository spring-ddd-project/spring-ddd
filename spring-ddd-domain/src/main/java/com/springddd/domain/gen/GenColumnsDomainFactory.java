package com.springddd.domain.gen;

public interface GenColumnsDomainFactory {

    GenColumnsDomain newInstance(InfoId infoId, GenColumnsProp basicInfo, GenColumnsExtendInfo extendInfo);
}
