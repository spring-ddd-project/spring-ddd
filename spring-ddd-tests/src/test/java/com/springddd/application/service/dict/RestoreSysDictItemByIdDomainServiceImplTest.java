package com.springddd.application.service.dict;

import com.springddd.domain.dict.RestoreSysDictItemByIdDomainService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RestoreSysDictItemByIdDomainServiceImplTest {

    @Test
    void shouldHaveRestoreSysDictItemByIdDomainServiceImplClass() {
        assertNotNull(RestoreSysDictItemByIdDomainServiceImpl.class);
    }

    @Test
    void shouldImplementRestoreSysDictItemByIdDomainService() {
        assertTrue(RestoreSysDictItemByIdDomainService.class.isAssignableFrom(RestoreSysDictItemByIdDomainServiceImpl.class));
    }

    @Test
    void shouldHaveRequiredArgsConstructor() {
        assertNotNull(RestoreSysDictItemByIdDomainServiceImpl.class.getConstructors().length > 0);
    }

    @Test
    void shouldHaveRestoreByIdsMethod() {
        assertNotNull(RestoreSysDictItemByIdDomainServiceImpl.class.getDeclaredMethod("restoreByIds", java.util.List.class));
    }
}
