package com.springddd.domain.dict.state;

import com.springddd.domain.dict.SysDictItemDomain;

public class DisabledDictItemState implements DictItemState {
    @Override
    public void enable(SysDictItemDomain domain) {
        domain.getItemBasicInfo().setItemStatus(true);
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














