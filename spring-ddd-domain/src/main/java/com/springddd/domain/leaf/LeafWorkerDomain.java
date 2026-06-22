package com.springddd.domain.leaf;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LeafWorkerDomain extends AbstractDomainMask {

    private LeafId leafId;

    private Worker worker;

    private Address address;

    private ExtendInfo extendInfo;

    public void create() {}

    public void update(Worker worker, Address address, ExtendInfo extendInfo) {
        this.worker = worker;
        this.address = address;
        this.extendInfo = extendInfo;
    }

    public void delete() {
        super.setDeleteStatus(true);
    }

    public void restore() {
        super.setDeleteStatus(false);
    }
}
