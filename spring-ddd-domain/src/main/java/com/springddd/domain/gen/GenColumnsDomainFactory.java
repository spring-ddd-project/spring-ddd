package com.springddd.domain.gen;

public interface GenColumnsDomainFactory {

    GenColumnsDomain newInstance(GenProjectInfoId infoId, GenColumnsBasicInfo basicInfo, GenColumnsExtendInfo extendInfo);
}
