package com.springddd.domain.dict.state;

import com.springddd.domain.dict.DictBasicInfo;
import com.springddd.domain.dict.DictExtendInfo;
import com.springddd.domain.dict.SysDictDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ActiveDictStateTest {

    @Test
    @DisplayName("删除操作应将字典标记为已删除并转换状态")
    void delete_shouldMarkDeletedAndTransitionState() {
        ActiveDictState state = new ActiveDictState();
        SysDictDomain domain = new SysDictDomain();
        domain.setDeleteStatus(false);
        domain.setState(state);

        state.delete(domain);

        assertThat(domain.getDeleteStatus()).isTrue();
        assertThat(domain.getState()).isInstanceOf(DeletedDictState.class);
    }

    @Test
    @DisplayName("恢复操作在已激活状态下不应改变任何内容")
    void restore_whenAlreadyActive_shouldDoNothing() {
        ActiveDictState state = new ActiveDictState();
        SysDictDomain domain = new SysDictDomain();
        domain.setDeleteStatus(false);
        domain.setDictBasicInfo(new DictBasicInfo("Test Dict", "test_code"));
        domain.setDictExtendInfo(new DictExtendInfo(1, true));
        domain.setState(state);

        state.restore(domain);

        assertThat(domain.getDeleteStatus()).isFalse();
        assertThat(domain.getState()).isInstanceOf(ActiveDictState.class);
    }

    @Test
    @DisplayName("创建状态对象应成功")
    void constructor_shouldCreateInstance() {
        ActiveDictState state = new ActiveDictState();
        assertThat(state).isNotNull();
        assertThat(state).isInstanceOf(DictState.class);
    }
}
