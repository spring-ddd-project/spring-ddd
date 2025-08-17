package com.springddd.domain.gen;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GenAggregateDomain extends AbstractDomainMask {

    private AggregateId aggregateId;

    private GenProjectInfoId infoId;

    private GenAggregateBasicInfo basicInfo;

    private GenAggregateExtendInfo extendInfo;

    public void create() {}

    public void update(GenProjectInfoId infoId, GenAggregateBasicInfo basicInfo, GenAggregateExtendInfo extendInfo) {
        this.infoId = infoId;
        this.basicInfo = basicInfo;
        this.extendInfo = extendInfo;
    }
}
