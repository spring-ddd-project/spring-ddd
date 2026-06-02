package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenDownloadDomainService;
import com.springddd.domain.gen.GenerateDomainService;
import com.springddd.domain.gen.WipeGenDataDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenTableInfoCommandServiceTest {

    @Mock
    private WipeGenDataDomainService wipeGenDataDomainService;

    @Mock
    private GenerateDomainService generateDomainService;

    @Mock
    private GenDownloadDomainService genDownloadDomainService;

    private GenTableInfoCommandService genTableInfoCommandService;

    @BeforeEach
    void setUp() {
        genTableInfoCommandService = new GenTableInfoCommandService(
                wipeGenDataDomainService,
                generateDomainService,
                genDownloadDomainService
        );
    }

    @Test
    void wipe_shouldDelegateToDomainService() {
        when(wipeGenDataDomainService.wipe()).thenReturn(Mono.empty());

        StepVerifier.create(genTableInfoCommandService.wipe())
                .verifyComplete();
    }

    @Test
    void generate_shouldDelegateToDomainService() {
        String tableName = "sys_user";
        when(generateDomainService.generate(tableName)).thenReturn(Mono.empty());

        StepVerifier.create(genTableInfoCommandService.generate(tableName))
                .verifyComplete();
    }

    @Test
    void download_shouldDelegateToDomainService() {
        @SuppressWarnings("unchecked")
        ResponseEntity<Resource> response = ResponseEntity.ok().build();
        when(genDownloadDomainService.download()).thenReturn(Mono.just(response));

        StepVerifier.create(genTableInfoCommandService.download())
                .expectNext(response)
                .verifyComplete();
    }
}
