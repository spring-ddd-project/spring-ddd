package com.springddd.application.service.menu.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuDtoTest {

    @Test
    @DisplayName("SysMenuCommand 应支持无参构造和 setter/getter")
    void sysMenuCommand_shouldSupportGetterSetter() {
        SysMenuCommand command = new SysMenuCommand();
        command.setId(1L);
        command.setParentId(2L);
        command.setName("TestMenu");
        command.setPath("/test");
        command.setComponent("Layout");
        command.setRedirect("/redirect");
        command.setApi("/api/test");
        command.setPermission("sys:menu:list");
        command.setOrder(1);
        command.setTitle("Test Title");
        command.setAffixTab(true);
        command.setNoBasicLayout(false);
        command.setIcon("icon");
        command.setMenuType(1);
        command.setVisible(true);
        command.setEmbedded(false);
        command.setMenuStatus(true);
        command.setDeptId(3L);
        command.setDeleteStatus(false);

        assertThat(command.getId()).isEqualTo(1L);
        assertThat(command.getParentId()).isEqualTo(2L);
        assertThat(command.getName()).isEqualTo("TestMenu");
        assertThat(command.getPath()).isEqualTo("/test");
        assertThat(command.getComponent()).isEqualTo("Layout");
        assertThat(command.getRedirect()).isEqualTo("/redirect");
        assertThat(command.getApi()).isEqualTo("/api/test");
        assertThat(command.getPermission()).isEqualTo("sys:menu:list");
        assertThat(command.getOrder()).isEqualTo(1);
        assertThat(command.getTitle()).isEqualTo("Test Title");
        assertThat(command.getAffixTab()).isTrue();
        assertThat(command.getNoBasicLayout()).isFalse();
        assertThat(command.getIcon()).isEqualTo("icon");
        assertThat(command.getMenuType()).isEqualTo(1);
        assertThat(command.getVisible()).isTrue();
        assertThat(command.getEmbedded()).isFalse();
        assertThat(command.getMenuStatus()).isTrue();
        assertThat(command.getDeptId()).isEqualTo(3L);
        assertThat(command.getDeleteStatus()).isFalse();
    }

    @Test
    @DisplayName("SysMenuCommand 初始值应为 null")
    void sysMenuCommand_shouldHaveNullInitialValues() {
        SysMenuCommand command = new SysMenuCommand();

        assertThat(command.getId()).isNull();
        assertThat(command.getParentId()).isNull();
        assertThat(command.getName()).isNull();
        assertThat(command.getPath()).isNull();
        assertThat(command.getComponent()).isNull();
        assertThat(command.getRedirect()).isNull();
        assertThat(command.getApi()).isNull();
        assertThat(command.getPermission()).isNull();
        assertThat(command.getOrder()).isNull();
        assertThat(command.getTitle()).isNull();
        assertThat(command.getAffixTab()).isNull();
        assertThat(command.getNoBasicLayout()).isNull();
        assertThat(command.getIcon()).isNull();
        assertThat(command.getMenuType()).isNull();
        assertThat(command.getVisible()).isNull();
        assertThat(command.getEmbedded()).isNull();
        assertThat(command.getMenuStatus()).isNull();
        assertThat(command.getDeptId()).isNull();
        assertThat(command.getDeleteStatus()).isNull();
    }

    @Test
    @DisplayName("SysMenuQuery 应支持无参构造和 setter/getter")
    void sysMenuQuery_shouldSupportGetterSetter() {
        SysMenuQuery query = new SysMenuQuery();
        query.setId(1L);
        query.setParentId(2L);
        query.setName("TestMenu");
        query.setPath("/test");
        query.setComponent("Layout");
        query.setApi("/api/test");
        query.setRedirect("/redirect");
        query.setPermission("sys:menu:list");
        query.setOrder(1);
        query.setTitle("Test Title");
        query.setAffixTab(true);
        query.setNoBasicLayout(false);
        query.setIcon("icon");
        query.setMenuType(1);
        query.setVisible(true);
        query.setEmbedded(false);
        query.setMenuStatus(true);
        query.setDeptId(3L);
        query.setDeleteStatus(false);

        assertThat(query.getId()).isEqualTo(1L);
        assertThat(query.getParentId()).isEqualTo(2L);
        assertThat(query.getName()).isEqualTo("TestMenu");
        assertThat(query.getPath()).isEqualTo("/test");
        assertThat(query.getComponent()).isEqualTo("Layout");
        assertThat(query.getApi()).isEqualTo("/api/test");
        assertThat(query.getRedirect()).isEqualTo("/redirect");
        assertThat(query.getPermission()).isEqualTo("sys:menu:list");
        assertThat(query.getOrder()).isEqualTo(1);
        assertThat(query.getTitle()).isEqualTo("Test Title");
        assertThat(query.getAffixTab()).isTrue();
        assertThat(query.getNoBasicLayout()).isFalse();
        assertThat(query.getIcon()).isEqualTo("icon");
        assertThat(query.getMenuType()).isEqualTo(1);
        assertThat(query.getVisible()).isTrue();
        assertThat(query.getEmbedded()).isFalse();
        assertThat(query.getMenuStatus()).isTrue();
        assertThat(query.getDeptId()).isEqualTo(3L);
        assertThat(query.getDeleteStatus()).isFalse();
    }

    @Test
    @DisplayName("SysMenuQuery 应支持 FieldNameConstants")
    void sysMenuQuery_shouldSupportFieldNameConstants() {
        assertThat(SysMenuQuery.Fields.id).isEqualTo("id");
        assertThat(SysMenuQuery.Fields.parentId).isEqualTo("parentId");
        assertThat(SysMenuQuery.Fields.name).isEqualTo("name");
        assertThat(SysMenuQuery.Fields.path).isEqualTo("path");
        assertThat(SysMenuQuery.Fields.component).isEqualTo("component");
        assertThat(SysMenuQuery.Fields.api).isEqualTo("api");
        assertThat(SysMenuQuery.Fields.redirect).isEqualTo("redirect");
        assertThat(SysMenuQuery.Fields.permission).isEqualTo("permission");
        assertThat(SysMenuQuery.Fields.order).isEqualTo("order");
        assertThat(SysMenuQuery.Fields.title).isEqualTo("title");
        assertThat(SysMenuQuery.Fields.affixTab).isEqualTo("affixTab");
        assertThat(SysMenuQuery.Fields.noBasicLayout).isEqualTo("noBasicLayout");
        assertThat(SysMenuQuery.Fields.icon).isEqualTo("icon");
        assertThat(SysMenuQuery.Fields.menuType).isEqualTo("menuType");
        assertThat(SysMenuQuery.Fields.visible).isEqualTo("visible");
        assertThat(SysMenuQuery.Fields.embedded).isEqualTo("embedded");
        assertThat(SysMenuQuery.Fields.menuStatus).isEqualTo("menuStatus");
        assertThat(SysMenuQuery.Fields.deptId).isEqualTo("deptId");
        assertThat(SysMenuQuery.Fields.deleteStatus).isEqualTo("deleteStatus");
    }

    @Test
    @DisplayName("SysMenuPageQuery 应支持无参构造和 setter/getter")
    void sysMenuPageQuery_shouldSupportGetterSetter() {
        SysMenuPageQuery pageQuery = new SysMenuPageQuery();
        pageQuery.setId(1L);
        pageQuery.setName("TestMenu");
        pageQuery.setPageNum(1);
        pageQuery.setPageSize(10);

        assertThat(pageQuery.getId()).isEqualTo(1L);
        assertThat(pageQuery.getName()).isEqualTo("TestMenu");
        assertThat(pageQuery.getPageNum()).isEqualTo(1);
        assertThat(pageQuery.getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("SysMenuPageQuery 应支持 equals 和 hashCode")
    void sysMenuPageQuery_shouldSupportEqualsAndHashCode() {
        SysMenuPageQuery pageQuery1 = new SysMenuPageQuery();
        pageQuery1.setId(1L);
        pageQuery1.setPageNum(1);
        pageQuery1.setPageSize(10);

        SysMenuPageQuery pageQuery2 = new SysMenuPageQuery();
        pageQuery2.setId(1L);
        pageQuery2.setPageNum(1);
        pageQuery2.setPageSize(10);

        assertThat(pageQuery1).isEqualTo(pageQuery2);
        assertThat(pageQuery1.hashCode()).isEqualTo(pageQuery2.hashCode());
    }

    @Test
    @DisplayName("SysMenuView 应支持无参构造和 setter/getter")
    void sysMenuView_shouldSupportGetterSetter() {
        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setParentId(2L);
        view.setName("TestMenu");
        view.setPath("/test");
        view.setComponent("Layout");
        view.setApi("/api/test");
        view.setRedirect("/redirect");
        view.setPermission("sys:menu:list");
        view.setMenuType(1);
        view.setVisible(true);
        view.setEmbedded(false);
        view.setMenuStatus(true);
        view.setDeptId(3L);
        view.setDeleteStatus(false);

        SysMenuView.Meta meta = new SysMenuView.Meta();
        meta.setOrder(1);
        meta.setTitle("Test Title");
        meta.setAffixTab(true);
        meta.setNoBasicLayout(false);
        meta.setIcon("icon");
        view.setMeta(meta);

        SysMenuView child = new SysMenuView();
        child.setId(4L);
        view.setChildren(List.of(child));

        assertThat(view.getId()).isEqualTo(1L);
        assertThat(view.getParentId()).isEqualTo(2L);
        assertThat(view.getName()).isEqualTo("TestMenu");
        assertThat(view.getPath()).isEqualTo("/test");
        assertThat(view.getComponent()).isEqualTo("Layout");
        assertThat(view.getApi()).isEqualTo("/api/test");
        assertThat(view.getRedirect()).isEqualTo("/redirect");
        assertThat(view.getPermission()).isEqualTo("sys:menu:list");
        assertThat(view.getMenuType()).isEqualTo(1);
        assertThat(view.getVisible()).isTrue();
        assertThat(view.getEmbedded()).isFalse();
        assertThat(view.getMenuStatus()).isTrue();
        assertThat(view.getDeptId()).isEqualTo(3L);
        assertThat(view.getDeleteStatus()).isFalse();
        assertThat(view.getMeta()).isNotNull();
        assertThat(view.getMeta().getOrder()).isEqualTo(1);
        assertThat(view.getMeta().getTitle()).isEqualTo("Test Title");
        assertThat(view.getMeta().getAffixTab()).isTrue();
        assertThat(view.getMeta().getNoBasicLayout()).isFalse();
        assertThat(view.getMeta().getIcon()).isEqualTo("icon");
        assertThat(view.getChildren()).hasSize(1);
        assertThat(view.getChildren().get(0).getId()).isEqualTo(4L);
    }
}
