package com.springddd.domain.leaf;

public interface LeafAllocState {
    void delete(LeafAllocDomain domain);
    void restore(LeafAllocDomain domain);
}
