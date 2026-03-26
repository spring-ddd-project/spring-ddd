package com.springddd.domain.menu;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysMenuDomain extends AbstractDomainMask implements Cloneable {

    private MenuId menuId;

    private MenuId parentId;

    private String name;

    private Catalog catalog;

    private Menu menu;

    private Button button;

    private AdvancedOptions advancedOptions;

    private MenuExtendInfo menuExtendInfo;

    @Override
    public SysMenuDomain clone() {
        try {
            SysMenuDomain clone = (SysMenuDomain) super.clone();
            if (this.menuId != null) clone.setMenuId(new MenuId(this.menuId.value()));
            if (this.parentId != null) clone.setParentId(new MenuId(this.parentId.value()));
            // Catalog, Menu, Button, AdvancedOptions are records or simple objects, shallow clone might be enough if immutable
            // But let's be safe
            if (this.menu != null) clone.setMenu(new Menu(this.menu.menuPath(), this.menu.component(), this.menu.affixTab(), this.menu.noBasicLayout(), this.menu.embedded()));
            if (this.advancedOptions != null) clone.setAdvancedOptions(new AdvancedOptions(this.advancedOptions.order(), this.advancedOptions.icon(), this.advancedOptions.menuType(), this.advancedOptions.visible(), this.advancedOptions.menuStatus()));
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    private com.springddd.domain.menu.state.MenuState state;

    public void setState(com.springddd.domain.menu.state.MenuState state) {
        this.state = state;
    }

    public void show() {
        if (state == null) state = advancedOptions != null && advancedOptions.visible() ? new com.springddd.domain.menu.state.VisibleMenuState() : new com.springddd.domain.menu.state.HiddenMenuState();
        state.show(this);
    }

    public void hide() {
        if (state == null) state = advancedOptions != null && advancedOptions.visible() ? new com.springddd.domain.menu.state.VisibleMenuState() : new com.springddd.domain.menu.state.HiddenMenuState();
        state.hide(this);
    }

    public void create() {
    }

    public void update(MenuId parentId,
                       String name,
                       Catalog catalog,
                       Menu menu,
                       Button button,
                       MenuExtendInfo menuExtendInfo, Long deptId) {
        this.parentId = parentId;
        this.name = name;
        this.catalog = catalog;
        this.menu = menu;
        this.button = button;
        this.menuExtendInfo = menuExtendInfo;
        super.setDeptId(deptId);
    }

    public void delete() {
        super.setDeleteStatus(true);
    }

    public void restore() {
        super.setDeleteStatus(false);
    }

    public com.springddd.domain.menu.memento.SysMenuMemento saveToMemento() {
        return new com.springddd.domain.menu.memento.SysMenuMemento(this.parentId, this.advancedOptions);
    }

    public void restoreFromMemento(com.springddd.domain.menu.memento.SysMenuMemento memento) {
        this.parentId = memento.getParentId();
        this.advancedOptions = memento.getAdvancedOptions();
    }
}











































