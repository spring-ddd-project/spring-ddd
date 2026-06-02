package com.springddd.domain.leaf;

import com.springddd.domain.AbstractDomainMask;
import com.springddd.domain.leaf.state.ActiveLeafAllocState;
import com.springddd.domain.leaf.state.DeletedLeafAllocState;
import com.springddd.domain.leaf.state.LeafAllocState;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LeafAllocDomain extends AbstractDomainMask {

    private LeafAllocId leafAllocId;

    private BizTag bizTag;

    private MaxId maxId;

    private Step step;

    private Description description;

    private LeafAllocState state;

    public void create() {
        this.state = new ActiveLeafAllocState();
        super.setDeleteStatus(false);
    }

    public void update(BizTag newBizTag, MaxId newMaxId, Step newStep, Description newDescription, Long deptId) {
        this.bizTag = newBizTag;
        this.maxId = newMaxId;
        this.step = newStep;
        this.description = newDescription;
        super.setDeptId(deptId);
    }

    public void updateMaxId(MaxId newMaxId) {
        this.maxId = newMaxId;
    }

    public void updateStep(Step newStep) {
        this.step = newStep;
    }

    public void delete() {
        super.setDeleteStatus(true);
        this.state = new DeletedLeafAllocState();
    }

    public void restore() {
        super.setDeleteStatus(false);
        this.state = new ActiveLeafAllocState();
    }
}
