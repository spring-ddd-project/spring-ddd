package com.springddd.domain.gen;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GenColumnsDomain extends AbstractDomainMask {

    private GenColumnsId id;

    private GenInfoId infoId;

    private GenColumnsBasicInfo basicInfo;

    private GenColumnsExtendInfo extendInfo;
}
