package com.springddd.domain.dept;

import com.springddd.domain.dept.observer.DeptObserver;
import com.springddd.domain.dept.state.ActiveDeptState;
import com.springddd.domain.dept.state.DeletedDeptState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

class SysDeptDomainTest {

    private SysDeptDomain createDeptDomain() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setDeptIdentifier(new DeptId(1L));
        domain.setParentId(new DeptId(0L));
        domain.setDeptBasicInfo(new DeptBasicInfo("TestDept"));
        domain.setDeptExtendInfo(new DeptExtendInfo(1, true));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        return domain;
    }

    @Test
    @DisplayName("create 应设置 ActiveDeptState")
    void create_shouldSetActiveState() {
        SysDeptDomain domain = createDeptDomain();
        domain.create();
        assertThat(domain.getState()).isInstanceOf(ActiveDeptState.class);
    }

    @Test
    @DisplayName("update 应更新基本信息")
    void update_shouldUpdateInfo() {
        SysDeptDomain domain = createDeptDomain();
        domain.create();

        domain.update(new DeptId(2L), new DeptBasicInfo("NewName"), new DeptExtendInfo(2, false));

        assertThat(domain.getDeptBasicInfo().deptName()).isEqualTo("NewName");
        assertThat(domain.getParentId().value()).isEqualTo(2L);
    }

    @Test
    @DisplayName("delete 应设置 deleteStatus")
    void delete_shouldSetDeleteStatus() {
        SysDeptDomain domain = createDeptDomain();
        domain.delete();
        assertThat(domain.getDeleteStatus()).isTrue();
    }

    @Test
    @DisplayName("restore 应清除 deleteStatus")
    void restore_shouldClearDeleteStatus() {
        SysDeptDomain domain = createDeptDomain();
        domain.delete();
        domain.restore();
        assertThat(domain.getDeleteStatus()).isFalse();
    }

    @Test
    @DisplayName("clone 应创建深拷贝")
    void clone_shouldCreateDeepCopy() {
        SysDeptDomain original = createDeptDomain();
        SysDeptDomain cloned = original.clone();

        assertThat(cloned).isNotSameAs(original);
        assertThat(cloned.getDeptIdentifier().value()).isEqualTo(1L);
        assertThat(cloned.getDeptBasicInfo()).isNotSameAs(original.getDeptBasicInfo());
    }

    @Test
    @DisplayName("saveToMemento 应保存状态")
    void saveToMemento_shouldSaveState() {
        SysDeptDomain domain = createDeptDomain();
        var memento = domain.saveToMemento();

        assertThat(memento.getDeptBasicInfo().deptName()).isEqualTo("TestDept");
    }

    @Test
    @DisplayName("restoreFromMemento 应恢复状态")
    void restoreFromMemento_shouldRestoreState() {
        SysDeptDomain domain = createDeptDomain();
        var memento = domain.saveToMemento();

        domain.setDeptBasicInfo(new DeptBasicInfo("Changed"));
        domain.restoreFromMemento(memento);

        assertThat(domain.getDeptBasicInfo().deptName()).isEqualTo("TestDept");
    }

    @Test
    @DisplayName("clone 当字段为 null 时应正确处理")
    void clone_withNullFields_shouldHandleNulls() {
        SysDeptDomain original = new SysDeptDomain();
        original.setDeptIdentifier(null);
        original.setParentId(null);
        original.setDeptBasicInfo(null);
        original.setDeptExtendInfo(null);
        original.setDeptId(1L);

        SysDeptDomain cloned = original.clone();

        assertThat(cloned).isNotSameAs(original);
        assertThat(cloned.getDeptIdentifier()).isNull();
        assertThat(cloned.getParentId()).isNull();
        assertThat(cloned.getDeptBasicInfo()).isNull();
        assertThat(cloned.getDeptExtendInfo()).isNull();
        assertThat(cloned.getDeptId()).isEqualTo(original.getDeptId());
    }

    @Test
    @DisplayName("addObserver 应添加观察者并在 update 时通知")
    void addObserver_shouldNotifyOnUpdate() {
        SysDeptDomain domain = createDeptDomain();
        DeptObserver observer = Mockito.mock(DeptObserver.class);
        domain.addObserver(observer);

        domain.update(new DeptId(2L), new DeptBasicInfo("Updated"), new DeptExtendInfo(2, false));

        verify(observer).onUpdate(domain);
    }

    @Test
    @DisplayName("delete 当 state 为 null 且 deleteStatus 为 true 时应设为 DeletedDeptState")
    void delete_whenStateNullAndDeleteStatusTrue_shouldSetDeletedState() {
        SysDeptDomain domain = createDeptDomain();
        domain.setDeleteStatus(true);
        domain.setState(null);
        domain.delete();
        assertThat(domain.getDeleteStatus()).isTrue();
        assertThat(domain.getState()).isInstanceOf(DeletedDeptState.class);
    }

    @Test
    @DisplayName("delete 当 state 已设置时应委托给 state")
    void delete_whenStateNotNull_shouldDelegateToState() {
        SysDeptDomain domain = createDeptDomain();
        domain.create(); // sets ActiveDeptState
        domain.delete();
        assertThat(domain.getDeleteStatus()).isTrue();
        assertThat(domain.getState()).isInstanceOf(DeletedDeptState.class);
    }

    @Test
    @DisplayName("restore 当 state 为 null 且 deleteStatus 为 true 时应恢复为 ActiveDeptState")
    void restore_whenStateNullAndDeleteStatusTrue_shouldRestore() {
        SysDeptDomain domain = createDeptDomain();
        domain.setDeleteStatus(true);
        domain.setState(null);
        domain.restore();
        assertThat(domain.getDeleteStatus()).isFalse();
        assertThat(domain.getState()).isInstanceOf(ActiveDeptState.class);
    }

    @Test
    @DisplayName("restore 当 state 为 null 且 deleteStatus 为 false 时应保持 ActiveDeptState")
    void restore_whenStateNullAndDeleteStatusFalse_shouldKeepActiveState() {
        SysDeptDomain domain = createDeptDomain();
        domain.setDeleteStatus(false);
        domain.setState(null);
        domain.restore();
        assertThat(domain.getDeleteStatus()).isFalse();
        assertThat(domain.getState()).isInstanceOf(ActiveDeptState.class);
    }

    @Test
    @DisplayName("restore 当 state 为 ActiveDeptState 时应保持原状态")
    void restore_whenStateActive_shouldDoNothing() {
        SysDeptDomain domain = createDeptDomain();
        domain.create(); // sets ActiveDeptState
        domain.restore();
        assertThat(domain.getDeleteStatus()).isFalse();
        assertThat(domain.getState()).isInstanceOf(ActiveDeptState.class);
    }
}
