package com.springddd.domain.gen.state;

import com.springddd.domain.gen.GenProjectInfoDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeletedProjectStateTest {

    @Test
    @DisplayName("删除操作在已删除状态下不应改变任何内容")
    void delete_whenAlreadyDeleted_shouldDoNothing() {
        DeletedProjectState state = new DeletedProjectState();
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.setDeleteStatus(true);
        domain.setState(state);

        state.delete(domain);

        assertThat(domain.getDeleteStatus()).isTrue();
        assertThat(domain.getState()).isInstanceOf(DeletedProjectState.class);
    }

    @Test
    @DisplayName("创建状态对象应成功")
    void constructor_shouldCreateInstance() {
        DeletedProjectState state = new DeletedProjectState();
        assertThat(state).isNotNull();
        assertThat(state).isInstanceOf(ProjectState.class);
    }
}
