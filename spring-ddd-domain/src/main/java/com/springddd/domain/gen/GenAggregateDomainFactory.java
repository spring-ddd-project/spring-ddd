package com.springddd.domain.gen;

public interface GenAggregateDomainFactory {

    GenAggregateDomain newInstance(GenProjectInfoId infoId, GenAggregateBasicInfo basicInfo, GenAggregateExtendInfo extendInfo);
}
