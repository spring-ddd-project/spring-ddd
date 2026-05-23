package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenProjectInfoDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class GenerateDomainServiceImplTest {

    private final GenerateDomainServiceImpl service = new GenerateDomainServiceImpl();

    @Test
    @DisplayName("generate 应返回 empty")
    void generate_shouldReturnEmpty() {
        StepVerifier.create(service.generate("t_user"))
                .verifyComplete();
    }

    @Test
    @DisplayName("generateProject 应返回 empty")
    void generateProject_shouldReturnEmpty() {
        StepVerifier.create(service.generateProject(new GenProjectInfoDTO()))
                .verifyComplete();
    }
}
