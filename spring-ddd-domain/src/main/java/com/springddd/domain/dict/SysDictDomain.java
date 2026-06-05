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
            SysDictDomain clone = (SysDictDomain) doClone();
            if (this.dictId != null) clone.setDictId(new DictId(this.dictId.value()));
            if (this.dictBasicInfo != null) {
                DictBasicInfo basic = new DictBasicInfo(this.dictBasicInfo.dictName(), this.dictBasicInfo.dictCode());
                clone.setDictBasicInfo(basic);
            }
            if (this.dictExtendInfo != null) {
                DictExtendInfo ext = new DictExtendInfo(this.dictExtendInfo.sortOrder(), this.dictExtendInfo.dictStatus());
                clone.setDictExtendInfo(ext);
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    private com.springddd.domain.dict.state.DictState state;

    public void setState(com.springddd.domain.dict.state.DictState state) {
        this.state = state;
    }

    public void create() {
        this.state = new com.springddd.domain.dict.state.ActiveDictState();
    }

    public void update(DictBasicInfo basicInfo, DictExtendInfo extendInfo) {
        this.setDictBasicInfo(basicInfo);
        this.setDictExtendInfo(extendInfo);
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
