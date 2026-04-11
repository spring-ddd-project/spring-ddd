package com.springddd.application.service.user;

import com.springddd.domain.dept.exception.DeptIdNullException;
import com.springddd.domain.user.Account;
import com.springddd.domain.user.ExtendInfo;
import com.springddd.domain.user.Password;
import com.springddd.domain.user.SysUserDomain;
import com.springddd.domain.user.Username;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysUserDomainFactoryImplTest {

    @Test
    void shouldCreateNewInstance() {
        SysUserDomainFactoryImpl factory = new SysUserDomainFactoryImpl();

        Account account = new Account();
        account.setUsername(new Username("testuser"));
        account.setPassword(new Password("password123"));
        account.setEmail("test@example.com");
        account.setLockStatus(false);

        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar("avatar_url");
        extendInfo.setSex(true);

        SysUserDomain domain = factory.newInstance(account, extendInfo, 1L);

        assertNotNull(domain);
        assertEquals(account, domain.getAccount());
        assertEquals(extendInfo, domain.getExtendInfo());
        assertEquals(1L, domain.getDeptId());
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void shouldThrowWhenDeptIdIsNull() {
        SysUserDomainFactoryImpl factory = new SysUserDomainFactoryImpl();

        Account account = new Account();
        account.setUsername(new Username("testuser"));
        account.setPassword(new Password("password123"));

        ExtendInfo extendInfo = new ExtendInfo();

        assertThrows(DeptIdNullException.class, () ->
            factory.newInstance(account, extendInfo, null));
    }
}
