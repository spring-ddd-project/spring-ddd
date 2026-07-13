package com.springddd.domain.post;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysPostDomainTest {

    @Test
    void setIdAndgetIdShouldWork() {
        SysPostDomain entity = new SysPostDomain();
        entity.setId(null);
        assertEquals(null, entity.getId());
    }

    @Test
    void setPostBasicInfoAndgetPostBasicInfoShouldWork() {
        SysPostDomain entity = new SysPostDomain();
        entity.setPostBasicInfo(null);
        assertEquals(null, entity.getPostBasicInfo());
    }

    @Test
    void setPostExtendInfoAndgetPostExtendInfoShouldWork() {
        SysPostDomain entity = new SysPostDomain();
        entity.setPostExtendInfo(null);
        assertEquals(null, entity.getPostExtendInfo());
    }

    @Test
    void equalsAndHashCodeAndToStringShouldWork() {
        SysPostDomain a = new SysPostDomain();
        SysPostDomain b = new SysPostDomain();
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotNull(a.toString());
    }
}
