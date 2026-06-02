package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerateDomainServiceTest {

    @Mock
    private GenerateDomainService generateDomainService;

    @Test
    void shouldGenerateWithTableName() {
        String tableName = "sys_user";

        when(generateDomainService.generate(tableName))
                .thenReturn(Mono.empty());

        StepVerifier.create(generateDomainService.generate(tableName))
                .verifyComplete();
    }

    @Test
    void shouldGenerateWithDifferentTableName() {
        String tableName = "gen_columns";

        when(generateDomainService.generate(tableName))
                .thenReturn(Mono.empty());

        StepVerifier.create(generateDomainService.generate(tableName))
                .verifyComplete();
    }

    @Test
    void shouldGenerateWithEmptyTableName() {
        String tableName = "";

        when(generateDomainService.generate(tableName))
                .thenReturn(Mono.empty());

        StepVerifier.create(generateDomainService.generate(tableName))
                .verifyComplete();
    }
}
