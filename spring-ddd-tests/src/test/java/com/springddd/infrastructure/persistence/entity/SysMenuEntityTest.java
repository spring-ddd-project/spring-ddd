package com.springddd.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class SysMenuEntityTest {

    @Test
    void shouldCreateSysMenuEntityWithDefaultConstructor() {
        SysMenuEntity entity = new SysMenuEntity();
        assertNotNull(entity);
    }

    @Test
    void shouldSetAndGetId() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        assertEquals(1L, entity.getId());
    }

    @Test
    void shouldSetAndGetParentId() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setParentId(0L);
        assertEquals(0L, entity.getParentId());
    }

    @Test
    void shouldSetAndGetName() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setName("菜单A");
        assertEquals("菜单A", entity.getName());
    }

    @Test
    void shouldSetAndGetPath() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setPath("/menu");
        assertEquals("/menu", entity.getPath());
    }

    @Test
    void shouldSetAndGetComponent() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setComponent("menu/index");
        assertEquals("menu/index", entity.getComponent());
    }

    @Test
    void shouldSetAndGetApi() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setApi("/api/menu");
        assertEquals("/api/menu", entity.getApi());
    }

    @Test
    void shouldSetAndGetRedirect() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setRedirect("/redirect");
        assertEquals("/redirect", entity.getRedirect());
    }

    @Test
    void shouldSetAndGetPermission() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setPermission("menu:view");
        assertEquals("menu:view", entity.getPermission());
    }

    @Test
    void shouldSetAndGetSortOrder() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setSortOrder(1);
        assertEquals(1, entity.getSortOrder());
    }

    @Test
    void shouldSetAndGetTitle() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setTitle("菜单标题");
        assertEquals("菜单标题", entity.getTitle());
    }

    @Test
    void shouldSetAndGetAffixTab() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setAffixTab(true);
        assertTrue(entity.getAffixTab());
    }

    @Test
    void shouldSetAndGetNoBasicLayout() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setNoBasicLayout(false);
        assertFalse(entity.getNoBasicLayout());
    }

    @Test
    void shouldSetAndGetIcon() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setIcon("icon-menu");
        assertEquals("icon-menu", entity.getIcon());
    }

    @Test
    void shouldSetAndGetMenuType() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setMenuType(1);
        assertEquals(1, entity.getMenuType());
    }

    @Test
    void shouldSetAndGetVisible() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setVisible(true);
        assertTrue(entity.getVisible());
    }

    @Test
    void shouldSetAndGetEmbedded() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setEmbedded(false);
        assertFalse(entity.getEmbedded());
    }

    @Test
    void shouldSetAndGetMenuStatus() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setMenuStatus(true);
        assertTrue(entity.getMenuStatus());
    }

    @Test
    void shouldSetAndGetDeptId() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setDeptId(100L);
        assertEquals(100L, entity.getDeptId());
    }

    @Test
    void shouldSetAndGetDeleteStatus() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setDeleteStatus(false);
        assertFalse(entity.getDeleteStatus());
    }

    @Test
    void shouldSetAndGetCreateBy() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setCreateBy("admin");
        assertEquals("admin", entity.getCreateBy());
    }

    @Test
    void shouldSetAndGetCreateTime() {
        SysMenuEntity entity = new SysMenuEntity();
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        assertEquals(now, entity.getCreateTime());
    }

    @Test
    void shouldSetAndGetUpdateBy() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setUpdateBy("admin");
        assertEquals("admin", entity.getUpdateBy());
    }

    @Test
    void shouldSetAndGetUpdateTime() {
        SysMenuEntity entity = new SysMenuEntity();
        LocalDateTime now = LocalDateTime.now();
        entity.setUpdateTime(now);
        assertEquals(now, entity.getUpdateTime());
    }

    @Test
    void shouldSetAndGetVersion() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setVersion(0);
        assertEquals(0, entity.getVersion());
    }

    @Test
    void equals_shouldWorkForSameInstance() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        assertEquals(entity, entity);
    }

    @Test
    void equals_shouldWorkForSameValues() {
        SysMenuEntity entity1 = new SysMenuEntity();
        entity1.setId(1L);
        entity1.setName("菜单A");

        SysMenuEntity entity2 = new SysMenuEntity();
        entity2.setId(1L);
        entity2.setName("菜单A");

        assertEquals(entity1, entity2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setName("菜单A");
        int hash1 = entity.hashCode();
        int hash2 = entity.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void toString_shouldContainFields() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setName("菜单A");
        String str = entity.toString();
        assertTrue(str.contains("SysMenuEntity"));
        assertTrue(str.contains("1"));
        assertTrue(str.contains("菜单A"));
    }
}
