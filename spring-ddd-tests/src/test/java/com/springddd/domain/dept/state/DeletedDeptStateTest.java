package com.springddd.domain.dept.state;

import com.springddd.domain.dept.DeptBasicInfo;
import com.springddd.domain.dept.DeptExtendInfo;
import com.springddd.domain.dept.SysDeptDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeletedDeptStateTest {

    @Test
    @DisplayName("删除操作在已删除状态下不应改变任何内容")
    void delete_whenAlreadyDeleted_shouldDoNothing() {
        DeletedDeptState state = new DeletedDeptState();
        SysDeptDomain domain = new SysDeptDomain();
        domain.setDeleteStatus(true);
        domain.setState(state);

        state.delete(domain);

        assertThat(domain.getDeleteStatus()).isTrue();
        assertThat(domain.getState()).isInstanceOf(DeletedDeptState.class);
    }

    @Test
    @DisplayName("恢复操作应将部门标记为未删除并转换状态")
    void restore_shouldMarkActiveAndTransitionState() {
        DeletedDeptState state = new DeletedDeptState();
        SysDeptDomain domain = new SysDeptDomain();
        domain.setDeleteStatus(true);
        domain.setDeptBasicInfo(new DeptBasicInfo("Test Dept"));
        domain.setDeptExtendInfo(new DeptExtendInfo(1, true));
        domain.setState(state);

        state.restore(domain);

        assertThat(domain.getDeleteStatus()).isFalse();
        assertThat(domain.getState()).isInstanceOf(ActiveDeptState.class);
    }

    @Test
    @DisplayName("创建状态对象应成功")
    void constructor_shouldCreateInstance() {
        DeletedDeptState state = new DeletedDeptState();
        assertThat(state).isNotNull();
        assertThat(state).isInstanceOf(DeptState.class);
    }
}
