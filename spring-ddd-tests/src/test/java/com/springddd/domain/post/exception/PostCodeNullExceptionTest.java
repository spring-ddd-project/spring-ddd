package com.springddd.domain.post.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostCodeNullExceptionTest {

    @Test
    void constructorShouldWork() {
        PostCodeNullException ex = new PostCodeNullException();
        assertNotNull(ex.getMessage());
    }
}
