package com.springddd.domain.dept.memento;

import com.springddd.domain.dept.DeptBasicInfo;
import com.springddd.domain.dept.DeptExtendInfo;
import com.springddd.domain.dept.DeptId;
import com.springddd.domain.dept.SysDeptDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SysDeptMementoTest {

    @Test
    @DisplayName("应从构造函数正确创建 Memento")
    void constructor_shouldCreateMementoWithCorrectValues() {
        DeptId parentId = new DeptId(1L);
        DeptBasicInfo basicInfo = new DeptBasicInfo("Test Dept");
        DeptExtendInfo extendInfo = new DeptExtendInfo(1, true);

        SysDeptMemento memento = new SysDeptMemento(parentId, basicInfo, extendInfo);

        assertThat(memento.getParentId()).isEqualTo(parentId);
        assertThat(memento.getDeptBasicInfo()).isEqualTo(basicInfo);
        assertThat(memento.getDeptExtendInfo()).isEqualTo(extendInfo);
    }

    @Test
    @DisplayName("应从聚合根正确创建 Memento")
    void saveToMemento_shouldCreateMementoFromDomain() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setParentId(new DeptId(2L));
        domain.setDeptBasicInfo(new DeptBasicInfo("HR Dept"));
        domain.setDeptExtendInfo(new DeptExtendInfo(2, false));

        SysDeptMemento memento = domain.saveToMemento();

        assertThat(memento.getParentId().value()).isEqualTo(2L);
        assertThat(memento.getDeptBasicInfo().deptName()).isEqualTo("HR Dept");
        assertThat(memento.getDeptExtendInfo().sortOrder()).isEqualTo(2);
        assertThat(memento.getDeptExtendInfo().deptStatus()).isFalse();
    }

    @Test
    @DisplayName("应从 Memento 正确恢复聚合根")
    void restoreFromMemento_shouldRestoreDomainValues() {
        SysDeptDomain domain = new SysDeptDomain();
        SysDeptMemento memento = new SysDeptMemento(
                new DeptId(3L),
                new DeptBasicInfo("Finance Dept"),
                new DeptExtendInfo(3, true)
        );

        domain.restoreFromMemento(memento);

        assertThat(domain.getParentId().value()).isEqualTo(3L);
        assertThat(domain.getDeptBasicInfo().deptName()).isEqualTo("Finance Dept");
        assertThat(domain.getDeptExtendInfo().sortOrder()).isEqualTo(3);
        assertThat(domain.getDeptExtendInfo().deptStatus()).isTrue();
    }

    @Test
    @DisplayName("Memento 字段值应通过 getter 正确返回")
    void getters_shouldReturnCorrectFieldValues() {
        DeptId parentId = new DeptId(5L);
        DeptBasicInfo basicInfo = new DeptBasicInfo("IT Dept");
        DeptExtendInfo extendInfo = new DeptExtendInfo(5, true);

        SysDeptMemento memento = new SysDeptMemento(parentId, basicInfo, extendInfo);

        assertThat(memento.getParentId().value()).isEqualTo(5L);
        assertThat(memento.getDeptBasicInfo().deptName()).isEqualTo("IT Dept");
        assertThat(memento.getDeptExtendInfo().sortOrder()).isEqualTo(5);
        assertThat(memento.getDeptExtendInfo().deptStatus()).isTrue();
    }
}
