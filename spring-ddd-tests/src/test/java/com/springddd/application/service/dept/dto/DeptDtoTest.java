package com.springddd.application.service.dept.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DeptDtoTest {

    @Test
    @DisplayName("SysDeptCommand 应支持无参构造和 setter/getter")
    void sysDeptCommand_shouldSupportGetterSetter() {
        SysDeptCommand command = new SysDeptCommand();
        command.setId(1L);
        command.setParentId(2L);
        command.setDeptName("TestDept");
        command.setSortOrder(3);
        command.setDeptStatus(true);
        command.setDeleteStatus(false);

        assertThat(command.getId()).isEqualTo(1L);
        assertThat(command.getParentId()).isEqualTo(2L);
        assertThat(command.getDeptName()).isEqualTo("TestDept");
        assertThat(command.getSortOrder()).isEqualTo(3);
        assertThat(command.getDeptStatus()).isTrue();
        assertThat(command.getDeleteStatus()).isFalse();
    }

    @Test
    @DisplayName("SysDeptCommand 初始值应为 null")
    void sysDeptCommand_shouldHaveNullInitialValues() {
        SysDeptCommand command = new SysDeptCommand();

        assertThat(command.getId()).isNull();
        assertThat(command.getParentId()).isNull();
        assertThat(command.getDeptName()).isNull();
        assertThat(command.getSortOrder()).isNull();
        assertThat(command.getDeptStatus()).isNull();
        assertThat(command.getDeleteStatus()).isNull();
    }

    @Test
    @DisplayName("SysDeptQuery 应支持无参构造和 setter/getter")
    void sysDeptQuery_shouldSupportGetterSetter() {
        SysDeptQuery query = new SysDeptQuery();
        query.setId(1L);
        query.setParentId(2L);
        query.setDeptName("TestDept");
        query.setSortOrder(3);
        query.setDeptStatus(true);
        query.setDeleteStatus(false);

        assertThat(query.getId()).isEqualTo(1L);
        assertThat(query.getParentId()).isEqualTo(2L);
        assertThat(query.getDeptName()).isEqualTo("TestDept");
        assertThat(query.getSortOrder()).isEqualTo(3);
        assertThat(query.getDeptStatus()).isTrue();
        assertThat(query.getDeleteStatus()).isFalse();
    }

    @Test
    @DisplayName("SysDeptQuery 应支持 FieldNameConstants")
    void sysDeptQuery_shouldSupportFieldNameConstants() {
        assertThat(SysDeptQuery.Fields.id).isEqualTo("id");
        assertThat(SysDeptQuery.Fields.parentId).isEqualTo("parentId");
        assertThat(SysDeptQuery.Fields.deptName).isEqualTo("deptName");
        assertThat(SysDeptQuery.Fields.sortOrder).isEqualTo("sortOrder");
        assertThat(SysDeptQuery.Fields.deptStatus).isEqualTo("deptStatus");
        assertThat(SysDeptQuery.Fields.deleteStatus).isEqualTo("deleteStatus");
    }

    @Test
    @DisplayName("SysDeptPageQuery 应支持无参构造和 setter/getter")
    void sysDeptPageQuery_shouldSupportGetterSetter() {
        SysDeptPageQuery pageQuery = new SysDeptPageQuery();
        pageQuery.setId(1L);
        pageQuery.setParentId(2L);
        pageQuery.setDeptName("TestDept");
        pageQuery.setSortOrder(3);
        pageQuery.setDeptStatus(true);
        pageQuery.setDeleteStatus(false);
        pageQuery.setPageNum(1);
        pageQuery.setPageSize(10);

        assertThat(pageQuery.getId()).isEqualTo(1L);
        assertThat(pageQuery.getParentId()).isEqualTo(2L);
        assertThat(pageQuery.getDeptName()).isEqualTo("TestDept");
        assertThat(pageQuery.getSortOrder()).isEqualTo(3);
        assertThat(pageQuery.getDeptStatus()).isTrue();
        assertThat(pageQuery.getDeleteStatus()).isFalse();
        assertThat(pageQuery.getPageNum()).isEqualTo(1);
        assertThat(pageQuery.getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("SysDeptPageQuery 应支持 equals 和 hashCode")
    void sysDeptPageQuery_shouldSupportEqualsAndHashCode() {
        SysDeptPageQuery pageQuery1 = new SysDeptPageQuery();
        pageQuery1.setId(1L);
        pageQuery1.setPageNum(1);
        pageQuery1.setPageSize(10);

        SysDeptPageQuery pageQuery2 = new SysDeptPageQuery();
        pageQuery2.setId(1L);
        pageQuery2.setPageNum(1);
        pageQuery2.setPageSize(10);

        assertThat(pageQuery1).isEqualTo(pageQuery2);
        assertThat(pageQuery1.hashCode()).isEqualTo(pageQuery2.hashCode());
    }

    @Test
    @DisplayName("SysDeptView 应支持无参构造和 setter/getter")
    void sysDeptView_shouldSupportGetterSetter() {
        SysDeptView view = new SysDeptView();
        view.setId(1L);
        view.setParentId(2L);
        view.setDeptName("TestDept");
        view.setSortOrder(3);
        view.setDeptStatus(true);
        view.setDeleteStatus(false);

        SysDeptView child = new SysDeptView();
        child.setId(3L);
        view.setChildren(List.of(child));

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getParentId()).isEqualTo(2L);
        assertThat(view.getDeptName()).isEqualTo("TestDept");
        assertThat(view.getSortOrder()).isEqualTo(3);
        assertThat(view.getDeptStatus()).isTrue();
        assertThat(view.getDeleteStatus()).isFalse();
        assertThat(view.getChildren()).hasSize(1);
        assertThat(view.getChildren().get(0).getId()).isEqualTo(3L);
    }
}
