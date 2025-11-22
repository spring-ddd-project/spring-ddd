package com.springddd.domain.dict.state;

import com.springddd.domain.dict.SysDictDomain;

public class DeletedDictState implements DictState {
    @Override
    public void delete(SysDictDomain domain) {
        // Already deleted
    }

    @Override
    public void restore(SysDictDomain domain) {
        domain.setDeleteStatus(false);
        domain.setState(new ActiveDictState());
    }
}




















