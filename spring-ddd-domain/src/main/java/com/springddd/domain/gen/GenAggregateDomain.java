package com.springddd.domain.gen;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GenAggregateDomain extends AbstractDomainMask {

    private AggregateId aggregateId;

    private GenProjectInfoId infoId;

    private GenAggregateValueObject valueObject;

    private GenAggregateExtendInfo extendInfo;

    public void create() {}

    public void update(GenProjectInfoId infoId, GenAggregateValueObject valueObject, GenAggregateExtendInfo extendInfo) {
        this.infoId = infoId;
        this.valueObject = valueObject;
        this.extendInfo = extendInfo;
    }
}
