package com.springddd.domain.leaf.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeletedLeafAllocStateTest {

    @Test
    void isActive_shouldReturnFalse() {
        DeletedLeafAllocState state = new DeletedLeafAllocState();
        assertFalse(state.isActive());
    }
}
