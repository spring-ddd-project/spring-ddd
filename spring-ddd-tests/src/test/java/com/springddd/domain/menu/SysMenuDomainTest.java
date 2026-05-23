package com.springddd.domain.menu;

import com.springddd.domain.menu.state.HiddenMenuState;
import com.springddd.domain.menu.state.VisibleMenuState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SysMenuDomainTest {

    private SysMenuDomain createMenuDomain() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setMenuId(new MenuId(1L));
        domain.setParentId(new MenuId(0L));
        domain.setName("系统管理");
        domain.setCatalog(new Catalog("sys", "Layout", "/sys"));
        domain.setMenu(new Menu("/sys", "Layout", false, false, false));
        domain.setButton(new Button("sys:user:create", "新增用户"));
        domain.setAdvancedOptions(new AdvancedOptions(1, "icon", 1, true, true));
        domain.setMenuExtendInfo(new MenuExtendInfo(1, "系统管理菜单", "icon", 1, true, true));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        return domain;
    }

    @Test
    @DisplayName("show 应设置 VisibleMenuState")
    void show_shouldSetVisibleState() {
        SysMenuDomain domain = createMenuDomain();
        domain.show();
        assertThat(domain.getState()).isInstanceOf(VisibleMenuState.class);
    }

    @Test
    @DisplayName("hide 应设置 HiddenMenuState")
    void hide_shouldSetHiddenState() {
        SysMenuDomain domain = createMenuDomain();
        domain.hide();
        assertThat(domain.getState()).isInstanceOf(HiddenMenuState.class);
    }

    @Test
    @DisplayName("update 应更新菜单信息")
    void update_shouldUpdateInfo() {
        SysMenuDomain domain = createMenuDomain();
        domain.update(new MenuId(2L), "用户管理", domain.getCatalog(), domain.getMenu(), domain.getButton(), domain.getMenuExtendInfo(), 2L);

        assertThat(domain.getName()).isEqualTo("用户管理");
        assertThat(domain.getParentId().value()).isEqualTo(2L);
        assertThat(domain.getDeptId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("delete 应设置 deleteStatus")
    void delete_shouldSetDeleteStatus() {
        SysMenuDomain domain = createMenuDomain();
        domain.delete();
        assertThat(domain.getDeleteStatus()).isTrue();
    }

    @Test
    @DisplayName("restore 应清除 deleteStatus")
    void restore_shouldClearDeleteStatus() {
        SysMenuDomain domain = createMenuDomain();
        domain.delete();
        domain.restore();
        assertThat(domain.getDeleteStatus()).isFalse();
    }

    @Test
    @DisplayName("clone 应创建深拷贝")
    void clone_shouldCreateDeepCopy() {
        SysMenuDomain original = createMenuDomain();
        SysMenuDomain cloned = original.clone();

        assertThat(cloned).isNotSameAs(original);
        assertThat(cloned.getMenuId().value()).isEqualTo(1L);
    }

    @Test
    @DisplayName("clone 当字段为 null 时应处理")
    void clone_withNullFields_shouldHandle() {
        SysMenuDomain original = new SysMenuDomain();
        original.setMenuId(new MenuId(1L));
        // menuId and parentId set, but menu and advancedOptions are null
        SysMenuDomain cloned = original.clone();

        assertThat(cloned).isNotNull();
        assertThat(cloned.getMenuId().value()).isEqualTo(1L);
        assertThat(cloned.getMenu()).isNull();
        assertThat(cloned.getAdvancedOptions()).isNull();
    }

    @Test
    @DisplayName("clone 当 menuId 为 null 时应处理")
    void clone_whenMenuIdIsNull_shouldHandleNull() {
        SysMenuDomain original = new SysMenuDomain();
        original.setMenuId(null);
        original.setParentId(new MenuId(1L));
        original.setName("test");

        SysMenuDomain cloned = original.clone();
        assertThat(cloned).isNotSameAs(original);
        assertThat(cloned.getMenuId()).isNull();
        assertThat(cloned.getParentId().value()).isEqualTo(1L);
        assertThat(cloned.getName()).isEqualTo("test");
    }

    @Test
    @DisplayName("show 当 state 为 null 且 advancedOptions 为 null 时应创建默认状态")
    void show_whenStateNullAndAdvancedOptionsNull_shouldCreateDefaultState() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.show();
        assertThat(domain.getState()).isNotNull();
    }

    @Test
    @DisplayName("show 当 state 已设置时不应重新初始化 state")
    void show_whenStateAlreadySet_shouldKeepState() {
        SysMenuDomain domain = createMenuDomain();
        domain.show();
        domain.show();
        assertThat(domain.getState()).isInstanceOf(VisibleMenuState.class);
    }

    @Test
    @DisplayName("hide 当 state 为 null 且 advancedOptions 不可见时应创建隐藏状态")
    void hide_whenStateNullAndNotVisible_shouldCreateHiddenState() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setAdvancedOptions(new AdvancedOptions(1, "icon", 1, false, true));
        domain.hide();
        assertThat(domain.getState()).isInstanceOf(HiddenMenuState.class);
    }

    @Test
    @DisplayName("hide 当 state 已设置时不应重新初始化 state")
    void hide_whenStateAlreadySet_shouldKeepState() {
        SysMenuDomain domain = createMenuDomain();
        domain.hide();
        domain.hide();
        assertThat(domain.getState()).isInstanceOf(HiddenMenuState.class);
    }

    @Test
    @DisplayName("hide 当 advancedOptions 为 null 时应保持 HiddenMenuState")
    void hide_whenAdvancedOptionsIsNull_shouldSetHiddenState() {
        SysMenuDomain domain = createMenuDomain();
        domain.setAdvancedOptions(null);
        domain.hide();
        assertThat(domain.getState()).isInstanceOf(HiddenMenuState.class);
    }

    @Test
    @DisplayName("saveToMemento 应保存状态")
    void saveToMemento_shouldSaveState() {
        SysMenuDomain domain = createMenuDomain();
        var memento = domain.saveToMemento();

        assertThat(memento.getParentId().value()).isEqualTo(0L);
    }

    @Test
    @DisplayName("restoreFromMemento 应恢复状态")
    void restoreFromMemento_shouldRestoreState() {
        SysMenuDomain domain = createMenuDomain();
        var memento = domain.saveToMemento();

        domain.setParentId(new MenuId(99L));
        domain.restoreFromMemento(memento);

        assertThat(domain.getParentId().value()).isEqualTo(0L);
    }
}
