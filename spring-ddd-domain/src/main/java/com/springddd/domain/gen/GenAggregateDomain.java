package com.springddd.domain.gen;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GenAggregateDomain extends AbstractDomainMask {

    private AggregateId aggregateId;

    private GenAggregateBasicInfo basicInfo;

    private GenAggregateExtendInfo extendInfo;
}
