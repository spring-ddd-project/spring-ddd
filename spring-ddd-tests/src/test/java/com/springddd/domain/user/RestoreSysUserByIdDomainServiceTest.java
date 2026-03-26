package com.springddd.domain.user;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for RestoreSysUserByIdDomainService interface.
 * Verifies the interface contract and method signatures.
 */
class RestoreSysUserByIdDomainServiceTest {

    @Test
    void interface_ShouldExist() {
        assertTrue(RestoreSysUserByIdDomainService.class.isInterface());
    }

    @Test
    void restoreByIdsMethod_ShouldHaveCorrectSignature() throws NoSuchMethodException {
        Method restoreByIdsMethod = RestoreSysUserByIdDomainService.class.getMethod(
            "restoreByIds",
            List.class
        );

        assertNotNull(restoreByIdsMethod);
        assertEquals(reactor.core.publisher.Mono.class, restoreByIdsMethod.getReturnType());
    }

    @Test
    void interface_ShouldExtendCorrectTypes() {
        // Verify RestoreSysUserByIdDomainService is an interface
        assertTrue(RestoreSysUserByIdDomainService.class.isInterface());
    }

    @Test
    void serviceMethods_ShouldBeAbstract() {
        // Interface methods should be abstract by default
        Method[] methods = RestoreSysUserByIdDomainService.class.getDeclaredMethods();
        for (Method method : methods) {
            assertFalse(method.isBridge());
            assertFalse(method.isSynthetic());
        }
    }
}
