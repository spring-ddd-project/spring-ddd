package com.springddd.domain.leaf.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActiveLeafAllocStateTest {

    @Test
    void isActive_shouldReturnTrue() {
        ActiveLeafAllocState state = new ActiveLeafAllocState();
        assertTrue(state.isActive());
    }
}
