package com.springddd.domain.post.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostStatusNullExceptionTest {

    @Test
    void constructorShouldWork() {
        PostStatusNullException ex = new PostStatusNullException();
        assertNotNull(ex.getMessage());
    }
}
