package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenDownloadDomainService;
import com.springddd.domain.gen.GenerateDomainService;
import com.springddd.domain.gen.WipeGenDataDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
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
    private GenTableInfoCommandService service;

    @Test
    @DisplayName("wipe 应调用 wipe 领域服务")
    void wipe_shouldCallDomainService() {
        when(wipeGenDataDomainService.wipe()).thenReturn(Mono.empty());

        StepVerifier.create(service.wipe())
                .verifyComplete();

        verify(wipeGenDataDomainService).wipe();
    }

    @Test
    @DisplayName("generate 应调用 generate 领域服务")
    void generate_shouldCallDomainService() {
        when(generateDomainService.generate("sys_user")).thenReturn(Mono.empty());

        StepVerifier.create(service.generate("sys_user"))
                .verifyComplete();

        verify(generateDomainService).generate("sys_user");
    }

    @Test
    @DisplayName("download 应返回 ResponseEntity")
    void download_shouldReturnResponseEntity() {
        Resource resource = new ByteArrayResource("content".getBytes());
        ResponseEntity<Resource> response = ResponseEntity.ok(resource);
        when(genDownloadDomainService.download()).thenReturn(Mono.just(response));

        StepVerifier.create(service.download())
                .assertNext(result -> assertThat(result.getStatusCode().is2xxSuccessful()).isTrue())
                .verifyComplete();
    }
}
