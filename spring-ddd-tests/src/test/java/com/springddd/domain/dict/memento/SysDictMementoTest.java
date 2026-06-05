package com.springddd.domain.dict.memento;

import com.springddd.domain.dict.DictBasicInfo;
import com.springddd.domain.dict.DictExtendInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysDictMementoTest {

    @Test
    void shouldCreateSysDictMemento() {
        DictBasicInfo basicInfo = new DictBasicInfo("name", "code");
        DictExtendInfo extendInfo = new DictExtendInfo(1, true);

        SysDictMemento memento = new SysDictMemento(basicInfo, extendInfo);

        assertEquals(basicInfo, memento.getDictBasicInfo());
        assertEquals(extendInfo, memento.getDictExtendInfo());
    }

    @Test
    void shouldCreateSysDictMementoWithNullValues() {
        SysDictMemento memento = new SysDictMemento(null, null);

        assertNull(memento.getDictBasicInfo());
        assertNull(memento.getDictExtendInfo());
    }
}
