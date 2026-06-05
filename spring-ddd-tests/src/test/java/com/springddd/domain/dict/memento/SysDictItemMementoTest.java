package com.springddd.domain.dict.memento;

import com.springddd.domain.dict.DictItemBasicInfo;
import com.springddd.domain.dict.DictItemExtendInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysDictItemMementoTest {

    @Test
    void shouldCreateSysDictItemMemento() {
        DictItemBasicInfo basicInfo = new DictItemBasicInfo("label", 1);
        DictItemExtendInfo extendInfo = new DictItemExtendInfo(1, true);

        SysDictItemMemento memento = new SysDictItemMemento(basicInfo, extendInfo);

        assertEquals(basicInfo, memento.getItemBasicInfo());
        assertEquals(extendInfo, memento.getItemExtendInfo());
    }

    @Test
    void shouldCreateSysDictItemMementoWithNullValues() {
        SysDictItemMemento memento = new SysDictItemMemento(null, null);

        assertNull(memento.getItemBasicInfo());
        assertNull(memento.getItemExtendInfo());
    }
}
