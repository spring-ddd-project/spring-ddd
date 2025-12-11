package com.springddd.domain.dict.state;

import com.springddd.domain.dict.SysDictItemDomain;

public interface DictItemState {
    void enable(SysDictItemDomain domain);
    void disable(SysDictItemDomain domain);
    void restore(SysDictItemDomain domain);
}





