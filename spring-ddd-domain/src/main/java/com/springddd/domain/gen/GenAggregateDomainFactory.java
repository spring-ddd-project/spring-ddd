package com.springddd.domain.gen;

public interface GenAggregateDomainFactory {

    GenAggregateDomain newInstance(GenProjectInfoId infoId, GenAggregateValueObject valueObject, GenAggregateExtendInfo extendInfo);
}
