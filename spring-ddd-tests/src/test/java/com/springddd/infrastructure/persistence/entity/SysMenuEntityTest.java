package com.springddd.infrastructure.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SysMenuEntity Tests")
class SysMenuEntityTest {

    @Test
    @DisplayName("should create instance with default constructor")
    void create_WithDefaultConstructor_Success() {
        SysMenuEntity entity = new SysMenuEntity();
        assertThat(entity).isNotNull();
    }

    @Test
    @DisplayName("should set and get all fields correctly")
    void setterGetter_AllFields_Success() {
        SysMenuEntity entity = new SysMenuEntity();

        Long id = 1L;
        Long parentId = 0L;
        String name = "Dashboard";
        String path = "/dashboard";
        String component = "Dashboard.vue";
        String api = "/api/dashboard";
        String redirect = "/";
        String permission = "dashboard:view";
        Integer sortOrder = 1;
        String title = "Dashboard";
        Boolean affixTab = true;
        Boolean noBasicLayout = false;
        String icon = "dashboard";
        Integer menuType = 1;
        Boolean visible = true;
        Boolean embedded = false;
        Boolean menuStatus = true;
        Long deptId = 1L;
        Boolean deleteStatus = false;
        String createBy = "admin";
        LocalDateTime createTime = LocalDateTime.now();
        String updateBy = "admin";
        LocalDateTime updateTime = LocalDateTime.now();
        Integer version = 0;

        entity.setId(id);
        entity.setParentId(parentId);
        entity.setName(name);
        entity.setPath(path);
        entity.setComponent(component);
        entity.setApi(api);
        entity.setRedirect(redirect);
        entity.setPermission(permission);
        entity.setSortOrder(sortOrder);
        entity.setTitle(title);
        entity.setAffixTab(affixTab);
        entity.setNoBasicLayout(noBasicLayout);
        entity.setIcon(icon);
        entity.setMenuType(menuType);
        entity.setVisible(visible);
        entity.setEmbedded(embedded);
        entity.setMenuStatus(menuStatus);
        entity.setDeptId(deptId);
        entity.setDeleteStatus(deleteStatus);
        entity.setCreateBy(createBy);
        entity.setCreateTime(createTime);
        entity.setUpdateBy(updateBy);
        entity.setUpdateTime(updateTime);
        entity.setVersion(version);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getParentId()).isEqualTo(parentId);
        assertThat(entity.getName()).isEqualTo(name);
        assertThat(entity.getPath()).isEqualTo(path);
        assertThat(entity.getComponent()).isEqualTo(component);
        assertThat(entity.getApi()).isEqualTo(api);
        assertThat(entity.getRedirect()).isEqualTo(redirect);
        assertThat(entity.getPermission()).isEqualTo(permission);
        assertThat(entity.getSortOrder()).isEqualTo(sortOrder);
        assertThat(entity.getTitle()).isEqualTo(title);
        assertThat(entity.getAffixTab()).isEqualTo(affixTab);
        assertThat(entity.getNoBasicLayout()).isEqualTo(noBasicLayout);
        assertThat(entity.getIcon()).isEqualTo(icon);
        assertThat(entity.getMenuType()).isEqualTo(menuType);
        assertThat(entity.getVisible()).isEqualTo(visible);
        assertThat(entity.getEmbedded()).isEqualTo(embedded);
        assertThat(entity.getMenuStatus()).isEqualTo(menuStatus);
        assertThat(entity.getDeptId()).isEqualTo(deptId);
        assertThat(entity.getDeleteStatus()).isEqualTo(deleteStatus);
        assertThat(entity.getCreateBy()).isEqualTo(createBy);
        assertThat(entity.getCreateTime()).isEqualTo(createTime);
        assertThat(entity.getUpdateBy()).isEqualTo(updateBy);
        assertThat(entity.getUpdateTime()).isEqualTo(updateTime);
        assertThat(entity.getVersion()).isEqualTo(version);
    }

    @Test
    @DisplayName("should handle null values for optional fields")
    void setterGetter_NullValues_Success() {
        SysMenuEntity entity = new SysMenuEntity();

        entity.setId(null);
        entity.setParentId(null);
        entity.setName(null);
        entity.setPath(null);
        entity.setComponent(null);
        entity.setApi(null);
        entity.setRedirect(null);
        entity.setPermission(null);
        entity.setSortOrder(null);
        entity.setTitle(null);
        entity.setAffixTab(null);
        entity.setNoBasicLayout(null);
        entity.setIcon(null);
        entity.setMenuType(null);
        entity.setVisible(null);
        entity.setEmbedded(null);
        entity.setMenuStatus(null);
        entity.setDeptId(null);
        entity.setDeleteStatus(null);
        entity.setCreateBy(null);
        entity.setCreateTime(null);
        entity.setUpdateBy(null);
        entity.setUpdateTime(null);
        entity.setVersion(null);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getParentId()).isNull();
        assertThat(entity.getName()).isNull();
        assertThat(entity.getPath()).isNull();
        assertThat(entity.getComponent()).isNull();
        assertThat(entity.getApi()).isNull();
        assertThat(entity.getRedirect()).isNull();
        assertThat(entity.getPermission()).isNull();
        assertThat(entity.getSortOrder()).isNull();
        assertThat(entity.getTitle()).isNull();
        assertThat(entity.getAffixTab()).isNull();
        assertThat(entity.getNoBasicLayout()).isNull();
        assertThat(entity.getIcon()).isNull();
        assertThat(entity.getMenuType()).isNull();
        assertThat(entity.getVisible()).isNull();
        assertThat(entity.getEmbedded()).isNull();
        assertThat(entity.getMenuStatus()).isNull();
        assertThat(entity.getDeptId()).isNull();
        assertThat(entity.getDeleteStatus()).isNull();
        assertThat(entity.getCreateBy()).isNull();
        assertThat(entity.getCreateTime()).isNull();
        assertThat(entity.getUpdateBy()).isNull();
        assertThat(entity.getUpdateTime()).isNull();
        assertThat(entity.getVersion()).isNull();
    }

    @Test
    @DisplayName("toString should contain all field values")
    void toString_ShouldContainFieldValues() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setName("TestMenu");

        String str = entity.toString();
        assertThat(str).contains("1");
        assertThat(str).contains("TestMenu");
    }
}