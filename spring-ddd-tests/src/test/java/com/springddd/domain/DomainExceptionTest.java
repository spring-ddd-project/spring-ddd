package com.springddd.domain;

import com.springddd.domain.util.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DomainExceptionTest {

    @Test
    void testDomainExceptionWithErrorCodeOnly() {
        DomainException exception = new TestDomainException(ErrorCode.USER_NAME_NULL);

        assertEquals(ErrorCode.USER_NAME_NULL, exception.getErrorCode());
        assertEquals(1000, exception.getCode());
        assertEquals("error.user.name.null", exception.getMessageKey());
        assertEquals("error.user.name.null", exception.getMessage());
        assertArrayEquals(new Object[]{}, exception.getArgs());
    }

    @Test
    void testDomainExceptionWithErrorCodeAndArgs() {
        DomainException exception = new TestDomainException(ErrorCode.ROLE_NAME_NULL, "admin", "role");

        assertEquals(ErrorCode.ROLE_NAME_NULL, exception.getErrorCode());
        assertEquals(1101, exception.getCode());
        assertEquals("error.role.name.null", exception.getMessageKey());
        assertEquals("error.role.name.null", exception.getMessage());
        assertArrayEquals(new Object[]{"admin", "role"}, exception.getArgs());
    }

    @Test
    void testGetCode() {
        DomainException exception1 = new TestDomainException(ErrorCode.USER_NAME_NULL);
        assertEquals(1000, exception1.getCode());

        DomainException exception2 = new TestDomainException(ErrorCode.MENU_NAME_NULL);
        assertEquals(1200, exception2.getCode());

        DomainException exception3 = new TestDomainException(ErrorCode.GEN_INFO_PACKAGE_NAME_NULL);
        assertEquals(1500, exception3.getCode());
    }

    @Test
    void testGetMessageKey() {
        DomainException exception = new TestDomainException(ErrorCode.DICT_CODE_NULL);
        assertEquals("error.dict.code.null", exception.getMessageKey());
    }

    @Test
    void testInheritance() {
        DomainException exception = new TestDomainException(ErrorCode.USER_PASSWORD_NULL);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testExceptionMessageFormat() {
        // The message is the errorCode's messageKey
        DomainException exception = new TestDomainException(ErrorCode.GEN_INFO_TABLE_NAME_NULL);
        assertEquals("error.gen.info.table.name.null", exception.getMessage());
    }

    // Test helper class to allow testing abstract DomainException
    private static class TestDomainException extends DomainException {
        public TestDomainException(ErrorCode errorCode, Object... args) {
            super(errorCode, args);
        }
    }
}
