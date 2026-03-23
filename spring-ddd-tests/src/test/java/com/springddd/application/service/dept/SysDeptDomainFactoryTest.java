package com.springddd.application.service.dept;

import com.springddd.domain.dept.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SysDeptDomainFactoryTest {

    private final SysDeptDomainFactoryImpl factory = new SysDeptDomainFactoryImpl();

    @Test
    void newInstance_shouldCreateDomainWithCorrectValues() {
        DeptId parentId = new DeptId(0L);
        DeptBasicInfo basicInfo = new DeptBasicInfo("Test Department");
        DeptExtendInfo extendInfo = new DeptExtendInfo(1, true);

        SysDeptDomain domain = factory.newInstance(parentId, basicInfo, extendInfo);

        assertNotNull(domain);
        assertEquals(parentId, domain.getParentId());
        assertEquals(basicInfo, domain.getDeptBasicInfo());
        assertEquals(extendInfo, domain.getDeptExtendInfo());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void newInstance_shouldSetDeleteStatusToFalse() {
        DeptId parentId = new DeptId(1L);
        DeptBasicInfo basicInfo = new DeptBasicInfo("Test");
        DeptExtendInfo extendInfo = new DeptExtendInfo(1, true);

        SysDeptDomain domain = factory.newInstance(parentId, basicInfo, extendInfo);

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void newInstance_shouldHandleNullParentId() {
        DeptBasicInfo basicInfo = new DeptBasicInfo("Test Department");
        DeptExtendInfo extendInfo = new DeptExtendInfo(1, true);

        SysDeptDomain domain = factory.newInstance(null, basicInfo, extendInfo);

        assertNotNull(domain);
        assertNull(domain.getParentId());
        assertEquals(basicInfo, domain.getDeptBasicInfo());
        assertEquals(extendInfo, domain.getDeptExtendInfo());
    }
}
