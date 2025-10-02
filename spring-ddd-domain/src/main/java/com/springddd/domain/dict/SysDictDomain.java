package com.springddd.domain.dict;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictDomain extends AbstractDomainMask {

    private DictId dictId;

    private DictBasicInfo dictBasicInfo;

    private DictExtendInfo dictExtendInfo;

    private final java.util.List<com.springddd.domain.dict.observer.DictObserver> observers = new java.util.ArrayList<>();

    public void addObserver(com.springddd.domain.dict.observer.DictObserver observer) {
        observers.add(observer);
    }

    public void create() {}

    public void update(DictBasicInfo basicInfo, DictExtendInfo extendInfo) {
        this.setDictBasicInfo(basicInfo);
        this.setDictExtendInfo(extendInfo);
        observers.forEach(o -> o.onUpdate(this));
    }

    public void delete() {
        super.setDeleteStatus(true);
    }

    public void restore() {
        super.setDeleteStatus(false);
    }
}
