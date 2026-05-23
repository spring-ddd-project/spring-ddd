package com.springddd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

class SpringDDDTestsApplicationTest {

    @Test
    @DisplayName("main 应启动 Spring 应用")
    void main_shouldStartApplication() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(SpringDDDTestsApplication.class, new String[]{}))
                    .thenReturn(mock(ConfigurableApplicationContext.class));

            SpringDDDTestsApplication.main(new String[]{});

            mocked.verify(() -> SpringApplication.run(SpringDDDTestsApplication.class, new String[]{}));
        }
    }
}
