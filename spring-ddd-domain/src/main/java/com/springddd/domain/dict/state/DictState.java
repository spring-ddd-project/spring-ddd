package com.springddd.domain.dict.state;

import com.springddd.domain.dict.SysDictDomain;

public interface DictState {
    void delete(SysDictDomain domain);
    void restore(SysDictDomain domain);
}















