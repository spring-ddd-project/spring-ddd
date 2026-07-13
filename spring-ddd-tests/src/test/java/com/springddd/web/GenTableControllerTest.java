package com.springddd.web;

import com.springddd.application.service.gen.GenTableInfoCommandService;
import com.springddd.application.service.gen.GenTableInfoQueryService;
import com.springddd.application.service.gen.dto.GenTableInfoPageQuery;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@WebFluxTest(GenTableController.class)
@Import({
        GenTableControllerTest.TestSecurityConfig.class,
        GenTableControllerTest.MockConfig.class
})
class GenTableControllerTest {

    @Autowired
    private GenTableController controller;

    @Autowired
    private GenTableInfoQueryService genTableInfoQueryService;

    @Autowired
    private GenTableInfoCommandService genTableInfoCommandService;

    static class TestSecurityConfig {
        @Bean
        public SecurityWebFilterChain testSecurityWebFilterChain(ServerHttpSecurity http) {
            return http
                    .csrf(ServerHttpSecurity.CsrfSpec::disable)
                    .authorizeExchange(exchange -> exchange.anyExchange().permitAll())
                    .build();
        }
    }

    @TestConfiguration
    static class MockConfig {

        @Bean
        GenTableInfoQueryService genTableInfoQueryService() {
            return Mockito.mock(GenTableInfoQueryService.class, defaultAnswer());
        }

        @Bean
        GenTableInfoCommandService genTableInfoCommandService() {
            return Mockito.mock(GenTableInfoCommandService.class, defaultAnswer());
        }

    }

    @SuppressWarnings("unchecked")
    private static Answer<Object> defaultAnswer() {
        return invocation -> {
            Class<?> returnType = invocation.getMethod().getReturnType();
            if (returnType == Mono.class) {
                return Mono.empty();
            }
            if (returnType == Flux.class) {
                return Flux.empty();
            }
            return Mockito.RETURNS_DEFAULTS.answer(invocation);
        };
    }

    @Test
    void tableIndexShouldReturnOk() {
        StepVerifier.create(controller.tableIndex(Mono.just(new GenTableInfoPageQuery())))
                .assertNext(apiResponse -> org.junit.jupiter.api.Assertions.assertEquals(0, apiResponse.getCode()))
                .verifyComplete();
    }

    @Test
    void previewShouldReturnOk() {
        StepVerifier.create(controller.preview())
                .assertNext(apiResponse -> org.junit.jupiter.api.Assertions.assertEquals(0, apiResponse.getCode()))
                .verifyComplete();
    }

    @Test
    void downloadShouldReturnOk() {
        StepVerifier.create(controller.download()).verifyComplete();
    }

    @Test
    void wipeShouldReturnOk() {
        StepVerifier.create(controller.wipe())
                .assertNext(apiResponse -> org.junit.jupiter.api.Assertions.assertEquals(0, apiResponse.getCode()))
                .verifyComplete();
    }

    @Test
    void generateShouldReturnOk() {
        StepVerifier.create(controller.generate("test"))
                .assertNext(apiResponse -> org.junit.jupiter.api.Assertions.assertEquals(0, apiResponse.getCode()))
                .verifyComplete();
    }
}
