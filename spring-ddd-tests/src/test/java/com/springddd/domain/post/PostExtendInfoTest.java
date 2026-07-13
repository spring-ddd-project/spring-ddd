package com.springddd.domain.post;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostExtendInfoTest {

    @Test
    void accessorsShouldWork() {
        var parentIdValue = 1L;
        var sortOrderValue = 1;
        var postStatusValue = true;
        PostExtendInfo rec = new PostExtendInfo(parentIdValue, sortOrderValue, postStatusValue);
        assertEquals(parentIdValue, rec.parentId());
        assertEquals(sortOrderValue, rec.sortOrder());
        assertEquals(postStatusValue, rec.postStatus());
    }

    @Test
    void equalsAndHashCodeAndToStringShouldWork() {
        var parentIdValue = 1L;
        var sortOrderValue = 1;
        var postStatusValue = true;
        PostExtendInfo a = new PostExtendInfo(parentIdValue, sortOrderValue, postStatusValue);
        PostExtendInfo b = new PostExtendInfo(parentIdValue, sortOrderValue, postStatusValue);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotNull(a.toString());
    }
}
