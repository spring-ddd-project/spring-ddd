package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenProjectInfoCommand;
import com.springddd.domain.gen.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenProjectInfoCommandServiceTest {

    @Mock
    private GenProjectInfoDomainRepository genProjectInfoDomainRepository;

    @Mock
    private GenProjectInfoDomainFactory genProjectInfoDomainFactory;

    @Mock
    private WipeGenProjectInfoByIdsDomainService wipeGenInfoByIdsDomainService;

    @InjectMocks
    private GenProjectInfoCommandService genProjectInfoCommandService;

    private GenProjectInfoCommand createCommand;
    private GenProjectInfoDomain mockDomain;

    @BeforeEach
    void setUp() {
        createCommand = new GenProjectInfoCommand();
        createCommand.setTableName("test_table");
        createCommand.setPackageName("com.example");
        createCommand.setClassName("TestClass");
        createCommand.setModuleName("module");
        createCommand.setProjectName("project");
        createCommand.setRequestName("TestRequest");

        mockDomain = new GenProjectInfoDomain();
        mockDomain.setProjectInfo(new ProjectInfo("test_table", "com.example", "TestClass", "module", "project"));
        mockDomain.setExtendInfo(new GenProjectInfoExtendInfo("TestRequest"));
        mockDomain.setDeleteStatus(false);
    }

    @Test
    void create_shouldCreateProjectInfo() {
        when(genProjectInfoDomainFactory.newInstance(any(ProjectInfo.class), any(GenProjectInfoExtendInfo.class)))
                .thenReturn(mockDomain);
        when(genProjectInfoDomainRepository.save(any(GenProjectInfoDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(genProjectInfoCommandService.create(createCommand))
                .expectNext(1L)
                .verifyComplete();

        verify(genProjectInfoDomainFactory).newInstance(any(ProjectInfo.class), any(GenProjectInfoExtendInfo.class));
        verify(genProjectInfoDomainRepository).save(any(GenProjectInfoDomain.class));
    }

    @Test
    void wipeByIds_shouldCallWipeDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeGenInfoByIdsDomainService.wipeByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(genProjectInfoCommandService.wipeByIds(ids))
                .verifyComplete();

        verify(wipeGenInfoByIdsDomainService).wipeByIds(ids);
    }
}
