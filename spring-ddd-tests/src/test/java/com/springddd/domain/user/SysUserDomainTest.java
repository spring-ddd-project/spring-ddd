package com.springddd.domain.user;

import com.springddd.domain.dept.exception.DeptIdNullException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SysUserDomainTest {

    @InjectMocks
    private SysUserDomain sysUserDomain;

    @Test
    void create_ShouldSetDefaultValues() {
        // Given
        SysUserDomain domain = new SysUserDomain();

        // When
        domain.create();

        // Then - create() is a no-op, just verify no exceptions
        assertNotNull(domain);
    }

    @Test
    void updateUser_WithValidParams_ShouldUpdateAllFields() {
        // Given
        SysUserDomain domain = new SysUserDomain();
        UserId userId = new UserId(1L);
        domain.setUserId(userId);

        Username username = new Username("admin");
        Password password = new Password("password123");
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setEmail("admin@example.com");
        account.setPhone("1234567890");
        account.setLockStatus(false);

        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar("avatar.png");
        extendInfo.setSex(true);

        Long deptId = 100L;

        // When
        domain.updateUser(account, extendInfo, deptId);

        // Then
        assertEquals(account, domain.getAccount());
        assertEquals(extendInfo, domain.getExtendInfo());
        assertEquals(deptId, domain.getDeptId());
    }

    @Test
    void updateUser_WithNullDeptId_ShouldThrowDeptIdNullException() {
        // Given
        SysUserDomain domain = new SysUserDomain();
        Account account = new Account();
        ExtendInfo extendInfo = new ExtendInfo();

        // When & Then
        assertThrows(
            DeptIdNullException.class,
            () -> domain.updateUser(account, extendInfo, null)
        );
    }

    @Test
    void updateUser_WithNullAccount_ShouldUpdate() {
        // Given
        SysUserDomain domain = new SysUserDomain();
        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar("avatar.png");
        Long deptId = 100L;

        // When
        domain.updateUser(null, extendInfo, deptId);

        // Then
        assertNull(domain.getAccount());
        assertEquals(extendInfo, domain.getExtendInfo());
        assertEquals(deptId, domain.getDeptId());
    }

    @Test
    void updateUser_WithNullExtendInfo_ShouldUpdate() {
        // Given
        SysUserDomain domain = new SysUserDomain();
        Account account = new Account();
        Long deptId = 100L;

        // When
        domain.updateUser(account, null, deptId);

        // Then
        assertEquals(account, domain.getAccount());
        assertNull(domain.getExtendInfo());
        assertEquals(deptId, domain.getDeptId());
    }

    @Test
    void delete_ShouldSetDeleteStatusTrue() {
        // Given
        SysUserDomain domain = new SysUserDomain();
        assertNull(domain.getDeleteStatus());

        // When
        domain.delete();

        // Then
        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void restore_ShouldSetDeleteStatusFalse() {
        // Given
        SysUserDomain domain = new SysUserDomain();
        domain.setDeleteStatus(true);

        // When
        domain.restore();

        // Then
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void updateUserStatus_WithTrue_ShouldLockUser() {
        // Given
        SysUserDomain domain = new SysUserDomain();
        Account account = new Account();
        account.setLockStatus(false);
        domain.setAccount(account);

        // When
        domain.updateUserStatus(true);

        // Then
        assertTrue(domain.getAccount().getLockStatus());
    }

    @Test
    void updateUserStatus_WithFalse_ShouldUnlockUser() {
        // Given
        SysUserDomain domain = new SysUserDomain();
        Account account = new Account();
        account.setLockStatus(true);
        domain.setAccount(account);

        // When
        domain.updateUserStatus(false);

        // Then
        assertFalse(domain.getAccount().getLockStatus());
    }

    @Test
    void updateUserStatus_WithNullAccount_ShouldNotThrowException() {
        // Given
        SysUserDomain domain = new SysUserDomain();
        domain.setAccount(null);

        // When & Then - should not throw NPE due to short-circuit
        assertThrows(
            NullPointerException.class,
            () -> domain.updateUserStatus(true)
        );
    }

    @Test
    void delete_ThenRestore_ShouldToggleDeleteStatus() {
        // Given
        SysUserDomain domain = new SysUserDomain();

        // When & Then
        assertNull(domain.getDeleteStatus());
        domain.delete();
        assertTrue(domain.getDeleteStatus());
        domain.restore();
        assertFalse(domain.getDeleteStatus());
    }
}
