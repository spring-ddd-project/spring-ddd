package com.springddd.application.service.user;

import com.springddd.domain.dept.exception.DeptIdNullException;
import com.springddd.domain.user.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysUserDomainFactoryImplTest {

    private final SysUserDomainFactoryImpl factory = new SysUserDomainFactoryImpl();

    @Test
    @DisplayName("should create SysUserDomain with correct fields set")
    void newInstance() {
        Account account = new Account(new Username("testuser"), new Password("password123"), "test@example.com", "1234567890", false);
        ExtendInfo extendInfo = new ExtendInfo("avatar.png", true);
        Long deptId = 1L;

        SysUserDomain domain = factory.newInstance(account, extendInfo, deptId);

        assertNotNull(domain);
        assertEquals(account, domain.getAccount());
        assertEquals(extendInfo, domain.getExtendInfo());
        assertEquals(deptId, domain.getDeptId());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    @DisplayName("should throw DeptIdNullException when deptId is null")
    void newInstanceWithNullDeptId() {
        Account account = new Account(new Username("testuser"), new Password("password123"), "test@example.com", "1234567890", false);
        ExtendInfo extendInfo = new ExtendInfo("avatar.png", true);

        assertThrows(DeptIdNullException.class, () -> factory.newInstance(account, extendInfo, null));
    }
}
