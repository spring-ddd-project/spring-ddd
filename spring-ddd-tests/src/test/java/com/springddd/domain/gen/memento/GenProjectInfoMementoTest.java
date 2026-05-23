package com.springddd.domain.gen.memento;

import com.springddd.domain.gen.GenProjectInfoDomain;
import com.springddd.domain.gen.GenProjectInfoExtendInfo;
import com.springddd.domain.gen.ProjectInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenProjectInfoMementoTest {

    @Test
    @DisplayName("应从构造函数正确创建 Memento")
    void constructor_shouldCreateMementoWithCorrectValues() {
        ProjectInfo projectInfo = new ProjectInfo("sys_user", "com.example", "User", "system", "Test Project");
        GenProjectInfoExtendInfo extendInfo = new GenProjectInfoExtendInfo("/api", "1.0.0");

        GenProjectInfoMemento memento = new GenProjectInfoMemento(projectInfo, extendInfo);

        assertThat(memento.getProjectInfo()).isEqualTo(projectInfo);
        assertThat(memento.getExtendInfo()).isEqualTo(extendInfo);
    }

    @Test
    @DisplayName("应从聚合根正确创建 Memento")
    void saveToMemento_shouldCreateMementoFromDomain() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.setProjectInfo(new ProjectInfo("sys_role", "com.test", "Role", "auth", "Role Project"));
        domain.setExtendInfo(new GenProjectInfoExtendInfo("/role", "2.0.0"));

        GenProjectInfoMemento memento = domain.saveToMemento();

        assertThat(memento.getProjectInfo().tableName()).isEqualTo("sys_role");
        assertThat(memento.getProjectInfo().packageName()).isEqualTo("com.test");
        assertThat(memento.getProjectInfo().className()).isEqualTo("Role");
        assertThat(memento.getProjectInfo().moduleName()).isEqualTo("auth");
        assertThat(memento.getProjectInfo().projectName()).isEqualTo("Role Project");
        assertThat(memento.getExtendInfo().requestName()).isEqualTo("/role");
        assertThat(memento.getExtendInfo().projectVersion()).isEqualTo("2.0.0");
    }

    @Test
    @DisplayName("应从 Memento 正确恢复聚合根")
    void restoreFromMemento_shouldRestoreDomainValues() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        GenProjectInfoMemento memento = new GenProjectInfoMemento(
                new ProjectInfo("sys_menu", "com.demo", "Menu", "ui", "Menu Project"),
                new GenProjectInfoExtendInfo("/menu", "3.0.0")
        );

        domain.restoreFromMemento(memento);

        assertThat(domain.getProjectInfo().tableName()).isEqualTo("sys_menu");
        assertThat(domain.getProjectInfo().packageName()).isEqualTo("com.demo");
        assertThat(domain.getExtendInfo().requestName()).isEqualTo("/menu");
        assertThat(domain.getExtendInfo().projectVersion()).isEqualTo("3.0.0");
    }

    @Test
    @DisplayName("Memento 字段值应通过 getter 正确返回")
    void getters_shouldReturnCorrectFieldValues() {
        ProjectInfo projectInfo = new ProjectInfo("sys_dept", "com.org", "Dept", "org", "Dept Project");
        GenProjectInfoExtendInfo extendInfo = new GenProjectInfoExtendInfo("/dept", "1.1.0");

        GenProjectInfoMemento memento = new GenProjectInfoMemento(projectInfo, extendInfo);

        assertThat(memento.getProjectInfo().tableName()).isEqualTo("sys_dept");
        assertThat(memento.getProjectInfo().className()).isEqualTo("Dept");
        assertThat(memento.getExtendInfo().requestName()).isEqualTo("/dept");
        assertThat(memento.getExtendInfo().projectVersion()).isEqualTo("1.1.0");
    }
}
