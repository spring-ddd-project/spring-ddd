package com.springddd.domain.menu.memento;

import com.springddd.domain.menu.AdvancedOptions;
import com.springddd.domain.menu.MenuId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysMenuMementoTest {

    @Test
    void shouldCreateSysMenuMemento() {
        MenuId parentId = new MenuId(1L);
        AdvancedOptions advancedOptions = new AdvancedOptions(1, "icon", 1, true, true);

        SysMenuMemento memento = new SysMenuMemento(parentId, advancedOptions);

        assertEquals(parentId, memento.getParentId());
        assertEquals(advancedOptions, memento.getAdvancedOptions());
    }

    @Test
    void shouldCreateSysMenuMementoWithNullValues() {
        SysMenuMemento memento = new SysMenuMemento(null, null);

        assertNull(memento.getParentId());
        assertNull(memento.getAdvancedOptions());
    }
}
