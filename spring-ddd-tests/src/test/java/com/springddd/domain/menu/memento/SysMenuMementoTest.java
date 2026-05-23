package com.springddd.domain.menu.memento;

import com.springddd.domain.menu.AdvancedOptions;
import com.springddd.domain.menu.MenuId;
import com.springddd.domain.menu.SysMenuDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SysMenuMementoTest {

    @Test
    @DisplayName("应从构造函数正确创建 Memento")
    void constructor_shouldCreateMementoWithCorrectValues() {
        MenuId parentId = new MenuId(1L);
        AdvancedOptions advancedOptions = new AdvancedOptions(1, "icon", 1, true, true);

        SysMenuMemento memento = new SysMenuMemento(parentId, advancedOptions);

        assertThat(memento.getParentId()).isEqualTo(parentId);
        assertThat(memento.getAdvancedOptions()).isEqualTo(advancedOptions);
    }

    @Test
    @DisplayName("应从聚合根正确创建 Memento")
    void saveToMemento_shouldCreateMementoFromDomain() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setParentId(new MenuId(2L));
        domain.setAdvancedOptions(new AdvancedOptions(2, "home", 0, false, true));

        SysMenuMemento memento = domain.saveToMemento();

        assertThat(memento.getParentId().value()).isEqualTo(2L);
        assertThat(memento.getAdvancedOptions().order()).isEqualTo(2);
        assertThat(memento.getAdvancedOptions().icon()).isEqualTo("home");
        assertThat(memento.getAdvancedOptions().visible()).isFalse();
        assertThat(memento.getAdvancedOptions().menuStatus()).isTrue();
    }

    @Test
    @DisplayName("应从 Memento 正确恢复聚合根")
    void restoreFromMemento_shouldRestoreDomainValues() {
        SysMenuDomain domain = new SysMenuDomain();
        SysMenuMemento memento = new SysMenuMemento(
                new MenuId(3L),
                new AdvancedOptions(3, "setting", 1, true, false)
        );

        domain.restoreFromMemento(memento);

        assertThat(domain.getParentId().value()).isEqualTo(3L);
        assertThat(domain.getAdvancedOptions().order()).isEqualTo(3);
        assertThat(domain.getAdvancedOptions().icon()).isEqualTo("setting");
        assertThat(domain.getAdvancedOptions().visible()).isTrue();
        assertThat(domain.getAdvancedOptions().menuStatus()).isFalse();
    }

    @Test
    @DisplayName("Memento 字段值应通过 getter 正确返回")
    void getters_shouldReturnCorrectFieldValues() {
        MenuId parentId = new MenuId(5L);
        AdvancedOptions advancedOptions = new AdvancedOptions(5, "user", 1, true, true);

        SysMenuMemento memento = new SysMenuMemento(parentId, advancedOptions);

        assertThat(memento.getParentId().value()).isEqualTo(5L);
        assertThat(memento.getAdvancedOptions().order()).isEqualTo(5);
        assertThat(memento.getAdvancedOptions().menuType()).isEqualTo(1);
    }
}
