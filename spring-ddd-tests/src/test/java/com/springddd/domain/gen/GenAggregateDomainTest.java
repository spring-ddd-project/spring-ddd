package com.springddd.domain.gen;

import com.springddd.domain.gen.state.ActiveState;
import com.springddd.domain.gen.state.DeletedState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenAggregateDomainTest {

    private GenAggregateDomain createDomain() {
        GenAggregateDomain domain = new GenAggregateDomain();
        domain.setAggregateId(new AggregateId(1L));
        domain.setInfoId(new InfoId(1L));
        domain.setValueObject(new GenAggregateValueObject("TestObj", "testValue", (byte) 1));
        domain.setExtendInfo(new GenAggregateExtendInfo(true));
        domain.setDeleteStatus(false);
        return domain;
    }

    @Test
    void testCreate() {
        GenAggregateDomain domain = createDomain();
        domain.create();
        assertThat(domain.getState()).isInstanceOf(ActiveState.class);
    }

    @Test
    void testUpdate() {
        GenAggregateDomain domain = createDomain();
        InfoId newInfoId = new InfoId(2L);
        GenAggregateValueObject newVo = new GenAggregateValueObject("NewObj", "newValue", (byte) 2);
        GenAggregateExtendInfo newExt = new GenAggregateExtendInfo(false);
        domain.update(newInfoId, newVo, newExt);
        assertThat(domain.getInfoId().value()).isEqualTo(2L);
        assertThat(domain.getValueObject().objectName()).isEqualTo("NewObj");
        assertThat(domain.getExtendInfo().hasCreated()).isFalse();
    }

    @Test
    void testDeleteFromActive() {
        GenAggregateDomain domain = createDomain();
        domain.setDeleteStatus(false);
        domain.delete();
        assertThat(domain.getDeleteStatus()).isTrue();
        assertThat(domain.getState()).isInstanceOf(DeletedState.class);
    }

    @Test
    void testDeleteFromDeleted() {
        GenAggregateDomain domain = createDomain();
        domain.setDeleteStatus(true);
        domain.delete();
        assertThat(domain.getDeleteStatus()).isTrue();
    }

    @Test
    void testRestoreFromDeleted() {
        GenAggregateDomain domain = createDomain();
        domain.setDeleteStatus(true);
        domain.restore();
        assertThat(domain.getDeleteStatus()).isFalse();
        assertThat(domain.getState()).isInstanceOf(ActiveState.class);
    }

    @Test
    void testRestoreFromActive() {
        GenAggregateDomain domain = createDomain();
        domain.setDeleteStatus(false);
        domain.restore();
        assertThat(domain.getDeleteStatus()).isFalse();
    }

    @Test
    void testSetState() {
        GenAggregateDomain domain = createDomain();
        domain.setState(new DeletedState());
        assertThat(domain.getState()).isInstanceOf(DeletedState.class);
    }
}
