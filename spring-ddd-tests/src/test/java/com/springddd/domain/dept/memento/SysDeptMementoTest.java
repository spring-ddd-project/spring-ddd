package com.springddd.domain.dept.memento;

import com.springddd.domain.dept.DeptBasicInfo;
import com.springddd.domain.dept.DeptExtendInfo;
import com.springddd.domain.dept.DeptId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysDeptMementoTest {

    @Test
    void shouldCreateSysDeptMemento() {
        DeptId parentId = new DeptId(1L);
        DeptBasicInfo basicInfo = new DeptBasicInfo("tech");
        DeptExtendInfo extendInfo = new DeptExtendInfo(1, true);

        SysDeptMemento memento = new SysDeptMemento(parentId, basicInfo, extendInfo);

        assertEquals(parentId, memento.getParentId());
        assertEquals(basicInfo, memento.getDeptBasicInfo());
        assertEquals(extendInfo, memento.getDeptExtendInfo());
    }

    @Test
    void shouldCreateSysDeptMementoWithNullValues() {
        SysDeptMemento memento = new SysDeptMemento(null, null, null);

        assertNull(memento.getParentId());
        assertNull(memento.getDeptBasicInfo());
        assertNull(memento.getDeptExtendInfo());
    }
}
