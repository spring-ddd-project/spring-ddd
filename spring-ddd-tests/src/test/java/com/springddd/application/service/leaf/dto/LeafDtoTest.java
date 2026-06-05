package com.springddd.application.service.leaf.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class LeafDtoTest {

    @Test
    @DisplayName("LeafAllocCommand 应支持无参构造和 setter/getter")
    void leafAllocCommand_shouldSupportGetterSetter() {
        LeafAllocCommand command = new LeafAllocCommand();
        command.setId(1L);
        command.setBizTag("test_tag");
        command.setMaxId(100L);
        command.setStep(10);
        command.setDescription("Test description");
        command.setDeptId(1L);

        assertThat(command.getId()).isEqualTo(1L);
        assertThat(command.getBizTag()).isEqualTo("test_tag");
        assertThat(command.getMaxId()).isEqualTo(100L);
        assertThat(command.getStep()).isEqualTo(10);
        assertThat(command.getDescription()).isEqualTo("Test description");
        assertThat(command.getDeptId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("LeafAllocCommand 初始值应为 null")
    void leafAllocCommand_shouldHaveNullInitialValues() {
        LeafAllocCommand command = new LeafAllocCommand();

        assertThat(command.getId()).isNull();
        assertThat(command.getBizTag()).isNull();
        assertThat(command.getMaxId()).isNull();
        assertThat(command.getStep()).isNull();
        assertThat(command.getDescription()).isNull();
        assertThat(command.getDeptId()).isNull();
    }

    @Test
    @DisplayName("LeafAllocQuery 应支持无参构造和 setter/getter")
    void leafAllocQuery_shouldSupportGetterSetter() {
        LeafAllocQuery query = new LeafAllocQuery();
        query.setBizTag("test_tag");
        query.setDeleteStatus(false);

        assertThat(query.getBizTag()).isEqualTo("test_tag");
        assertThat(query.getDeleteStatus()).isFalse();
    }

    @Test
    @DisplayName("LeafAllocQuery 应支持 FieldNameConstants")
    void leafAllocQuery_shouldSupportFieldNameConstants() {
        assertThat(LeafAllocQuery.Fields.bizTag).isEqualTo("bizTag");
        assertThat(LeafAllocQuery.Fields.deleteStatus).isEqualTo("deleteStatus");
    }

    @Test
    @DisplayName("LeafAllocPageQuery 应支持无参构造和 setter/getter")
    void leafAllocPageQuery_shouldSupportGetterSetter() {
        LeafAllocPageQuery pageQuery = new LeafAllocPageQuery();
        pageQuery.setBizTag("test_tag");
        pageQuery.setDeleteStatus(false);
        pageQuery.setPageNum(1);
        pageQuery.setPageSize(10);

        assertThat(pageQuery.getBizTag()).isEqualTo("test_tag");
        assertThat(pageQuery.getDeleteStatus()).isFalse();
        assertThat(pageQuery.getPageNum()).isEqualTo(1);
        assertThat(pageQuery.getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("LeafAllocPageQuery 应支持 equals 和 hashCode")
    void leafAllocPageQuery_shouldSupportEqualsAndHashCode() {
        LeafAllocPageQuery pageQuery1 = new LeafAllocPageQuery();
        pageQuery1.setBizTag("test");
        pageQuery1.setPageNum(1);
        pageQuery1.setPageSize(10);

        LeafAllocPageQuery pageQuery2 = new LeafAllocPageQuery();
        pageQuery2.setBizTag("test");
        pageQuery2.setPageNum(1);
        pageQuery2.setPageSize(10);

        assertThat(pageQuery1).isEqualTo(pageQuery2);
        assertThat(pageQuery1.hashCode()).isEqualTo(pageQuery2.hashCode());
    }

    @Test
    @DisplayName("LeafAllocView 应支持无参构造和 setter/getter")
    void leafAllocView_shouldSupportGetterSetter() {
        LeafAllocView view = new LeafAllocView();
        view.setId(1L);
        view.setBizTag("test_tag");
        view.setMaxId(100L);
        view.setStep(10);
        view.setDescription("Test description");
        view.setUpdateTime(LocalDateTime.of(2024, 1, 1, 12, 0));
        view.setVersion(1);
        view.setDeleteStatus(false);
        view.setCreateBy("admin");
        view.setCreateTime(LocalDateTime.of(2024, 1, 1, 10, 0));
        view.setUpdateBy("admin");
        view.setDeptId(1L);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getBizTag()).isEqualTo("test_tag");
        assertThat(view.getMaxId()).isEqualTo(100L);
        assertThat(view.getStep()).isEqualTo(10);
        assertThat(view.getDescription()).isEqualTo("Test description");
        assertThat(view.getUpdateTime()).isEqualTo(LocalDateTime.of(2024, 1, 1, 12, 0));
        assertThat(view.getVersion()).isEqualTo(1);
        assertThat(view.getDeleteStatus()).isFalse();
        assertThat(view.getCreateBy()).isEqualTo("admin");
        assertThat(view.getCreateTime()).isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 0));
        assertThat(view.getUpdateBy()).isEqualTo("admin");
        assertThat(view.getDeptId()).isEqualTo(1L);
    }
}
