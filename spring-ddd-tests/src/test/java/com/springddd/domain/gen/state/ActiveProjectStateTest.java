package com.springddd.domain.gen.state;

import com.springddd.domain.gen.GenProjectInfoDomain;
import com.springddd.domain.gen.ProjectInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ActiveProjectStateTest {

    @Test
    @DisplayName("删除操作应将项目标记为已删除并转换状态")
    void delete_shouldMarkDeletedAndTransitionState() {
        ActiveProjectState state = new ActiveProjectState();
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.setDeleteStatus(false);
        domain.setState(state);

        state.delete(domain);

        assertThat(domain.getDeleteStatus()).isTrue();
        assertThat(domain.getState()).isInstanceOf(DeletedProjectState.class);
    }

    @Test
    @DisplayName("创建状态对象应成功")
    void constructor_shouldCreateInstance() {
        ActiveProjectState state = new ActiveProjectState();
        assertThat(state).isNotNull();
        assertThat(state).isInstanceOf(ProjectState.class);
    }
}
