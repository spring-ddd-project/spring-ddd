package com.springddd.domain.leaf.state;

public class DeletedLeafAllocState implements LeafAllocState {

    @Override
    public boolean isActive() {
        return false;
    }
}
