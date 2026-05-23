package com.springddd.domain.gen;

import com.springddd.domain.gen.state.ActiveProjectState;
import com.springddd.domain.gen.state.DeletedProjectState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenProjectInfoDomainTest {

    private GenProjectInfoDomain createDomain() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.setId(new InfoId(1L));
        domain.setProjectInfo(new ProjectInfo("sys_user", "com.springddd", "SysUser", "user", "spring-ddd"));
        domain.setExtendInfo(new GenProjectInfoExtendInfo("sys-user", "1.0"));
        domain.setDeleteStatus(false);
        return domain;
    }

    @Test
    void testClone() {
        GenProjectInfoDomain original = createDomain();
        GenProjectInfoDomain clone = original.clone();

        assertThat(clone.getId().value()).isEqualTo(1L);
        assertThat(clone.getProjectInfo().tableName()).isEqualTo("sys_user");
        assertThat(clone.getProjectInfo().packageName()).isEqualTo("com.springddd");
        assertThat(clone.getExtendInfo().requestName()).isEqualTo("sys-user");
    }

    @Test
    void testCloneWithNullFields() {
        GenProjectInfoDomain original = new GenProjectInfoDomain();
        GenProjectInfoDomain clone = original.clone();
        assertThat(clone.getId()).isNull();
        assertThat(clone.getProjectInfo()).isNull();
        assertThat(clone.getExtendInfo()).isNull();
    }

    @Test
    void testCreate() {
        GenProjectInfoDomain domain = createDomain();
        domain.create();
    }

    @Test
    void testUpdate() {
        GenProjectInfoDomain domain = createDomain();
        ProjectInfo newInfo = new ProjectInfo("sys_role", "com.test", "SysRole", "role", "test");
        GenProjectInfoExtendInfo newExt = new GenProjectInfoExtendInfo("sys-role", "2.0");
        domain.update(newInfo, newExt);
        assertThat(domain.getProjectInfo().tableName()).isEqualTo("sys_role");
        assertThat(domain.getExtendInfo().projectVersion()).isEqualTo("2.0");
    }

    @Test
    void testDeleteFromActive() {
        GenProjectInfoDomain domain = createDomain();
        domain.setDeleteStatus(false);
        domain.delete();
        assertThat(domain.getDeleteStatus()).isTrue();
        assertThat(domain.getState()).isInstanceOf(DeletedProjectState.class);
    }

    @Test
    void testDeleteFromDeleted() {
        GenProjectInfoDomain domain = createDomain();
        domain.setDeleteStatus(true);
        domain.delete(); // Already deleted, no-op
        assertThat(domain.getDeleteStatus()).isTrue();
        assertThat(domain.getState()).isInstanceOf(DeletedProjectState.class);
    }

    @Test
    void testMemento() {
        GenProjectInfoDomain domain = createDomain();
        var memento = domain.saveToMemento();
        assertThat(memento.getProjectInfo().tableName()).isEqualTo("sys_user");
        assertThat(memento.getExtendInfo().requestName()).isEqualTo("sys-user");

        domain.setProjectInfo(new ProjectInfo("changed", "com.changed", "Changed", "chg", "changed"));
        domain.restoreFromMemento(memento);
        assertThat(domain.getProjectInfo().tableName()).isEqualTo("sys_user");
    }

    @Test
    void testSetState() {
        GenProjectInfoDomain domain = createDomain();
        domain.setState(new DeletedProjectState());
        assertThat(domain.getState()).isInstanceOf(DeletedProjectState.class);
    }
}
