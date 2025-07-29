package com.springddd.domain.gen;

public interface GenColumnsDomainFactory {

    GenColumnsDomain newInstance(GenInfoId infoId, GenColumnsBasicInfo basicInfo, GenColumnsExtendInfo extendInfo);
}
