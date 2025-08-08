package com.springddd.domain.gen;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GenColumnBindDomain extends AbstractDomainMask {

    private ColumnBindId bindId;

    private GenColumnBindBasicInfo basicInfo;
}
