package com.springddd.application.service.user;

import com.springddd.domain.dept.exception.DeptIdNullException;
import com.springddd.domain.user.Account;
import com.springddd.domain.user.ExtendInfo;
import com.springddd.domain.user.SysUserDomain;
import com.springddd.domain.user.Username;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysUserDomainFactoryTest {

    @Test
    void newInstance_shouldCreateDomainWithCorrectValues() {
        Account account = new Account();
        account.setUsername(new Username("testuser"));
        account.setPassword(new com.springddd.domain.user.Password("password"));
        account.setPhone("1234567890");
        account.setEmail("test@example.com");
        account.setLockStatus(false);

        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar("avatar.png");
        extendInfo.setSex(true);

        Long deptId = 1L;

        SysUserDomainFactoryImpl factory = new SysUserDomainFactoryImpl();
        SysUserDomain result = factory.newInstance(account, extendInfo, deptId);

        assertNotNull(result);
        assertEquals(account, result.getAccount());
        assertEquals(extendInfo, result.getExtendInfo());
        assertEquals(deptId, result.getDeptId());
        assertFalse(result.getDeleteStatus());
    }

    @Test
    void newInstance_shouldSetDeleteStatusToFalse() {
        Account account = new Account();
        account.setUsername(new Username("testuser"));

        ExtendInfo extendInfo = new ExtendInfo();

        SysUserDomainFactoryImpl factory = new SysUserDomainFactoryImpl();
        SysUserDomain result = factory.newInstance(account, extendInfo, 1L);

        assertFalse(result.getDeleteStatus());
    }

    @Test
    void newInstance_shouldThrowException_whenDeptIdIsNull() {
        Account account = new Account();
        account.setUsername(new Username("testuser"));

        ExtendInfo extendInfo = new ExtendInfo();

        SysUserDomainFactoryImpl factory = new SysUserDomainFactoryImpl();

        assertThrows(DeptIdNullException.class, () -> factory.newInstance(account, extendInfo, null));
    }

    @Test
    void newInstance_shouldWork_whenDeptIdIsZero() {
        Account account = new Account();
        account.setUsername(new Username("testuser"));

        ExtendInfo extendInfo = new ExtendInfo();

        SysUserDomainFactoryImpl factory = new SysUserDomainFactoryImpl();
        SysUserDomain result = factory.newInstance(account, extendInfo, 0L);

        assertNotNull(result);
        assertEquals(0L, result.getDeptId());
    }
}
