package com.springddd.domain.dept;

import com.springddd.domain.dept.exception.DeptNameNullException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeptBasicInfoTest {

    @Test
    void shouldCreateDeptBasicInfoWithValidName() {
        String deptName = "Engineering";
        DeptBasicInfo info = new DeptBasicInfo(deptName);
        assertEquals(deptName, info.deptName());
    }

    @Test
    void shouldThrowDeptNameNullExceptionWhenNameIsNull() {
        assertThrows(DeptNameNullException.class, () -> new DeptBasicInfo(null));
    }

    @Test
    void shouldThrowDeptNameNullExceptionWhenNameIsEmpty() {
        assertThrows(DeptNameNullException.class, () -> new DeptBasicInfo(""));
    }

    @Test
    void shouldNotThrowExceptionForBlankString() {
        // ObjectUtils.isEmpty only checks null or empty string, not whitespace
        DeptBasicInfo info = new DeptBasicInfo("   ");
        assertEquals("   ", info.deptName());
    }
}
