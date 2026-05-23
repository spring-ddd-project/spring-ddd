package com.springddd.domain.dict.state;

import com.springddd.domain.dict.DictBasicInfo;
import com.springddd.domain.dict.DictExtendInfo;
import com.springddd.domain.dict.SysDictDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeletedDictStateTest {

    @Test
    @DisplayName("删除操作在已删除状态下不应改变任何内容")
    void delete_whenAlreadyDeleted_shouldDoNothing() {
        DeletedDictState state = new DeletedDictState();
        SysDictDomain domain = new SysDictDomain();
        domain.setDeleteStatus(true);
        domain.setState(state);

        state.delete(domain);

        assertThat(domain.getDeleteStatus()).isTrue();
        assertThat(domain.getState()).isInstanceOf(DeletedDictState.class);
    }

    @Test
    @DisplayName("恢复操作应将字典标记为未删除并转换状态")
    void restore_shouldMarkActiveAndTransitionState() {
        DeletedDictState state = new DeletedDictState();
        SysDictDomain domain = new SysDictDomain();
        domain.setDeleteStatus(true);
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
        DeletedDictState state = new DeletedDictState();
        assertThat(state).isNotNull();
        assertThat(state).isInstanceOf(DictState.class);
    }
}
