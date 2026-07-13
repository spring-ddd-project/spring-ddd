package com.springddd.application.service.post.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysPostQueryTest {

    @Test
    void setIdAndgetIdShouldWork() {
        SysPostQuery entity = new SysPostQuery();
        entity.setId(1L);
        assertEquals(1L, entity.getId());
    }

    @Test
    void setParentIdAndgetParentIdShouldWork() {
        SysPostQuery entity = new SysPostQuery();
        entity.setParentId(1L);
        assertEquals(1L, entity.getParentId());
    }

    @Test
    void setPostCodeAndgetPostCodeShouldWork() {
        SysPostQuery entity = new SysPostQuery();
        entity.setPostCode("test");
        assertEquals("test", entity.getPostCode());
    }

    @Test
    void setPostNameAndgetPostNameShouldWork() {
        SysPostQuery entity = new SysPostQuery();
        entity.setPostName("test");
        assertEquals("test", entity.getPostName());
    }

    @Test
    void setSortOrderAndgetSortOrderShouldWork() {
        SysPostQuery entity = new SysPostQuery();
        entity.setSortOrder(1);
        assertEquals(1, entity.getSortOrder());
    }

    @Test
    void setPostStatusAndgetPostStatusShouldWork() {
        SysPostQuery entity = new SysPostQuery();
        entity.setPostStatus(true);
        assertEquals(true, entity.getPostStatus());
    }

    @Test
    void setDeleteStatusAndgetDeleteStatusShouldWork() {
        SysPostQuery entity = new SysPostQuery();
        entity.setDeleteStatus(true);
        assertEquals(true, entity.getDeleteStatus());
    }

    @Test
    void setCreateByAndgetCreateByShouldWork() {
        SysPostQuery entity = new SysPostQuery();
        entity.setCreateBy("test");
        assertEquals("test", entity.getCreateBy());
    }

    @Test
    void equalsAndHashCodeAndToStringShouldWork() {
        SysPostQuery a = new SysPostQuery();
        SysPostQuery b = new SysPostQuery();
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotNull(a.toString());
    }
}
