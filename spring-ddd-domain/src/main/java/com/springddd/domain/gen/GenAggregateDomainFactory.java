package com.springddd.domain.gen;

public interface GenAggregateDomainFactory {

    GenAggregateDomain newInstance(InfoId infoId, GenAggregateValueObject valueObject, GenAggregateExtendInfo extendInfo);
}
