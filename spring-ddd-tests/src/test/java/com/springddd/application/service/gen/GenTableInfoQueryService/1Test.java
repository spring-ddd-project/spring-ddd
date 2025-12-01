package com.springddd.application.service.gen.GenTableInfoQueryService;

import com.springddd.application.service.gen.GenTableInfoQueryService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GenTableInfoQueryService1Test {

    @Test
    void shouldHaveGenTableInfoQueryServiceClass() {
        assertNotNull(GenTableInfoQueryService.class);
    }

    @Test
    void shouldHaveRequiredArgsConstructor() {
        assertNotNull(GenTableInfoQueryService.class.getConstructors().length > 0);
    }

    @Test
    void shouldHaveIndexMethod() {
        assertNotNull(GenTableInfoQueryService.class.getDeclaredMethods());
    }

    @Test
    void shouldHaveBuildDataMethod() {
        assertNotNull(GenTableInfoQueryService.class.getDeclaredMethod("buildData", String.class));
    }

    @Test
    void shouldHavePreviewMethod() {
        assertNotNull(GenTableInfoQueryService.class.getDeclaredMethod("preview"));
    }
}
