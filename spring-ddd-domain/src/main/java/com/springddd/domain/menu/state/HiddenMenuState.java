package com.springddd.domain.menu.state;

import com.springddd.domain.menu.SysMenuDomain;

public class HiddenMenuState implements MenuState {
    @Override
    public void show(SysMenuDomain domain) {
        com.springddd.domain.menu.AdvancedOptions old = domain.getAdvancedOptions();
        if (old != null) {
            domain.setAdvancedOptions(new com.springddd.domain.menu.AdvancedOptions(old.order(), old.icon(), old.menuType(), true, old.menuStatus()));
        }
        domain.setState(new VisibleMenuState());
    }

    @Override
    public void hide(SysMenuDomain domain) {
        // Already hidden
    }
}


























