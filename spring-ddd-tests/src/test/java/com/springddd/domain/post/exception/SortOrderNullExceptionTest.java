package com.springddd.domain.post.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SortOrderNullExceptionTest {

    @Test
    void constructorShouldWork() {
        SortOrderNullException ex = new SortOrderNullException();
        assertNotNull(ex.getMessage());
    }
}
