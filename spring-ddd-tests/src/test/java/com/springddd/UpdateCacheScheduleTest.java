package com.springddd;

import com.springddd.domain.leaf.UpdateCacheDomainService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UpdateCacheScheduleTest {

    @Test
    void shouldHaveUpdateCacheScheduleClass() {
        assertNotNull(UpdateCacheSchedule.class);
    }

    @Test
    void shouldHaveRequiredArgsConstructor() {
        assertNotNull(UpdateCacheSchedule.class.getConstructors().length > 0);
    }

    @Test
    void shouldHaveStartMethod() {
        assertNotNull(UpdateCacheSchedule.class.getDeclaredMethod("start"));
    }
}
