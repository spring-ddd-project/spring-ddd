package com.springddd.domain.dict.state;

import com.springddd.domain.dict.DictItemBasicInfo;
import com.springddd.domain.dict.SysDictItemDomain;

public class EnabledDictItemState implements DictItemState {
    @Override
    public void enable(SysDictItemDomain domain) {
        // Already enabled
    }

    @Override
    public void disable(SysDictItemDomain domain) {
        DictItemBasicInfo old = domain.getItemBasicInfo();
        domain.setItemBasicInfo(new DictItemBasicInfo(old.itemLabel(), old.itemValue(), false));
        domain.setState(new DisabledDictItemState());
    }

    @Override
    public void restore(SysDictItemDomain domain) {
        domain.setDeleteStatus(false);
    }
}
























































