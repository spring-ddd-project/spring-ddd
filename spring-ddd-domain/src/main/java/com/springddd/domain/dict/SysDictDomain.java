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

    private com.springddd.domain.dict.state.DictState state;

    public void setState(com.springddd.domain.dict.state.DictState state) {
        this.state = state;
    }

    public void addObserver(com.springddd.domain.dict.observer.DictObserver observer) {
        observers.add(observer);
    }

    public void create() {
        this.state = new com.springddd.domain.dict.state.ActiveDictState();
    }

    public void update(DictBasicInfo basicInfo, DictExtendInfo extendInfo) {
        this.setDictBasicInfo(basicInfo);
        this.setDictExtendInfo(extendInfo);
        observers.forEach(o -> o.onUpdate(this));
    }

    public void delete() {
        if (state == null) state = getDeleteStatus() ? new com.springddd.domain.dict.state.DeletedDictState() : new com.springddd.domain.dict.state.ActiveDictState();
        state.delete(this);
    }

    public void restore() {
        if (state == null) state = getDeleteStatus() ? new com.springddd.domain.dict.state.DeletedDictState() : new com.springddd.domain.dict.state.ActiveDictState();
        state.restore(this);
    }
}
