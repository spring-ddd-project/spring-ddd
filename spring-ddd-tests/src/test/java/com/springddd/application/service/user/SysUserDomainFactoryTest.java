package com.springddd.application.service.user;

import com.springddd.domain.dept.exception.DeptIdNullException;
import com.springddd.domain.user.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SysUserDomainFactoryTest {

    private final SysUserDomainFactoryImpl factory = new SysUserDomainFactoryImpl();

    @Test
    void newInstance_shouldCreateDomainWithCorrectValues() {
        Account account = new Account();
        account.setUsername(new Username("testuser"));
        account.setPassword(new Password("password123"));
        account.setEmail("test@example.com");
        account.setPhone("13800138000");
        account.setLockStatus(false);

        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar("avatar.png");
        extendInfo.setSex(true);

        Long deptId = 1L;

        SysUserDomain domain = factory.newInstance(account, extendInfo, deptId);

        assertNotNull(domain);
        assertEquals(account, domain.getAccount());
        assertEquals(extendInfo, domain.getExtendInfo());
        assertEquals(deptId, domain.getDeptId());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void newInstance_shouldSetDeleteStatusToFalse() {
        Account account = new Account();
        ExtendInfo extendInfo = new ExtendInfo();
        Long deptId = 1L;

        SysUserDomain domain = factory.newInstance(account, extendInfo, deptId);

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void newInstance_shouldThrowDeptIdNullExceptionWhenDeptIdIsNull() {
        Account account = new Account();
        ExtendInfo extendInfo = new ExtendInfo();

        assertThrows(DeptIdNullException.class, () -> factory.newInstance(account, extendInfo, null));
    }

    @Test
    void newInstance_shouldHandleNullAccount() {
        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar("avatar.png");
        Long deptId = 1L;

        SysUserDomain domain = factory.newInstance(null, extendInfo, deptId);

        assertNotNull(domain);
        assertNull(domain.getAccount());
        assertEquals(extendInfo, domain.getExtendInfo());
        assertEquals(deptId, domain.getDeptId());
    }

    @Test
    void newInstance_shouldHandleNullExtendInfo() {
        Account account = new Account();
        account.setUsername(new Username("testuser"));
        Long deptId = 1L;

        SysUserDomain domain = factory.newInstance(account, null, deptId);

        assertNotNull(domain);
        assertEquals(account, domain.getAccount());
        assertNull(domain.getExtendInfo());
        assertEquals(deptId, domain.getDeptId());
    }
}
