package com.springddd.domain.gen.memento;

import com.springddd.domain.gen.GenProjectInfoExtendInfo;
import com.springddd.domain.gen.ProjectInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenProjectInfoMementoTest {

    @Test
    void shouldCreateGenProjectInfoMemento() {
        ProjectInfo projectInfo = new ProjectInfo("table", "com.example", "ClassName", "module", "project");
        GenProjectInfoExtendInfo extendInfo = new GenProjectInfoExtendInfo("requestName");

        GenProjectInfoMemento memento = new GenProjectInfoMemento(projectInfo, extendInfo);

        assertEquals(projectInfo, memento.getProjectInfo());
        assertEquals(extendInfo, memento.getExtendInfo());
    }

    @Test
    void shouldCreateGenProjectInfoMementoWithNullValues() {
        GenProjectInfoMemento memento = new GenProjectInfoMemento(null, null);

        assertNull(memento.getProjectInfo());
        assertNull(memento.getExtendInfo());
    }
}
