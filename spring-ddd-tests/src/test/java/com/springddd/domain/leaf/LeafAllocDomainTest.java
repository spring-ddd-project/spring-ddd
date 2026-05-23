package com.springddd.domain.leaf;

import com.springddd.domain.leaf.state.ActiveLeafAllocState;
import com.springddd.domain.leaf.state.DeletedLeafAllocState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LeafAllocDomainTest {

    private LeafAllocDomain createDomain() {
        LeafAllocDomain domain = new LeafAllocDomain();
        domain.setLeafAllocId(new LeafAllocId("test-key"));
        domain.setId(1L);
        domain.setBasicInfo(new LeafAllocBasicInfo("test description"));
        domain.setExtendInfo(new LeafAllocExtendInfo(1000L, 10));
        domain.setDeleteStatus(false);
        return domain;
    }

    @Test
    void testCreate() {
        LeafAllocDomain domain = createDomain();
        domain.create();
        assertThat(domain.getState()).isInstanceOf(ActiveLeafAllocState.class);
    }

    @Test
    void testUpdate() {
        LeafAllocDomain domain = createDomain();
        LeafAllocBasicInfo newBasic = new LeafAllocBasicInfo("new description");
        LeafAllocExtendInfo newExt = new LeafAllocExtendInfo(2000L, 20);
        domain.update(newBasic, newExt);
        assertThat(domain.getBasicInfo().description()).isEqualTo("new description");
        assertThat(domain.getExtendInfo().maxId()).isEqualTo(2000L);
        assertThat(domain.getExtendInfo().step()).isEqualTo(20);
    }

    @Test
    void testDeleteFromActive() {
        LeafAllocDomain domain = createDomain();
        domain.setDeleteStatus(false);
        domain.delete();
        assertThat(domain.getDeleteStatus()).isTrue();
        assertThat(domain.getState()).isInstanceOf(DeletedLeafAllocState.class);
    }

    @Test
    void testDeleteFromDeleted() {
        LeafAllocDomain domain = createDomain();
        domain.setDeleteStatus(true);
        domain.delete();
        assertThat(domain.getDeleteStatus()).isTrue();
    }

    @Test
    void testRestoreFromDeleted() {
        LeafAllocDomain domain = createDomain();
        domain.setDeleteStatus(true);
        domain.restore();
        assertThat(domain.getDeleteStatus()).isFalse();
        assertThat(domain.getState()).isInstanceOf(ActiveLeafAllocState.class);
    }

    @Test
    void testRestoreFromActive() {
        LeafAllocDomain domain = createDomain();
        domain.setDeleteStatus(false);
        domain.restore();
        assertThat(domain.getDeleteStatus()).isFalse();
    }

    @Test
    void testSetState() {
        LeafAllocDomain domain = createDomain();
        domain.setState(new DeletedLeafAllocState());
        assertThat(domain.getState()).isInstanceOf(DeletedLeafAllocState.class);
    }
}
