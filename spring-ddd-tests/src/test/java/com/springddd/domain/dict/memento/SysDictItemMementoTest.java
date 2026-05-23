package com.springddd.domain.dict.memento;

import com.springddd.domain.dict.DictItemBasicInfo;
import com.springddd.domain.dict.DictItemExtendInfo;
import com.springddd.domain.dict.SysDictItemDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SysDictItemMementoTest {

    @Test
    @DisplayName("应从构造函数正确创建 Memento")
    void constructor_shouldCreateMementoWithCorrectValues() {
        DictItemBasicInfo basicInfo = new DictItemBasicInfo("Label", 1, true);
        DictItemExtendInfo extendInfo = new DictItemExtendInfo(1, true);

        SysDictItemMemento memento = new SysDictItemMemento(basicInfo, extendInfo);

        assertThat(memento.getItemBasicInfo()).isEqualTo(basicInfo);
        assertThat(memento.getItemExtendInfo()).isEqualTo(extendInfo);
    }

    @Test
    @DisplayName("应从聚合根正确创建 Memento")
    void saveToMemento_shouldCreateMementoFromDomain() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemBasicInfo(new DictItemBasicInfo("Enabled", 1, true));
        domain.setItemExtendInfo(new DictItemExtendInfo(2, false));

        SysDictItemMemento memento = domain.saveToMemento();

        assertThat(memento.getItemBasicInfo().itemLabel()).isEqualTo("Enabled");
        assertThat(memento.getItemBasicInfo().itemValue()).isEqualTo(1);
        assertThat(memento.getItemExtendInfo().sortOrder()).isEqualTo(2);
        assertThat(memento.getItemExtendInfo().itemStatus()).isFalse();
    }

    @Test
    @DisplayName("应从 Memento 正确恢复聚合根")
    void restoreFromMemento_shouldRestoreDomainValues() {
        SysDictItemDomain domain = new SysDictItemDomain();
        SysDictItemMemento memento = new SysDictItemMemento(
                new DictItemBasicInfo("Disabled", 0, false),
                new DictItemExtendInfo(3, true)
        );

        domain.restoreFromMemento(memento);

        assertThat(domain.getItemBasicInfo().itemLabel()).isEqualTo("Disabled");
        assertThat(domain.getItemBasicInfo().itemValue()).isEqualTo(0);
        assertThat(domain.getItemExtendInfo().sortOrder()).isEqualTo(3);
        assertThat(domain.getItemExtendInfo().itemStatus()).isTrue();
    }

    @Test
    @DisplayName("Memento 字段值应通过 getter 正确返回")
    void getters_shouldReturnCorrectFieldValues() {
        DictItemBasicInfo basicInfo = new DictItemBasicInfo("Pending", 2, true);
        DictItemExtendInfo extendInfo = new DictItemExtendInfo(5, true);

        SysDictItemMemento memento = new SysDictItemMemento(basicInfo, extendInfo);

        assertThat(memento.getItemBasicInfo().itemLabel()).isEqualTo("Pending");
        assertThat(memento.getItemBasicInfo().itemValue()).isEqualTo(2);
        assertThat(memento.getItemExtendInfo().sortOrder()).isEqualTo(5);
        assertThat(memento.getItemExtendInfo().itemStatus()).isTrue();
    }
}
