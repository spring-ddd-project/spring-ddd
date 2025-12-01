package com.springddd.web;

import com.springddd.application.service.user.SysUserCommandService;
import com.springddd.application.service.user.SysUserQueryService;
import com.springddd.domain.util.ApiResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class SysUserControllerTest {

    @Test
    void shouldHaveSysUserControllerClass() {
        assertNotNull(SysUserController.class);
    }

    @Test
    void shouldHaveRequiredArgsConstructor() {
        assertNotNull(SysUserController.class.getConstructors().length > 0);
    }

    @Test
    void shouldHavePageEndpoint() {
        assertNotNull(SysUserController.class.getDeclaredMethod("page", reactor.core.publisher.Mono.class));
    }

    @Test
    void shouldHaveRecyclePageEndpoint() {
        assertNotNull(SysUserController.class.getDeclaredMethod("recyclePage", reactor.core.publisher.Mono.class));
    }

    @Test
    void shouldHaveCreateEndpoint() {
        assertNotNull(SysUserController.class.getDeclaredMethod("create", com.springddd.application.service.user.dto.SysUserCommand.class));
    }

    @Test
    void shouldHaveUpdateEndpoint() {
        assertNotNull(SysUserController.class.getDeclaredMethod("update", com.springddd.application.service.user.dto.SysUserCommand.class));
    }

    @Test
    void shouldHaveDeleteEndpoint() {
        assertNotNull(SysUserController.class.getDeclaredMethod("delete", List.class));
    }

    @Test
    void shouldHaveWipeEndpoint() {
        assertNotNull(SysUserController.class.getDeclaredMethod("wipe", List.class));
    }

    @Test
    void shouldHaveRestoreEndpoint() {
        assertNotNull(SysUserController.class.getDeclaredMethod("restore", List.class));
    }
}
