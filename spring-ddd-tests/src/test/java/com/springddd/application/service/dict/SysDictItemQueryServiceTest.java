package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SysDictItemQueryServiceTest {

    @Test
    void service_shouldBeCreated_withDependencies() {
        SysDictItemQueryService service = new SysDictItemQueryService(null, null);
        assertNotNull(service);
    }

    @Test
    void service_shouldNotBeNull() {
        assertNotNull(new SysDictItemQueryService(null, null));
    }
}
