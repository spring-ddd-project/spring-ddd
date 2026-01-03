package com.springddd.domain.dict;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictItemDomain extends AbstractDomainMask implements Cloneable {

    private DictItemId itemId;

    private DictId dictId;

    private DictItemBasicInfo itemBasicInfo;

    private DictItemExtendInfo itemExtendInfo;

    @Override
    public SysDictItemDomain clone() {
        try {
            SysDictItemDomain clone = (SysDictItemDomain) super.clone();
            if (this.itemId != null) clone.setItemId(new DictItemId(this.itemId.value()));
            if (this.dictId != null) clone.setDictId(new DictId(this.dictId.value()));
            if (this.itemBasicInfo != null) {
                DictItemBasicInfo basic = new DictItemBasicInfo();
                basic.setItemLabel(this.itemBasicInfo.getItemLabel());
                basic.setItemValue(this.itemBasicInfo.getItemValue());
                basic.setItemSort(this.itemBasicInfo.getItemSort());
                basic.setItemStatus(this.itemBasicInfo.getItemStatus());
                clone.setItemBasicInfo(basic);
            }
            if (this.itemExtendInfo != null) {
                DictItemExtendInfo ext = new DictItemExtendInfo();
                ext.setItemRemark(this.itemExtendInfo.getItemRemark());
                clone.setItemExtendInfo(ext);
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    private com.springddd.domain.dict.state.DictItemState state;

    public void setState(com.springddd.domain.dict.state.DictItemState state) {
        this.state = state;
    }

    public void enable() {
        if (state == null) state = itemBasicInfo != null && itemBasicInfo.getItemStatus() ? new com.springddd.domain.dict.state.EnabledDictItemState() : new com.springddd.domain.dict.state.DisabledDictItemState();
        state.enable(this);
    }

    public void disable() {
        if (state == null) state = itemBasicInfo != null && itemBasicInfo.getItemStatus() ? new com.springddd.domain.dict.state.EnabledDictItemState() : new com.springddd.domain.dict.state.DisabledDictItemState();
        state.disable(this);
    }

    public void create() {}

    public void update(DictItemBasicInfo basicInfo, DictItemExtendInfo extendInfo) {
        this.itemBasicInfo = basicInfo;
        this.itemExtendInfo = extendInfo;
    }

    public void delete() {
        super.setDeleteStatus(true);
    }

    public void restore() {
        if (state == null) state = itemBasicInfo != null && itemBasicInfo.getItemStatus() ? new com.springddd.domain.dict.state.EnabledDictItemState() : new com.springddd.domain.dict.state.DisabledDictItemState();
        state.restore(this);
    }

    public com.springddd.domain.dict.memento.SysDictItemMemento saveToMemento() {
        return new com.springddd.domain.dict.memento.SysDictItemMemento(this.itemBasicInfo, this.itemExtendInfo);
    }

    public void restoreFromMemento(com.springddd.domain.dict.memento.SysDictItemMemento memento) {
        this.itemBasicInfo = memento.getItemBasicInfo();
        this.itemExtendInfo = memento.getItemExtendInfo();
    }
}



































