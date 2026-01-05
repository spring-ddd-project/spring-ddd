package com.springddd.domain.dict;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictDomain extends AbstractDomainMask {

    private DictId dictId;

    private DictBasicInfo dictBasicInfo;

    private DictExtendInfo dictExtendInfo;
}
