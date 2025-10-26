package com.springddd.domain.menu.state;

import com.springddd.domain.menu.SysMenuDomain;

public class VisibleMenuState implements MenuState {
    @Override
    public void show(SysMenuDomain domain) {
        // Already visible
    }

    @Override
    public void hide(SysMenuDomain domain) {
        com.springddd.domain.menu.AdvancedOptions old = domain.getAdvancedOptions();
        if (old != null) {
            domain.setAdvancedOptions(new com.springddd.domain.menu.AdvancedOptions(old.order(), old.icon(), old.menuType(), false, old.menuStatus()));
        }
        domain.setState(new HiddenMenuState());
    }
}









