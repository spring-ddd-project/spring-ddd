package com.springddd.domain.dict.memento;

import com.springddd.domain.dict.DictBasicInfo;
import com.springddd.domain.dict.DictExtendInfo;
import com.springddd.domain.dict.SysDictDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SysDictMementoTest {

    @Test
    @DisplayName("应从构造函数正确创建 Memento")
    void constructor_shouldCreateMementoWithCorrectValues() {
        DictBasicInfo basicInfo = new DictBasicInfo("Test Dict", "test_code");
        DictExtendInfo extendInfo = new DictExtendInfo(1, true);

        SysDictMemento memento = new SysDictMemento(basicInfo, extendInfo);

        assertThat(memento.getDictBasicInfo()).isEqualTo(basicInfo);
        assertThat(memento.getDictExtendInfo()).isEqualTo(extendInfo);
    }

    @Test
    @DisplayName("应从聚合根正确创建 Memento")
    void saveToMemento_shouldCreateMementoFromDomain() {
        SysDictDomain domain = new SysDictDomain();
        domain.setDictBasicInfo(new DictBasicInfo("Status Dict", "status"));
        domain.setDictExtendInfo(new DictExtendInfo(2, false));

        SysDictMemento memento = domain.saveToMemento();

        assertThat(memento.getDictBasicInfo().dictName()).isEqualTo("Status Dict");
        assertThat(memento.getDictBasicInfo().dictCode()).isEqualTo("status");
        assertThat(memento.getDictExtendInfo().sortOrder()).isEqualTo(2);
        assertThat(memento.getDictExtendInfo().dictStatus()).isFalse();
    }

    @Test
    @DisplayName("应从 Memento 正确恢复聚合根")
    void restoreFromMemento_shouldRestoreDomainValues() {
        SysDictDomain domain = new SysDictDomain();
        SysDictMemento memento = new SysDictMemento(
                new DictBasicInfo("Type Dict", "type"),
                new DictExtendInfo(3, true)
        );

        domain.restoreFromMemento(memento);

        assertThat(domain.getDictBasicInfo().dictName()).isEqualTo("Type Dict");
        assertThat(domain.getDictBasicInfo().dictCode()).isEqualTo("type");
        assertThat(domain.getDictExtendInfo().sortOrder()).isEqualTo(3);
        assertThat(domain.getDictExtendInfo().dictStatus()).isTrue();
    }

    @Test
    @DisplayName("Memento 字段值应通过 getter 正确返回")
    void getters_shouldReturnCorrectFieldValues() {
        DictBasicInfo basicInfo = new DictBasicInfo("Gender Dict", "gender");
        DictExtendInfo extendInfo = new DictExtendInfo(5, true);

        SysDictMemento memento = new SysDictMemento(basicInfo, extendInfo);

        assertThat(memento.getDictBasicInfo().dictName()).isEqualTo("Gender Dict");
        assertThat(memento.getDictBasicInfo().dictCode()).isEqualTo("gender");
        assertThat(memento.getDictExtendInfo().sortOrder()).isEqualTo(5);
        assertThat(memento.getDictExtendInfo().dictStatus()).isTrue();
    }
}
