package com.springddd.domain.dict.memento;

import com.springddd.domain.dict.DictBasicInfo;
import com.springddd.domain.dict.DictExtendInfo;
import lombok.Getter;

@Getter
public class SysDictMemento {
    private final DictBasicInfo dictBasicInfo;
    private final DictExtendInfo dictExtendInfo;

    public SysDictMemento(DictBasicInfo dictBasicInfo, DictExtendInfo dictExtendInfo) {
        this.dictBasicInfo = dictBasicInfo;
        this.dictExtendInfo = dictExtendInfo;
    }
}










