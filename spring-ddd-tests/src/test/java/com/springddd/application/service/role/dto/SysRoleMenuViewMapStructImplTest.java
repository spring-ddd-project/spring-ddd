package com.springddd.application.service.role.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysRoleMenuViewMapStructImplTest {

    @Test
    void shouldHaveSysRoleMenuViewMapStructImplClass() {
        assertNotNull(SysRoleMenuViewMapStructImpl.class);
    }

    @Test
    void shouldImplementSysRoleMenuViewMapStruct() {
        assertNotNull(SysRoleMenuViewMapStructImpl.class.getInterfaces());
    }

    @Test
    void shouldHaveToViewMethod() {
        assertNotNull(SysRoleMenuViewMapStructImpl.class.getDeclaredMethods());
    }
}
