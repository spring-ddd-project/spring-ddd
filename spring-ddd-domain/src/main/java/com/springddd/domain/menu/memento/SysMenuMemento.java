package com.springddd.domain.menu.memento;

import com.springddd.domain.menu.MenuId;
import com.springddd.domain.menu.AdvancedOptions;
import lombok.Getter;

@Getter
public class SysMenuMemento {
    private final MenuId parentId;
    private final AdvancedOptions advancedOptions;

    public SysMenuMemento(MenuId parentId, AdvancedOptions advancedOptions) {
        this.parentId = parentId;
        this.advancedOptions = advancedOptions;
    }
}




















