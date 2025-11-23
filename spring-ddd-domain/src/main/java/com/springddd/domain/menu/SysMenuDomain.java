package com.springddd.domain.menu;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysMenuDomain extends AbstractDomainMask {

    private MenuId menuId;

    private MenuId parentId;

    private String name;

    private Catalog catalog;

    private Menu menu;

    private Button button;

    private AdvancedOptions advancedOptions;

    private MenuExtendInfo menuExtendInfo;

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
}
