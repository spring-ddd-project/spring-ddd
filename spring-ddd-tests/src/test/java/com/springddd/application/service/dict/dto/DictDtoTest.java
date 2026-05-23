package com.springddd.application.service.dict.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DictDtoTest {

    @Test
    @DisplayName("SysDictCommand 应支持无参构造和 setter/getter")
    void sysDictCommand_shouldSupportGetterSetter() {
        SysDictCommand command = new SysDictCommand();
        command.setId(1L);
        command.setDictName("TestDict");
        command.setDictCode("test_dict");
        command.setSortOrder(2);
        command.setDictStatus(true);
        command.setDeleteStatus(false);

        assertThat(command.getId()).isEqualTo(1L);
        assertThat(command.getDictName()).isEqualTo("TestDict");
        assertThat(command.getDictCode()).isEqualTo("test_dict");
        assertThat(command.getSortOrder()).isEqualTo(2);
        assertThat(command.getDictStatus()).isTrue();
        assertThat(command.getDeleteStatus()).isFalse();
    }

    @Test
    @DisplayName("SysDictCommand 初始值应为 null")
    void sysDictCommand_shouldHaveNullInitialValues() {
        SysDictCommand command = new SysDictCommand();

        assertThat(command.getId()).isNull();
        assertThat(command.getDictName()).isNull();
        assertThat(command.getDictCode()).isNull();
        assertThat(command.getSortOrder()).isNull();
        assertThat(command.getDictStatus()).isNull();
        assertThat(command.getDeleteStatus()).isNull();
    }

    @Test
    @DisplayName("SysDictItemCommand 应支持无参构造和 setter/getter")
    void sysDictItemCommand_shouldSupportGetterSetter() {
        SysDictItemCommand command = new SysDictItemCommand();
        command.setId(1L);
        command.setDictId(2L);
        command.setItemLabel("TestLabel");
        command.setItemValue(3);
        command.setSortOrder(4);
        command.setItemStatus(true);
        command.setDeleteStatus(false);

        assertThat(command.getId()).isEqualTo(1L);
        assertThat(command.getDictId()).isEqualTo(2L);
        assertThat(command.getItemLabel()).isEqualTo("TestLabel");
        assertThat(command.getItemValue()).isEqualTo(3);
        assertThat(command.getSortOrder()).isEqualTo(4);
        assertThat(command.getItemStatus()).isTrue();
        assertThat(command.getDeleteStatus()).isFalse();
    }

    @Test
    @DisplayName("SysDictItemQuery 应支持无参构造和 setter/getter")
    void sysDictItemQuery_shouldSupportGetterSetter() {
        SysDictItemQuery query = new SysDictItemQuery();
        query.setId(1L);
        query.setDictId(2L);
        query.setItemLabel("TestLabel");
        query.setItemValue(3);
        query.setSortOrder(4);
        query.setItemStatus(true);
        query.setDeleteStatus(false);

        assertThat(query.getId()).isEqualTo(1L);
        assertThat(query.getDictId()).isEqualTo(2L);
        assertThat(query.getItemLabel()).isEqualTo("TestLabel");
        assertThat(query.getItemValue()).isEqualTo(3);
        assertThat(query.getSortOrder()).isEqualTo(4);
        assertThat(query.getItemStatus()).isTrue();
        assertThat(query.getDeleteStatus()).isFalse();
    }

    @Test
    @DisplayName("SysDictItemQuery 应支持 FieldNameConstants")
    void sysDictItemQuery_shouldSupportFieldNameConstants() {
        assertThat(SysDictItemQuery.Fields.id).isEqualTo("id");
        assertThat(SysDictItemQuery.Fields.dictId).isEqualTo("dictId");
        assertThat(SysDictItemQuery.Fields.itemLabel).isEqualTo("itemLabel");
        assertThat(SysDictItemQuery.Fields.itemValue).isEqualTo("itemValue");
        assertThat(SysDictItemQuery.Fields.sortOrder).isEqualTo("sortOrder");
        assertThat(SysDictItemQuery.Fields.itemStatus).isEqualTo("itemStatus");
        assertThat(SysDictItemQuery.Fields.deleteStatus).isEqualTo("deleteStatus");
    }

    @Test
    @DisplayName("SysDictItemPageQuery 应支持无参构造和 setter/getter")
    void sysDictItemPageQuery_shouldSupportGetterSetter() {
        SysDictItemPageQuery pageQuery = new SysDictItemPageQuery();
        pageQuery.setId(1L);
        pageQuery.setDictId(2L);
        pageQuery.setItemLabel("TestLabel");
        pageQuery.setItemValue(3);
        pageQuery.setSortOrder(4);
        pageQuery.setItemStatus(true);
        pageQuery.setDeleteStatus(false);
        pageQuery.setPageNum(1);
        pageQuery.setPageSize(10);

        assertThat(pageQuery.getId()).isEqualTo(1L);
        assertThat(pageQuery.getDictId()).isEqualTo(2L);
        assertThat(pageQuery.getItemLabel()).isEqualTo("TestLabel");
        assertThat(pageQuery.getItemValue()).isEqualTo(3);
        assertThat(pageQuery.getSortOrder()).isEqualTo(4);
        assertThat(pageQuery.getItemStatus()).isTrue();
        assertThat(pageQuery.getDeleteStatus()).isFalse();
        assertThat(pageQuery.getPageNum()).isEqualTo(1);
        assertThat(pageQuery.getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("SysDictItemPageQuery 应支持 equals 和 hashCode")
    void sysDictItemPageQuery_shouldSupportEqualsAndHashCode() {
        SysDictItemPageQuery pageQuery1 = new SysDictItemPageQuery();
        pageQuery1.setId(1L);
        pageQuery1.setPageNum(1);
        pageQuery1.setPageSize(10);

        SysDictItemPageQuery pageQuery2 = new SysDictItemPageQuery();
        pageQuery2.setId(1L);
        pageQuery2.setPageNum(1);
        pageQuery2.setPageSize(10);

        assertThat(pageQuery1).isEqualTo(pageQuery2);
        assertThat(pageQuery1.hashCode()).isEqualTo(pageQuery2.hashCode());
    }

    @Test
    @DisplayName("SysDictItemView 应支持无参构造和 setter/getter")
    void sysDictItemView_shouldSupportGetterSetter() {
        SysDictItemView view = new SysDictItemView();
        view.setId(1L);
        view.setDictId(2L);
        view.setItemLabel("TestLabel");
        view.setItemValue(3);
        view.setSortOrder(4);
        view.setItemStatus(true);
        view.setDeleteStatus(false);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getDictId()).isEqualTo(2L);
        assertThat(view.getItemLabel()).isEqualTo("TestLabel");
        assertThat(view.getItemValue()).isEqualTo(3);
        assertThat(view.getSortOrder()).isEqualTo(4);
        assertThat(view.getItemStatus()).isTrue();
        assertThat(view.getDeleteStatus()).isFalse();
    }

    @Test
    @DisplayName("SysDictQuery 应支持无参构造和 setter/getter")
    void sysDictQuery_shouldSupportGetterSetter() {
        SysDictQuery query = new SysDictQuery();
        query.setId(1L);
        query.setDictName("TestDict");
        query.setDictCode("test_dict");
        query.setSortOrder(2);
        query.setDictStatus(true);
        query.setDeleteStatus(false);

        assertThat(query.getId()).isEqualTo(1L);
        assertThat(query.getDictName()).isEqualTo("TestDict");
        assertThat(query.getDictCode()).isEqualTo("test_dict");
        assertThat(query.getSortOrder()).isEqualTo(2);
        assertThat(query.getDictStatus()).isTrue();
        assertThat(query.getDeleteStatus()).isFalse();
    }

    @Test
    @DisplayName("SysDictQuery 应支持 FieldNameConstants")
    void sysDictQuery_shouldSupportFieldNameConstants() {
        assertThat(SysDictQuery.Fields.id).isEqualTo("id");
        assertThat(SysDictQuery.Fields.dictName).isEqualTo("dictName");
        assertThat(SysDictQuery.Fields.dictCode).isEqualTo("dictCode");
        assertThat(SysDictQuery.Fields.sortOrder).isEqualTo("sortOrder");
        assertThat(SysDictQuery.Fields.dictStatus).isEqualTo("dictStatus");
        assertThat(SysDictQuery.Fields.deleteStatus).isEqualTo("deleteStatus");
    }

    @Test
    @DisplayName("SysDictPageQuery 应支持无参构造和 setter/getter")
    void sysDictPageQuery_shouldSupportGetterSetter() {
        SysDictPageQuery pageQuery = new SysDictPageQuery();
        pageQuery.setId(1L);
        pageQuery.setDictName("TestDict");
        pageQuery.setDictCode("test_dict");
        pageQuery.setSortOrder(2);
        pageQuery.setDictStatus(true);
        pageQuery.setDeleteStatus(false);
        pageQuery.setPageNum(1);
        pageQuery.setPageSize(10);

        assertThat(pageQuery.getId()).isEqualTo(1L);
        assertThat(pageQuery.getDictName()).isEqualTo("TestDict");
        assertThat(pageQuery.getDictCode()).isEqualTo("test_dict");
        assertThat(pageQuery.getSortOrder()).isEqualTo(2);
        assertThat(pageQuery.getDictStatus()).isTrue();
        assertThat(pageQuery.getDeleteStatus()).isFalse();
        assertThat(pageQuery.getPageNum()).isEqualTo(1);
        assertThat(pageQuery.getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("SysDictPageQuery 应支持 equals 和 hashCode")
    void sysDictPageQuery_shouldSupportEqualsAndHashCode() {
        SysDictPageQuery pageQuery1 = new SysDictPageQuery();
        pageQuery1.setId(1L);
        pageQuery1.setPageNum(1);
        pageQuery1.setPageSize(10);

        SysDictPageQuery pageQuery2 = new SysDictPageQuery();
        pageQuery2.setId(1L);
        pageQuery2.setPageNum(1);
        pageQuery2.setPageSize(10);

        assertThat(pageQuery1).isEqualTo(pageQuery2);
        assertThat(pageQuery1.hashCode()).isEqualTo(pageQuery2.hashCode());
    }

    @Test
    @DisplayName("SysDictView 应支持无参构造和 setter/getter")
    void sysDictView_shouldSupportGetterSetter() {
        SysDictView view = new SysDictView();
        view.setId(1L);
        view.setDictName("TestDict");
        view.setDictCode("test_dict");
        view.setSortOrder(2);
        view.setDictStatus(true);
        view.setDeleteStatus(false);

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getDictName()).isEqualTo("TestDict");
        assertThat(view.getDictCode()).isEqualTo("test_dict");
        assertThat(view.getSortOrder()).isEqualTo(2);
        assertThat(view.getDictStatus()).isTrue();
        assertThat(view.getDeleteStatus()).isFalse();
    }
}
