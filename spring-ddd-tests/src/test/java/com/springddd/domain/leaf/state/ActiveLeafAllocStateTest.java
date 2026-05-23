package com.springddd.domain.leaf.state;

import com.springddd.domain.leaf.LeafAllocDomain;
import com.springddd.domain.leaf.LeafAllocBasicInfo;
import com.springddd.domain.leaf.LeafAllocExtendInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ActiveLeafAllocStateTest {

    @Test
    @DisplayName("删除操作应将叶子分配标记为已删除并转换状态")
    void delete_shouldMarkDeletedAndTransitionState() {
        ActiveLeafAllocState state = new ActiveLeafAllocState();
        LeafAllocDomain domain = new LeafAllocDomain();
        domain.setDeleteStatus(false);
        domain.setState(state);

        state.delete(domain);

        assertThat(domain.getDeleteStatus()).isTrue();
        assertThat(domain.getState()).isInstanceOf(DeletedLeafAllocState.class);
    }

    @Test
    @DisplayName("恢复操作在已激活状态下不应改变任何内容")
    void restore_whenAlreadyActive_shouldDoNothing() {
        ActiveLeafAllocState state = new ActiveLeafAllocState();
        LeafAllocDomain domain = new LeafAllocDomain();
        domain.setDeleteStatus(false);
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
        ActiveLeafAllocState state = new ActiveLeafAllocState();
        assertThat(state).isNotNull();
        assertThat(state).isInstanceOf(com.springddd.domain.leaf.LeafAllocState.class);
    }
}
