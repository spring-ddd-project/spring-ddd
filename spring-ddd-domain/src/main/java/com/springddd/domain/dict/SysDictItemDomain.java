package com.springddd.domain.dict;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictItemDomain extends AbstractDomainMask {

    private DictItemId itemId;

    private DictId dictId;

    private DictItemBasicInfo itemBasicInfo;

    private DictItemExtendInfo itemExtendInfo;

    public void create() {}

    public void update(DictId dictId, DictItemBasicInfo basicInfo, DictItemExtendInfo extendInfo) {
        this.dictId = dictId;
        this.itemBasicInfo = basicInfo;
        this.itemExtendInfo = extendInfo;
    }

    public void delete() {
        super.setDeleteStatus(true);
    }
}
