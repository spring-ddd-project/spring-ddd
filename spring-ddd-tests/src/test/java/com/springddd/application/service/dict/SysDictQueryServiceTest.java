package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SysDictQueryServiceTest {

    @Test
    void service_shouldBeCreated_withDependencies() {
        // Test that the service can be created with dependencies
        SysDictQueryService service = new SysDictQueryService(
                null, null, null, null
        );
        assertNotNull(service);
    }

    @Test
    void service_shouldNotBeNull() {
        assertNotNull(new SysDictQueryService(null, null, null, null));
    }
}
