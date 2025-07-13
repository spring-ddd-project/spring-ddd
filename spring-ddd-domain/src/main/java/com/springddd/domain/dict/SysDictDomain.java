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

    public void create() {}

    public void update(DictBasicInfo basicInfo, DictExtendInfo extendInfo) {
        this.setDictBasicInfo(basicInfo);
        this.setDictExtendInfo(extendInfo);
    }

    public void delete() {
        super.setDeleteStatus(true);
    }

    public void restore() {
        super.setDeleteStatus(false);
    }
}
