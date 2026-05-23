package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenProjectInfoDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class GenDownloadDomainServiceImplTest {

    private final GenDownloadDomainServiceImpl service = new GenDownloadDomainServiceImpl();

    @Test
    @DisplayName("downloadCode 应返回 empty")
    void downloadCode_shouldReturnEmpty() {
        StepVerifier.create(service.downloadCode(new GenProjectInfoDTO()))
                .verifyComplete();
    }

    @Test
    @DisplayName("download 应返回 empty")
    void download_shouldReturnEmpty() {
        StepVerifier.create(service.download())
                .verifyComplete();
    }
}
