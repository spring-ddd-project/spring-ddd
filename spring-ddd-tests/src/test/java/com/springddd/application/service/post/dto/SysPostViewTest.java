package com.springddd.application.service.post.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysPostViewTest {

    @Test
    void setIdAndgetIdShouldWork() {
        SysPostView entity = new SysPostView();
        entity.setId(1L);
        assertEquals(1L, entity.getId());
    }

    @Test
    void setPostCodeAndgetPostCodeShouldWork() {
        SysPostView entity = new SysPostView();
        entity.setPostCode("test");
        assertEquals("test", entity.getPostCode());
    }

    @Test
    void setPostNameAndgetPostNameShouldWork() {
        SysPostView entity = new SysPostView();
        entity.setPostName("test");
        assertEquals("test", entity.getPostName());
    }

    @Test
    void setParentIdAndgetParentIdShouldWork() {
        SysPostView entity = new SysPostView();
        entity.setParentId(1L);
        assertEquals(1L, entity.getParentId());
    }

    @Test
    void setSortOrderAndgetSortOrderShouldWork() {
        SysPostView entity = new SysPostView();
        entity.setSortOrder(1);
        assertEquals(1, entity.getSortOrder());
    }

    @Test
    void setPostStatusAndgetPostStatusShouldWork() {
        SysPostView entity = new SysPostView();
        entity.setPostStatus(true);
        assertEquals(true, entity.getPostStatus());
    }

    @Test
    void setDeleteStatusAndgetDeleteStatusShouldWork() {
        SysPostView entity = new SysPostView();
        entity.setDeleteStatus(true);
        assertEquals(true, entity.getDeleteStatus());
    }

    @Test
    void equalsAndHashCodeAndToStringShouldWork() {
        SysPostView a = new SysPostView();
        SysPostView b = new SysPostView();
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotNull(a.toString());
    }
}
