package com.springddd.application.service.gen.command;

import com.springddd.application.service.gen.GenDownloadDomainServiceImpl;
import com.springddd.application.service.gen.dto.GenProjectInfoDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DownloadCommandTest {

    @Mock
    private GenDownloadDomainServiceImpl genDownloadDomainService;

    @InjectMocks
    private DownloadCommand downloadCommand;

    @Test
    @DisplayName("execute 应调用 download")
    void execute_shouldCallDownload() {
        GenProjectInfoDTO dto = new GenProjectInfoDTO();
        when(genDownloadDomainService.download()).thenReturn(Mono.empty());

        StepVerifier.create(downloadCommand.execute(dto))
                .verifyComplete();

        verify(genDownloadDomainService).download();
    }
}
