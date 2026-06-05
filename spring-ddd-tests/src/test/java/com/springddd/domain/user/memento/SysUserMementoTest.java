package com.springddd.domain.user.memento;

import com.springddd.domain.user.Account;
import com.springddd.domain.user.ExtendInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysUserMementoTest {

    @Test
    void shouldCreateSysUserMemento() {
        Account account = new Account(null, null, "test@example.com", null, false);
        ExtendInfo extendInfo = new ExtendInfo();
        Long deptId = 1L;

        SysUserMemento memento = new SysUserMemento(account, extendInfo, deptId);

        assertEquals(account, memento.getAccount());
        assertEquals(extendInfo, memento.getExtendInfo());
        assertEquals(deptId, memento.getDeptId());
    }

    @Test
    void shouldCreateSysUserMementoWithNullValues() {
        SysUserMemento memento = new SysUserMemento(null, null, null);

        assertNull(memento.getAccount());
        assertNull(memento.getExtendInfo());
        assertNull(memento.getDeptId());
    }
}
