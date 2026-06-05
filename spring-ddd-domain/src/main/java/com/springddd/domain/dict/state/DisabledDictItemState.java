package com.springddd.domain.dict.state;

import com.springddd.domain.dict.DictItemBasicInfo;
import com.springddd.domain.dict.SysDictItemDomain;

public class DisabledDictItemState implements DictItemState {
    @Override
    public void enable(SysDictItemDomain domain) {
        DictItemBasicInfo old = domain.getItemBasicInfo();
        if (old != null) {
            DictItemBasicInfo newItemBasicInfo = new DictItemBasicInfo(
                old.itemLabel(),
                old.itemValue()
            );
            domain.setItemBasicInfo(newItemBasicInfo);
        }
        domain.setState(new EnabledDictItemState());
    }

    @Override
    public void disable(SysDictItemDomain domain) {
        // Already disabled
    }

    @Override
    public void restore(SysDictItemDomain domain) {
        domain.setDeleteStatus(false);
    }
}
