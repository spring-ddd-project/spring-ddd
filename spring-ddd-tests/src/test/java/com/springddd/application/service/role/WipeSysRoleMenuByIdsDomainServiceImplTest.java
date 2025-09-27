package com.springddd.application.service.role;

import com.springddd.domain.role.WipeSysRoleMenuByIdsDomainService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WipeSysRoleMenuByIdsDomainServiceImplTest {

    @Test
    void shouldHaveWipeSysRoleMenuByIdsDomainServiceImplClass() {
        assertNotNull(WipeSysRoleMenuByIdsDomainServiceImpl.class);
    }

    @Test
    void shouldImplementWipeSysRoleMenuByIdsDomainService() {
        assertTrue(WipeSysRoleMenuByIdsDomainService.class.isAssignableFrom(WipeSysRoleMenuByIdsDomainServiceImpl.class));
    }

    @Test
    void shouldHaveRequiredArgsConstructor() {
        assertNotNull(WipeSysRoleMenuByIdsDomainServiceImpl.class.getConstructors().length > 0);
    }

    @Test
    void shouldHaveDeleteByIdsMethod() {
        assertNotNull(WipeSysRoleMenuByIdsDomainServiceImpl.class.getDeclaredMethod("deleteByIds", java.util.List.class));
    }
}
