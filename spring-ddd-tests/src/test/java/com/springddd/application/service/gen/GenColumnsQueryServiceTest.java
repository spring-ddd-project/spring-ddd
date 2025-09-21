package com.springddd.application.service.gen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GenColumnsQueryServiceTest {

    @Test
    void service_shouldBeCreated() {
        // This test verifies the basic structure of the service
        GenColumnsQueryService service = new GenColumnsQueryService(null, null, null, null, null, null);
        assertNotNull(service);
    }
}
