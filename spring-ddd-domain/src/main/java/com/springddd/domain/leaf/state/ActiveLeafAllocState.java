package com.springddd.domain.leaf.state;

public class ActiveLeafAllocState implements LeafAllocState {

    @Override
    public boolean isActive() {
        return true;
    }
}
