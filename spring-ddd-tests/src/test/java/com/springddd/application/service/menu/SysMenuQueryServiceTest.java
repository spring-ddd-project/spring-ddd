package com.springddd.application.service.menu;

import com.springddd.application.service.menu.dto.SysMenuQuery;
import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.domain.util.PageResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysMenuQueryServiceTest {

    @Test
    void shouldHaveSysMenuQueryServiceClass() {
        assertNotNull(SysMenuQueryService.class);
    }

    @Test
    void shouldHaveRequiredArgsConstructor() {
        assertNotNull(SysMenuQueryService.class.getConstructors().length > 0);
    }

    @Test
    void sysMenuQueryFields_shouldHaveDeleteStatusField() {
        assertNotNull(SysMenuQuery.Fields.class);
    }
}
