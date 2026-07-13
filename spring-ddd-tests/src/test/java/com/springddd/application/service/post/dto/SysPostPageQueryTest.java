package com.springddd.application.service.post.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysPostPageQueryTest {

    @Test
    void setPageNumAndgetPageNumShouldWork() {
        SysPostPageQuery entity = new SysPostPageQuery();
        entity.setPageNum(1);
        assertEquals(1, entity.getPageNum());
    }

    @Test
    void setPageSizeAndgetPageSizeShouldWork() {
        SysPostPageQuery entity = new SysPostPageQuery();
        entity.setPageSize(1);
        assertEquals(1, entity.getPageSize());
    }

    @Test
    void equalsAndHashCodeAndToStringShouldWork() {
        SysPostPageQuery a = new SysPostPageQuery();
        SysPostPageQuery b = new SysPostPageQuery();
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotNull(a.toString());
    }
}
