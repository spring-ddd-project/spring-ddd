package com.springddd.domain.gen.state;

import com.springddd.domain.gen.GenAggregateDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeletedStateTest {

    @Test
    @DisplayName("删除操作在已删除状态下不应改变任何内容")
    void delete_whenAlreadyDeleted_shouldDoNothing() {
        DeletedState state = new DeletedState();
        GenAggregateDomain domain = new GenAggregateDomain();
        domain.setDeleteStatus(true);
        domain.setState(state);

        state.delete(domain);

        assertThat(domain.getDeleteStatus()).isTrue();
        assertThat(domain.getState()).isInstanceOf(DeletedState.class);
    }

    @Test
    @DisplayName("恢复操作应将聚合标记为活跃并转换状态")
    void restore_shouldMarkActiveAndTransitionState() {
        DeletedState state = new DeletedState();
        GenAggregateDomain domain = new GenAggregateDomain();
        domain.setDeleteStatus(true);
        domain.setState(state);

        state.restore(domain);

        assertThat(domain.getDeleteStatus()).isFalse();
        assertThat(domain.getState()).isInstanceOf(ActiveState.class);
    }

    @Test
    @DisplayName("创建状态对象应成功")
    void constructor_shouldCreateInstance() {
        DeletedState state = new DeletedState();
        assertThat(state).isNotNull();
        assertThat(state).isInstanceOf(AggregateState.class);
    }
}
