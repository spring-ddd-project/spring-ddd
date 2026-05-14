package com.springddd.domain.leaf.state;

import com.springddd.domain.leaf.LeafAllocDomain;
import com.springddd.domain.leaf.LeafAllocState;

public class ActiveLeafAllocState implements LeafAllocState {
    @Override
    public void delete(LeafAllocDomain domain) {
        domain.setDeleteStatus(true);
        domain.setState(new DeletedLeafAllocState());
    }

    @Override
    public void restore(LeafAllocDomain domain) {
        // Already active
    }
}
