package com.springddd.domain.dict.state;

import com.springddd.domain.dict.SysDictDomain;

public class ActiveDictState implements DictState {
    @Override
    public void delete(SysDictDomain domain) {
        domain.setDeleteStatus(true);
        domain.setState(new DeletedDictState());
    }

    @Override
    public void restore(SysDictDomain domain) {
        // Already active
    }
}





























