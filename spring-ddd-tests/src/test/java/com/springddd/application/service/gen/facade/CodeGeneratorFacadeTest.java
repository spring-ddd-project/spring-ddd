package com.springddd.application.service.gen.facade;

import com.springddd.application.service.gen.GenTableInfoQueryService;
import com.springddd.application.service.gen.dto.ProjectTreeView;
import com.springddd.domain.gen.GenerateDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CodeGeneratorFacadeTest {

    @Mock
    private GenerateDomainService generateDomainService;

    @Mock
    private GenTableInfoQueryService genTableInfoQueryService;

    @InjectMocks
    private CodeGeneratorFacade codeGeneratorFacade;

    @Test
    @DisplayName("generateCode 应委托给 generateDomainService")
    void generateCode_shouldDelegateToGenerateDomainService() {
        when(generateDomainService.generate("sys_user")).thenReturn(Mono.empty());

        StepVerifier.create(codeGeneratorFacade.generateCode("sys_user"))
                .verifyComplete();

        verify(generateDomainService).generate("sys_user");
    }

    @Test
    @DisplayName("previewCode 应返回预览列表")
    void previewCode_shouldReturnPreviewList() {
        ProjectTreeView view = new ProjectTreeView();
        view.setLabel("test");

        when(genTableInfoQueryService.preview()).thenReturn(Mono.just(List.of(view)));

        StepVerifier.create(codeGeneratorFacade.previewCode())
                .assertNext(list -> {
                    assertThat(list).hasSize(1);
                    assertThat(list.get(0).getLabel()).isEqualTo("test");
                })
                .verifyComplete();
    }
}
