package com.springddd.domain.menu;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysMenuDomain extends AbstractDomainMask {

    private MenuId menuId;

    private MenuId parentId;

    private Catalog catalog;

    private Menu menu;

    private Button button;

    private AdvancedOptions advancedOptions;

    private MenuExtendInfo menuExtendInfo;

    public void create() {}

    public void update(MenuId parentId,
                       Catalog catalog,
                       Menu menu,
                       Button button,
                       MenuExtendInfo menuExtendInfo, Long deptId) {
        this.parentId = parentId;
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
