package com.springddd.domain.gen;

public interface GenColumnsDomainFactory {

    GenColumnsDomain newInstance(InfoId infoId, Prop basicInfo, GenColumnsExtendInfo extendInfo);
}
