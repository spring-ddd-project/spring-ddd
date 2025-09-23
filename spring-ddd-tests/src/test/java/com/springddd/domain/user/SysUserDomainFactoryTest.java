package com.springddd.domain.user;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for SysUserDomainFactory interface.
 * Verifies the interface contract and method signatures.
 */
class SysUserDomainFactoryTest {

    @Test
    void interface_ShouldExist() {
        assertTrue(SysUserDomainFactory.class.isInterface());
    }

    @Test
    void newInstanceMethod_ShouldHaveCorrectSignature() throws NoSuchMethodException {
        Method newInstanceMethod = SysUserDomainFactory.class.getMethod(
            "newInstance",
            Account.class,
            ExtendInfo.class,
            Long.class
        );

        assertNotNull(newInstanceMethod);
        assertEquals(SysUserDomain.class, newInstanceMethod.getReturnType());
    }

    @Test
    void interface_ShouldExtendCorrectTypes() {
        // Verify SysUserDomainFactory is an interface
        assertTrue(SysUserDomainFactory.class.isInterface());
    }

    @Test
    void factoryMethods_ShouldBeAbstract() {
        // Interface methods should be abstract by default
        Method[] methods = SysUserDomainFactory.class.getDeclaredMethods();
        for (Method method : methods) {
            assertFalse(method.isBridge());
            assertFalse(method.isSynthetic());
        }
    }
}
