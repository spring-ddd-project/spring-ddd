package com.springddd.domain.dict.memento;

import com.springddd.domain.dict.DictItemBasicInfo;
import com.springddd.domain.dict.DictItemExtendInfo;
import lombok.Getter;

@Getter
public class SysDictItemMemento {
    private final DictItemBasicInfo itemBasicInfo;
    private final DictItemExtendInfo itemExtendInfo;

    public SysDictItemMemento(DictItemBasicInfo itemBasicInfo, DictItemExtendInfo itemExtendInfo) {
        this.itemBasicInfo = itemBasicInfo;
        this.itemExtendInfo = itemExtendInfo;
    }
}
















































