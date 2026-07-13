package com.springddd.domain.post;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostBasicInfoTest {

    @Test
    void accessorsShouldWork() {
        var postCodeValue = "test";
        var postNameValue = "test";
        PostBasicInfo rec = new PostBasicInfo(postCodeValue, postNameValue);
        assertEquals(postCodeValue, rec.postCode());
        assertEquals(postNameValue, rec.postName());
    }

    @Test
    void equalsAndHashCodeAndToStringShouldWork() {
        var postCodeValue = "test";
        var postNameValue = "test";
        PostBasicInfo a = new PostBasicInfo(postCodeValue, postNameValue);
        PostBasicInfo b = new PostBasicInfo(postCodeValue, postNameValue);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotNull(a.toString());
    }
}
