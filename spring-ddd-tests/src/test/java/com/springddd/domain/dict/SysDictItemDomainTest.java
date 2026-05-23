package com.springddd.domain.dict;

import com.springddd.domain.dict.memento.SysDictItemMemento;
import com.springddd.domain.dict.state.DisabledDictItemState;
import com.springddd.domain.dict.state.EnabledDictItemState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SysDictItemDomainTest {

    private SysDictItemDomain createDomain() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemId(new DictItemId(1L));
        domain.setDictId(new DictId(1L));
        domain.setItemBasicInfo(new DictItemBasicInfo("label", 1, true));
        domain.setItemExtendInfo(new DictItemExtendInfo(1, true));
        domain.setDeleteStatus(false);
        return domain;
    }

    @Test
    void testClone() {
        SysDictItemDomain original = createDomain();
        SysDictItemDomain clone = original.clone();

        assertThat(clone.getItemId().value()).isEqualTo(1L);
        assertThat(clone.getDictId().value()).isEqualTo(1L);
        assertThat(clone.getItemBasicInfo().itemLabel()).isEqualTo("label");
        assertThat(clone.getItemBasicInfo().itemValue()).isEqualTo(1);
        // clone uses 2-arg constructor, itemStatus becomes null
        assertThat(clone.getItemBasicInfo().itemStatus()).isNull();
    }

    @Test
    void testCloneWithNullFields() {
        SysDictItemDomain original = new SysDictItemDomain();
        SysDictItemDomain clone = original.clone();
        assertThat(clone.getItemId()).isNull();
        assertThat(clone.getDictId()).isNull();
        assertThat(clone.getItemBasicInfo()).isNull();
        assertThat(clone.getItemExtendInfo()).isNull();
    }

    @Test
    void testEnableFromDisabled() {
        SysDictItemDomain domain = createDomain();
        domain.setItemBasicInfo(new DictItemBasicInfo("label", 1, false));
        domain.enable();
        // DisabledDictItemState.enable() creates new DictItemBasicInfo via 2-arg ctor -> itemStatus=null
        assertThat(domain.getItemBasicInfo().itemStatus()).isNull();
        assertThat(domain.getState()).isInstanceOf(EnabledDictItemState.class);
    }

    @Test
    void testEnableFromEnabled() {
        SysDictItemDomain domain = createDomain();
        domain.setItemBasicInfo(new DictItemBasicInfo("label", 1, true));
        domain.enable();
        assertThat(domain.getItemBasicInfo().itemStatus()).isTrue();
        assertThat(domain.getState()).isInstanceOf(EnabledDictItemState.class);
    }

    @Test
    void testDisableFromEnabled() {
        SysDictItemDomain domain = createDomain();
        domain.setItemBasicInfo(new DictItemBasicInfo("label", 1, true));
        domain.disable();
        assertThat(domain.getItemBasicInfo().itemStatus()).isFalse();
        assertThat(domain.getState()).isInstanceOf(DisabledDictItemState.class);
    }

    @Test
    void testDisableFromDisabled() {
        SysDictItemDomain domain = createDomain();
        domain.setItemBasicInfo(new DictItemBasicInfo("label", 1, false));
        domain.disable();
        assertThat(domain.getItemBasicInfo().itemStatus()).isFalse();
        assertThat(domain.getState()).isInstanceOf(DisabledDictItemState.class);
    }

    @Test
    void testCreate() {
        SysDictItemDomain domain = createDomain();
        domain.create();
        // create() is empty
        assertThat(domain.getItemId()).isNotNull();
    }

    @Test
    void testUpdate() {
        SysDictItemDomain domain = createDomain();
        DictItemBasicInfo newBasic = new DictItemBasicInfo("newLabel", 2, false);
        DictItemExtendInfo newExt = new DictItemExtendInfo(2, false);
        domain.update(newBasic, newExt);
        assertThat(domain.getItemBasicInfo().itemLabel()).isEqualTo("newLabel");
        assertThat(domain.getItemBasicInfo().itemValue()).isEqualTo(2);
        assertThat(domain.getItemExtendInfo().sortOrder()).isEqualTo(2);
    }

    @Test
    void testDelete() {
        SysDictItemDomain domain = createDomain();
        domain.delete();
        assertThat(domain.getDeleteStatus()).isTrue();
    }

    @Test
    void testRestoreFromDeleted() {
        SysDictItemDomain domain = createDomain();
        domain.setDeleteStatus(true);
        domain.setItemBasicInfo(new DictItemBasicInfo("label", 1, true));
        domain.restore();
        assertThat(domain.getDeleteStatus()).isFalse();
    }

    @Test
    void testRestoreFromActive() {
        SysDictItemDomain domain = createDomain();
        domain.setDeleteStatus(false);
        domain.setItemBasicInfo(new DictItemBasicInfo("label", 1, true));
        domain.restore();
        assertThat(domain.getDeleteStatus()).isFalse();
    }

    @Test
    void testMemento() {
        SysDictItemDomain domain = createDomain();
        SysDictItemMemento memento = domain.saveToMemento();
        assertThat(memento.getItemBasicInfo().itemLabel()).isEqualTo("label");
        assertThat(memento.getItemExtendInfo().sortOrder()).isEqualTo(1);

        domain.setItemBasicInfo(new DictItemBasicInfo("changed", 99, false));
        domain.restoreFromMemento(memento);
        assertThat(domain.getItemBasicInfo().itemLabel()).isEqualTo("label");
    }

    @Test
    void testSetState() {
        SysDictItemDomain domain = createDomain();
        domain.setState(new DisabledDictItemState());
        assertThat(domain.getState()).isInstanceOf(DisabledDictItemState.class);
    }
}
