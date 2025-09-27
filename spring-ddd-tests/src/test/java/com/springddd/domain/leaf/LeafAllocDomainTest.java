package com.springddd.domain.leaf;

import com.springddd.domain.AbstractDomainMask;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LeafAllocDomainTest {

    @Test
    void shouldHaveLeafAllocDomainClass() {
        assertNotNull(LeafAllocDomain.class);
    }

    @Test
    void shouldExtendAbstractDomainMask() {
        assertTrue(AbstractDomainMask.class.isAssignableFrom(LeafAllocDomain.class));
    }

    @Test
    void shouldHaveCreateMethod() {
        assertNotNull(LeafAllocDomain.class.getDeclaredMethod("create"));
    }

    @Test
    void shouldHaveUpdateMethod() {
        assertNotNull(LeafAllocDomain.class.getDeclaredMethod("update", LeafProp.class, ExtendInfo.class));
    }

    @Test
    void shouldHaveDeleteMethod() {
        assertNotNull(LeafAllocDomain.class.getDeclaredMethod("delete"));
    }

    @Test
    void shouldHaveRestoreMethod() {
        assertNotNull(LeafAllocDomain.class.getDeclaredMethod("restore"));
    }

    @Test
    void shouldHaveUpdateMaxIdMethod() {
        assertNotNull(LeafAllocDomain.class.getDeclaredMethod("updateMaxId", LeafProp.class));
    }

    @Test
    void shouldHaveUpdateMaxIdByCustomStepMethod() {
        assertNotNull(LeafAllocDomain.class.getDeclaredMethod("updateMaxIdByCustomStep", LeafProp.class));
    }

    @Test
    void create_shouldNotThrowException() {
        LeafAllocDomain domain = new LeafAllocDomain();
        domain.create();
        assertNotNull(domain);
    }

    @Test
    void delete_shouldSetDeleteStatusToTrue() {
        LeafAllocDomain domain = new LeafAllocDomain();
        domain.delete();
        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void restore_shouldSetDeleteStatusToFalse() {
        LeafAllocDomain domain = new LeafAllocDomain();
        domain.setDeleteStatus(true);
        domain.restore();
        assertFalse(domain.getDeleteStatus());
    }
}
