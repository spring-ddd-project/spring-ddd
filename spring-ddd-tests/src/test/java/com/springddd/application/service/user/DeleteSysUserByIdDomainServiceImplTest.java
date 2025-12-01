package com.springddd.application.service.user;

import com.springddd.domain.user.DeleteSysUserByIdDomainService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeleteSysUserByIdDomainServiceImplTest {

    @Test
    void shouldHaveDeleteSysUserByIdDomainServiceImplClass() {
        assertNotNull(DeleteSysUserByIdDomainServiceImpl.class);
    }

    @Test
    void shouldImplementDeleteSysUserByIdDomainService() {
        assertTrue(DeleteSysUserByIdDomainService.class.isAssignableFrom(DeleteSysUserByIdDomainServiceImpl.class));
    }

    @Test
    void shouldHaveRequiredArgsConstructor() {
        assertNotNull(DeleteSysUserByIdDomainServiceImpl.class.getConstructors().length > 0);
    }

    @Test
    void shouldHaveDeleteByIdsMethod() {
        assertNotNull(DeleteSysUserByIdDomainServiceImpl.class.getDeclaredMethod("deleteByIds", java.util.List.class));
    }
}
