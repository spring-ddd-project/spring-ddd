package com.springddd.application.service.post.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysPostCommandTest {

    @Test
    void setIdAndgetIdShouldWork() {
        SysPostCommand entity = new SysPostCommand();
        entity.setId(1L);
        assertEquals(1L, entity.getId());
    }

    @Test
    void setPostCodeAndgetPostCodeShouldWork() {
        SysPostCommand entity = new SysPostCommand();
        entity.setPostCode("test");
        assertEquals("test", entity.getPostCode());
    }

    @Test
    void setPostNameAndgetPostNameShouldWork() {
        SysPostCommand entity = new SysPostCommand();
        entity.setPostName("test");
        assertEquals("test", entity.getPostName());
    }

    @Test
    void setParentIdAndgetParentIdShouldWork() {
        SysPostCommand entity = new SysPostCommand();
        entity.setParentId(1L);
        assertEquals(1L, entity.getParentId());
    }

    @Test
    void setSortOrderAndgetSortOrderShouldWork() {
        SysPostCommand entity = new SysPostCommand();
        entity.setSortOrder(1);
        assertEquals(1, entity.getSortOrder());
    }

    @Test
    void setPostStatusAndgetPostStatusShouldWork() {
        SysPostCommand entity = new SysPostCommand();
        entity.setPostStatus(true);
        assertEquals(true, entity.getPostStatus());
    }

    @Test
    void setDeleteStatusAndgetDeleteStatusShouldWork() {
        SysPostCommand entity = new SysPostCommand();
        entity.setDeleteStatus(true);
        assertEquals(true, entity.getDeleteStatus());
    }

    @Test
    void equalsAndHashCodeAndToStringShouldWork() {
        SysPostCommand a = new SysPostCommand();
        SysPostCommand b = new SysPostCommand();
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotNull(a.toString());
    }
}
