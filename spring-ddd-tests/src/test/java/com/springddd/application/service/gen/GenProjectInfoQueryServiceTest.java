package com.springddd.application.service.gen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GenProjectInfoQueryServiceTest {

    @Test
    void service_shouldBeCreated() {
        // This test verifies the basic structure of the service
        GenProjectInfoQueryService service = new GenProjectInfoQueryService(null, null);
        assertNotNull(service);
    }
}
