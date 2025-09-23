package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SysUserRoleQueryServiceTest {

    @Test
    void service_shouldBeCreated_withDependencies() {
        SysUserRoleQueryService service = new SysUserRoleQueryService(null, null);
        assertNotNull(service);
    }

    @Test
    void service_shouldNotBeNull() {
        assertNotNull(new SysUserRoleQueryService(null, null));
    }
}
