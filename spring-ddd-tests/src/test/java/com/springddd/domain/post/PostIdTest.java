package com.springddd.domain.post;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostIdTest {

    @Test
    void accessorsShouldWork() {
        var valueValue = 1L;
        PostId rec = new PostId(valueValue);
        assertEquals(valueValue, rec.value());
    }

    @Test
    void equalsAndHashCodeAndToStringShouldWork() {
        var valueValue = 1L;
        PostId a = new PostId(valueValue);
        PostId b = new PostId(valueValue);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotNull(a.toString());
    }
}
