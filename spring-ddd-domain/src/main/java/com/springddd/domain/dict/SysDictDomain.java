package com.springddd.domain.dict;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictDomain extends AbstractDomainMask implements Cloneable {

    private DictId dictId;

    private DictBasicInfo dictBasicInfo;

    private DictExtendInfo dictExtendInfo;

    @Override
    public SysDictDomain clone() {
        try {
            SysDictDomain clone = (SysDictDomain) super.clone();
            if (this.dictId != null) clone.setDictId(new DictId(this.dictId.value()));
            if (this.dictBasicInfo != null) {
                DictBasicInfo basic = new DictBasicInfo();
                basic.setDictName(this.dictBasicInfo.getDictName());
                basic.setDictType(this.dictBasicInfo.getDictType());
                basic.setDictStatus(this.dictBasicInfo.getDictStatus());
                clone.setDictBasicInfo(basic);
            }
            if (this.dictExtendInfo != null) {
                DictExtendInfo ext = new DictExtendInfo();
                ext.setDictRemark(this.dictExtendInfo.getDictRemark());
                clone.setDictExtendInfo(ext);
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

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

    public com.springddd.domain.dict.memento.SysDictMemento saveToMemento() {
        return new com.springddd.domain.dict.memento.SysDictMemento(this.dictBasicInfo, this.dictExtendInfo);
    }

    public void restoreFromMemento(com.springddd.domain.dict.memento.SysDictMemento memento) {
        this.dictBasicInfo = memento.getDictBasicInfo();
        this.dictExtendInfo = memento.getDictExtendInfo();
    }
}














































