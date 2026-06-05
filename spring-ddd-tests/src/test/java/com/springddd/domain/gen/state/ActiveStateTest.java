package com.springddd.domain.gen.state;

import com.springddd.domain.gen.GenAggregateDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ActiveStateTest {

    @Test
    @DisplayName("删除操作应将聚合标记为已删除并转换状态")
    void delete_shouldMarkDeletedAndTransitionState() {
        ActiveState state = new ActiveState();
        GenAggregateDomain domain = new GenAggregateDomain();
        domain.setDeleteStatus(false);
        domain.setState(state);

        state.delete(domain);

        assertThat(domain.getDeleteStatus()).isTrue();
        assertThat(domain.getState()).isInstanceOf(DeletedState.class);
    }

    @Test
    @DisplayName("恢复操作在活跃状态下不应改变任何内容")
    void restore_whenAlreadyActive_shouldDoNothing() {
        ActiveState state = new ActiveState();
        GenAggregateDomain domain = new GenAggregateDomain();
        domain.setDeleteStatus(false);
        domain.setState(state);

        state.restore(domain);

        assertThat(domain.getDeleteStatus()).isFalse();
        assertThat(domain.getState()).isInstanceOf(ActiveState.class);
    }

    @Test
    @DisplayName("创建状态对象应成功")
    void constructor_shouldCreateInstance() {
        ActiveState state = new ActiveState();
        assertThat(state).isNotNull();
        assertThat(state).isInstanceOf(AggregateState.class);
    }
}
