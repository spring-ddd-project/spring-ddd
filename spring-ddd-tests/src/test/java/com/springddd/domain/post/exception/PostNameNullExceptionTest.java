package com.springddd.domain.post.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostNameNullExceptionTest {

    @Test
    void constructorShouldWork() {
        PostNameNullException ex = new PostNameNullException();
        assertNotNull(ex.getMessage());
    }
}
