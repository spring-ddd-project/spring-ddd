package com.springddd.domain.leaf;

import com.springddd.domain.leaf.state.ActiveLeafAllocState;
import com.springddd.domain.leaf.state.DeletedLeafAllocState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeafAllocDomainTest {

    @Test
    void shouldCreateLeafAllocDomain() {
        LeafAllocDomain domain = new LeafAllocDomain();
        assertNotNull(domain);
    }

    @Test
    void shouldSetAndGetLeafAllocId() {
        LeafAllocDomain domain = new LeafAllocDomain();
        LeafAllocId id = new LeafAllocId(1L);
        domain.setLeafAllocId(id);
        assertEquals(id, domain.getLeafAllocId());
    }

    @Test
    void shouldSetAndGetBizTag() {
        LeafAllocDomain domain = new LeafAllocDomain();
        BizTag bizTag = new BizTag("order");
        domain.setBizTag(bizTag);
        assertEquals(bizTag, domain.getBizTag());
    }

    @Test
    void shouldSetAndGetMaxId() {
        LeafAllocDomain domain = new LeafAllocDomain();
        MaxId maxId = new MaxId(100L);
        domain.setMaxId(maxId);
        assertEquals(maxId, domain.getMaxId());
    }

    @Test
    void shouldSetAndGetStep() {
        LeafAllocDomain domain = new LeafAllocDomain();
        Step step = new Step(100);
        domain.setStep(step);
        assertEquals(step, domain.getStep());
    }

    @Test
    void shouldSetAndGetDescription() {
        LeafAllocDomain domain = new LeafAllocDomain();
        Description description = new Description("test");
        domain.setDescription(description);
        assertEquals(description, domain.getDescription());
    }

    @Test
    void shouldSetAndGetState() {
        LeafAllocDomain domain = new LeafAllocDomain();
        ActiveLeafAllocState state = new ActiveLeafAllocState();
        domain.setState(state);
        assertEquals(state, domain.getState());
    }

    @Test
    void create_shouldSetActiveState() {
        LeafAllocDomain domain = new LeafAllocDomain();
        domain.create();
        assertNotNull(domain.getState());
        assertTrue(domain.getState() instanceof ActiveLeafAllocState);
        assertTrue(domain.getState().isActive());
    }

    @Test
    void update_shouldUpdateAllFieldsAndDeptId() {
        LeafAllocDomain domain = new LeafAllocDomain();
        BizTag bizTag = new BizTag("order");
        MaxId maxId = new MaxId(100L);
        Step step = new Step(100);
        Description description = new Description("test");

        domain.update(bizTag, maxId, step, description, 1L);

        assertEquals(bizTag, domain.getBizTag());
        assertEquals(maxId, domain.getMaxId());
        assertEquals(step, domain.getStep());
        assertEquals(description, domain.getDescription());
        assertEquals(1L, domain.getDeptId());
    }

    @Test
    void updateMaxId_shouldUpdateMaxIdOnly() {
        LeafAllocDomain domain = new LeafAllocDomain();
        MaxId initialMaxId = new MaxId(100L);
        domain.setMaxId(initialMaxId);

        MaxId newMaxId = new MaxId(200L);
        domain.updateMaxId(newMaxId);

        assertEquals(newMaxId, domain.getMaxId());
    }

    @Test
    void updateStep_shouldUpdateStepOnly() {
        LeafAllocDomain domain = new LeafAllocDomain();
        Step initialStep = new Step(100);
        domain.setStep(initialStep);

        Step newStep = new Step(200);
        domain.updateStep(newStep);

        assertEquals(newStep, domain.getStep());
    }

    @Test
    void delete_shouldSetDeleteStatusTrueAndDeletedState() {
        LeafAllocDomain domain = new LeafAllocDomain();
        domain.create();
        domain.delete();

        assertTrue(domain.getDeleteStatus());
        assertNotNull(domain.getState());
        assertTrue(domain.getState() instanceof DeletedLeafAllocState);
        assertFalse(domain.getState().isActive());
    }

    @Test
    void restore_shouldSetDeleteStatusFalseAndActiveState() {
        LeafAllocDomain domain = new LeafAllocDomain();
        domain.create();
        domain.delete();
        domain.restore();

        assertFalse(domain.getDeleteStatus());
        assertNotNull(domain.getState());
        assertTrue(domain.getState() instanceof ActiveLeafAllocState);
        assertTrue(domain.getState().isActive());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        LeafAllocDomain domain = new LeafAllocDomain();
        String str = domain.toString();
        assertTrue(str.contains("LeafAllocDomain"));
    }
}
