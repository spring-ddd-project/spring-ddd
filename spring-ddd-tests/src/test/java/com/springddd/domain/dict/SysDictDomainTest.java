package com.springddd.domain.dict;

import com.springddd.domain.dict.memento.SysDictMemento;
import com.springddd.domain.dict.observer.DictObserver;
import com.springddd.domain.dict.state.ActiveDictState;
import com.springddd.domain.dict.state.DeletedDictState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SysDictDomainTest {

    private SysDictDomain createDomain() {
        SysDictDomain domain = new SysDictDomain();
        domain.setDictId(new DictId(1L));
        domain.setDictBasicInfo(new DictBasicInfo("status", "sys_status"));
        domain.setDictExtendInfo(new DictExtendInfo(1, true));
        domain.setDeleteStatus(false);
        return domain;
    }

    @Test
    void testClone() {
        SysDictDomain original = createDomain();
        SysDictDomain clone = original.clone();

        assertThat(clone.getDictId().value()).isEqualTo(1L);
        assertThat(clone.getDictBasicInfo().dictName()).isEqualTo("status");
        assertThat(clone.getDictBasicInfo().dictCode()).isEqualTo("sys_status");
        assertThat(clone.getDictExtendInfo().sortOrder()).isEqualTo(1);
        assertThat(clone.getDictExtendInfo().dictStatus()).isTrue();
    }

    @Test
    void testCloneWithNullFields() {
        SysDictDomain original = new SysDictDomain();
        SysDictDomain clone = original.clone();
        assertThat(clone.getDictId()).isNull();
        assertThat(clone.getDictBasicInfo()).isNull();
        assertThat(clone.getDictExtendInfo()).isNull();
    }

    @Test
    void testCreate() {
        SysDictDomain domain = createDomain();
        domain.create();
        assertThat(domain.getState()).isInstanceOf(ActiveDictState.class);
    }

    @Test
    void testUpdate() {
        SysDictDomain domain = createDomain();
        boolean[] called = {false};
        domain.addObserver((DictObserver) d -> called[0] = true);
        DictBasicInfo newBasic = new DictBasicInfo("newName", "newCode");
        DictExtendInfo newExt = new DictExtendInfo(2, false);
        domain.update(newBasic, newExt);

        assertThat(called[0]).isTrue();
        assertThat(domain.getDictBasicInfo().dictName()).isEqualTo("newName");
        assertThat(domain.getDictExtendInfo().sortOrder()).isEqualTo(2);
    }

    @Test
    void testDeleteFromActive() {
        SysDictDomain domain = createDomain();
        domain.setDeleteStatus(false);
        domain.delete();
        assertThat(domain.getDeleteStatus()).isTrue();
        assertThat(domain.getState()).isInstanceOf(DeletedDictState.class);
    }

    @Test
    void testDeleteFromDeleted() {
        SysDictDomain domain = createDomain();
        domain.setDeleteStatus(true);
        domain.delete();
        assertThat(domain.getDeleteStatus()).isTrue();
    }

    @Test
    void testRestoreFromDeleted() {
        SysDictDomain domain = createDomain();
        domain.setDeleteStatus(true);
        domain.restore();
        assertThat(domain.getDeleteStatus()).isFalse();
        assertThat(domain.getState()).isInstanceOf(ActiveDictState.class);
    }

    @Test
    void testRestoreFromActive() {
        SysDictDomain domain = createDomain();
        domain.setDeleteStatus(false);
        domain.restore();
        assertThat(domain.getDeleteStatus()).isFalse();
    }

    @Test
    void testMemento() {
        SysDictDomain domain = createDomain();
        SysDictMemento memento = domain.saveToMemento();
        assertThat(memento.getDictBasicInfo().dictName()).isEqualTo("status");
        assertThat(memento.getDictExtendInfo().sortOrder()).isEqualTo(1);

        domain.setDictBasicInfo(new DictBasicInfo("changed", "changed_code"));
        domain.restoreFromMemento(memento);
        assertThat(domain.getDictBasicInfo().dictName()).isEqualTo("status");
    }

    @Test
    void testSetState() {
        SysDictDomain domain = createDomain();
        domain.setState(new DeletedDictState());
        assertThat(domain.getState()).isInstanceOf(DeletedDictState.class);
    }
}
