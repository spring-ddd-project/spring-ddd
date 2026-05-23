package com.springddd.application.service.gen.command;

import com.springddd.application.service.gen.GenerateDomainServiceImpl;
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
class PreviewCommandTest {

    @Mock
    private GenerateDomainServiceImpl generateDomainService;

    @InjectMocks
    private PreviewCommand previewCommand;

    @Test
    @DisplayName("execute 应调用 generateProject")
    void execute_shouldCallGenerateProject() {
        GenProjectInfoDTO dto = new GenProjectInfoDTO();
        when(generateDomainService.generateProject(any())).thenReturn(Mono.empty());

        StepVerifier.create(previewCommand.execute(dto))
                .verifyComplete();

        verify(generateDomainService).generateProject(dto);
    }
}
