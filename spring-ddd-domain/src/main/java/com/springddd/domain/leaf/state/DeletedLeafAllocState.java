package com.springddd.domain.leaf.state;

import com.springddd.domain.leaf.LeafAllocDomain;
import com.springddd.domain.leaf.LeafAllocState;

public class DeletedLeafAllocState implements LeafAllocState {
    @Override
    public void delete(LeafAllocDomain domain) {
        // Already deleted
    }

    @Override
    public void restore(LeafAllocDomain domain) {
        domain.setDeleteStatus(false);
        domain.setState(new ActiveLeafAllocState());
    }
}
