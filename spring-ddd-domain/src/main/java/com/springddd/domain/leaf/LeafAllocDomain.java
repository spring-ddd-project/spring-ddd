package com.springddd.domain.leaf;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LeafAllocDomain extends AbstractDomainMask {

    private LeafId leafId;

    private LeafProp leafProp;

    private ExtendInfo extendInfo;

    public void create() {}

    public void update(LeafProp leafProp, ExtendInfo extendInfo) {
        this.leafProp = leafProp;
        this.extendInfo = extendInfo;
    }

    public void delete() {
        super.setDeleteStatus(true);
    }

    public void restore() {
        super.setDeleteStatus(false);
    }
}
