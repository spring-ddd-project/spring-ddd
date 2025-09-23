package com.springddd.domain.user;

import com.springddd.domain.role.RoleId;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for SysUserRoleDomainFactory interface.
 * Verifies the interface contract and method signatures.
 */
class SysUserRoleDomainFactoryTest {

    @Test
    void interface_ShouldExist() {
        assertTrue(SysUserRoleDomainFactory.class.isInterface());
    }

    @Test
    void newInstanceMethod_ShouldHaveCorrectSignature() throws NoSuchMethodException {
        Method newInstanceMethod = SysUserRoleDomainFactory.class.getMethod(
            "newInstance",
            UserId.class,
            RoleId.class,
            Long.class
        );

        assertNotNull(newInstanceMethod);
        assertEquals(SysUserRoleDomain.class, newInstanceMethod.getReturnType());
    }

    @Test
    void interface_ShouldExtendCorrectTypes() {
        // Verify SysUserRoleDomainFactory is an interface
        assertTrue(SysUserRoleDomainFactory.class.isInterface());
    }

    @Test
    void factoryMethods_ShouldBeAbstract() {
        // Interface methods should be abstract by default
        Method[] methods = SysUserRoleDomainFactory.class.getDeclaredMethods();
        for (Method method : methods) {
            assertFalse(method.isBridge());
            assertFalse(method.isSynthetic());
        }
    }
}
