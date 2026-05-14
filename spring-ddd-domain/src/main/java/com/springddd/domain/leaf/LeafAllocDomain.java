package com.springddd.domain.leaf;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LeafAllocDomain extends AbstractDomainMask implements Cloneable {

    private LeafAllocId leafAllocId;

    private Long id;

    private LeafAllocBasicInfo basicInfo;

    private LeafAllocExtendInfo extendInfo;

    private LeafAllocState state;

    public void setState(LeafAllocState state) {
        this.state = state;
    }

    public void create() {
        this.state = new com.springddd.domain.leaf.state.ActiveLeafAllocState();
    }

    public void update(LeafAllocBasicInfo basicInfo, LeafAllocExtendInfo extendInfo) {
        this.basicInfo = basicInfo;
        this.extendInfo = extendInfo;
    }

    public void delete() {
        if (state == null) state = getDeleteStatus() ? new com.springddd.domain.leaf.state.DeletedLeafAllocState() : new com.springddd.domain.leaf.state.ActiveLeafAllocState();
        state.delete(this);
    }

    public void restore() {
        if (state == null) state = getDeleteStatus() ? new com.springddd.domain.leaf.state.DeletedLeafAllocState() : new com.springddd.domain.leaf.state.ActiveLeafAllocState();
        state.restore(this);
    }
}
