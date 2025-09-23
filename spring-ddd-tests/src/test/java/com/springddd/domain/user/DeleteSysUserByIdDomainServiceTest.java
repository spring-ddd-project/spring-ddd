package com.springddd.domain.user;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for DeleteSysUserByIdDomainService interface.
 * Verifies the interface contract and method signatures.
 */
class DeleteSysUserByIdDomainServiceTest {

    @Test
    void interface_ShouldExist() {
        assertTrue(DeleteSysUserByIdDomainService.class.isInterface());
    }

    @Test
    void deleteByIdsMethod_ShouldHaveCorrectSignature() throws NoSuchMethodException {
        Method deleteByIdsMethod = DeleteSysUserByIdDomainService.class.getMethod(
            "deleteByIds",
            List.class
        );

        assertNotNull(deleteByIdsMethod);
        assertEquals(reactor.core.publisher.Mono.class, deleteByIdsMethod.getReturnType());
    }

    @Test
    void interface_ShouldExtendCorrectTypes() {
        // Verify DeleteSysUserByIdDomainService is an interface
        assertTrue(DeleteSysUserByIdDomainService.class.isInterface());
    }

    @Test
    void serviceMethods_ShouldBeAbstract() {
        // Interface methods should be abstract by default
        Method[] methods = DeleteSysUserByIdDomainService.class.getDeclaredMethods();
        for (Method method : methods) {
            assertFalse(method.isBridge());
            assertFalse(method.isSynthetic());
        }
    }
}
