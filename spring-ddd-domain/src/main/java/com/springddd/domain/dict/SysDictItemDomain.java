package com.springddd.domain.dict;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictItemDomain extends AbstractDomainMask {

    private DictId itemId;

    private DictId dictId;

    private DictItemBasicInfo itemBasicInfo;

    private DictItemExtendInfo itemExtendInfo;
}
