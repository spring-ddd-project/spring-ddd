package com.springddd.domain.dict.state;

import com.springddd.domain.dict.DictItemBasicInfo;
import com.springddd.domain.dict.DictItemExtendInfo;
import com.springddd.domain.dict.SysDictItemDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EnabledDictItemStateTest {

    @Test
    @DisplayName("启用操作在已启用状态下不应改变任何内容")
    void enable_whenAlreadyEnabled_shouldDoNothing() {
        EnabledDictItemState state = new EnabledDictItemState();
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemBasicInfo(new DictItemBasicInfo("Label", 1, true));
        domain.setItemExtendInfo(new DictItemExtendInfo(1, true));
        domain.setState(state);

        state.enable(domain);

        assertThat(domain.getState()).isInstanceOf(EnabledDictItemState.class);
    }

    @Test
    @DisplayName("禁用操作应将字典项标记为禁用并转换状态")
    void disable_shouldMarkDisabledAndTransitionState() {
        EnabledDictItemState state = new EnabledDictItemState();
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemBasicInfo(new DictItemBasicInfo("Label", 1, true));
        domain.setItemExtendInfo(new DictItemExtendInfo(1, true));
        domain.setState(state);

        state.disable(domain);

        assertThat(domain.getState()).isInstanceOf(DisabledDictItemState.class);
    }

    @Test
    @DisplayName("恢复操作应将删除状态设为 false")
    void restore_shouldSetDeleteStatusFalse() {
        EnabledDictItemState state = new EnabledDictItemState();
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setDeleteStatus(true);
        domain.setItemBasicInfo(new DictItemBasicInfo("Label", 1, true));
        domain.setItemExtendInfo(new DictItemExtendInfo(1, true));
        domain.setState(state);

        state.restore(domain);

        assertThat(domain.getDeleteStatus()).isFalse();
    }

    @Test
    @DisplayName("创建状态对象应成功")
    void constructor_shouldCreateInstance() {
        EnabledDictItemState state = new EnabledDictItemState();
        assertThat(state).isNotNull();
        assertThat(state).isInstanceOf(DictItemState.class);
    }
}
