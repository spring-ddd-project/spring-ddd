package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenDownloadDomainService;
import com.springddd.domain.gen.GenerateDomainService;
import com.springddd.domain.gen.WipeGenDataDomainService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenTableInfoCommandServiceTest {

    @Mock
    private WipeGenDataDomainService wipeGenDataDomainService;

    @Mock
    private GenerateDomainService generateDomainService;

    @Mock
    private GenDownloadDomainService genDownloadDomainService;

    @InjectMocks
    private GenTableInfoCommandService genTableInfoCommandService;

    @Test
    void wipe_shouldCallWipeDomainService() {
        when(wipeGenDataDomainService.wipe()).thenReturn(Mono.empty());

        StepVerifier.create(genTableInfoCommandService.wipe())
                .verifyComplete();

        verify(wipeGenDataDomainService).wipe();
    }

    @Test
    void generate_shouldCallGenerateDomainService() {
        String tableName = "test_table";
        when(generateDomainService.generate(tableName)).thenReturn(Mono.empty());

        StepVerifier.create(genTableInfoCommandService.generate(tableName))
                .verifyComplete();

        verify(generateDomainService).generate(tableName);
    }
}
