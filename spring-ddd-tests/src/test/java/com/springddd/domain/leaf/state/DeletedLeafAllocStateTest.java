package com.springddd.domain.leaf.state;

import com.springddd.domain.leaf.LeafAllocDomain;
import com.springddd.domain.leaf.LeafAllocBasicInfo;
import com.springddd.domain.leaf.LeafAllocExtendInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeletedLeafAllocStateTest {

    @Test
    @DisplayName("删除操作在已删除状态下不应改变任何内容")
    void delete_whenAlreadyDeleted_shouldDoNothing() {
        DeletedLeafAllocState state = new DeletedLeafAllocState();
        LeafAllocDomain domain = new LeafAllocDomain();
        domain.setDeleteStatus(true);
        domain.setState(state);

        state.delete(domain);

        assertThat(domain.getDeleteStatus()).isTrue();
        assertThat(domain.getState()).isInstanceOf(DeletedLeafAllocState.class);
    }

    @Test
    @DisplayName("恢复操作应将叶子分配标记为未删除并转换状态")
    void restore_shouldMarkActiveAndTransitionState() {
        DeletedLeafAllocState state = new DeletedLeafAllocState();
        LeafAllocDomain domain = new LeafAllocDomain();
        domain.setDeleteStatus(true);
        domain.setBasicInfo(new LeafAllocBasicInfo("test"));
        domain.setExtendInfo(new LeafAllocExtendInfo(100L, 10));
        domain.setState(state);

        state.restore(domain);

        assertThat(domain.getDeleteStatus()).isFalse();
        assertThat(domain.getState()).isInstanceOf(ActiveLeafAllocState.class);
    }

    @Test
    @DisplayName("创建状态对象应成功")
    void constructor_shouldCreateInstance() {
        DeletedLeafAllocState state = new DeletedLeafAllocState();
        assertThat(state).isNotNull();
        assertThat(state).isInstanceOf(com.springddd.domain.leaf.LeafAllocState.class);
    }
}
