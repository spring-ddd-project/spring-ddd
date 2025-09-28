package com.springddd.domain.gen;

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
class GenDownloadDomainServiceTest {

    @Mock
    private GenDownloadDomainService genDownloadDomainService;

    @Mock
    private Resource mockResource;

    @Test
    void shouldReturnResponseEntityResource() {
        ResponseEntity<Resource> expectedResponse = ResponseEntity.ok(mockResource);

        when(genDownloadDomainService.download())
                .thenReturn(Mono.just(expectedResponse));

        StepVerifier.create(genDownloadDomainService.download())
                .expectNext(expectedResponse)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenNoResource() {
        when(genDownloadDomainService.download())
                .thenReturn(Mono.just(ResponseEntity.noContent().build()));

        StepVerifier.create(genDownloadDomainService.download())
                .expectNext(ResponseEntity.noContent().build())
                .verifyComplete();
    }
}
