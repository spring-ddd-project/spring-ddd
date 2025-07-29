package com.springddd.domain.gen;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GenInfoDomain extends AbstractDomainMask {

    private GenInfoId id;

    private GenInfoBasicInfo basicInfo;

    private GenInfoExtendInfo extendInfo;

    public void create(GenInfoExtendInfo extendInfo) {
    }

    public void update(GenInfoBasicInfo basicInfo, GenInfoExtendInfo extendInfo) {
        this.basicInfo = basicInfo;
        this.extendInfo = extendInfo;
    }

    public void delete() {
        super.setDeleteStatus(true);
    }
}
