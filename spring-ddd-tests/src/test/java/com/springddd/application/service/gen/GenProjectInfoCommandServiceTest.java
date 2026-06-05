package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenProjectInfoCommand;
import com.springddd.domain.gen.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenProjectInfoCommandServiceTest {

    @Mock
    private GenProjectInfoDomainRepository genProjectInfoDomainRepository;

    @Mock
    private GenProjectInfoDomainFactory genProjectInfoDomainFactory;

    @Mock
    private WipeGenProjectInfoByIdsDomainService wipeGenInfoByIdsDomainService;

    private GenProjectInfoCommandService genProjectInfoCommandService;

    @BeforeEach
    void setUp() {
        genProjectInfoCommandService = new GenProjectInfoCommandService(
                genProjectInfoDomainRepository,
                genProjectInfoDomainFactory,
                wipeGenInfoByIdsDomainService
        );
    }

    @Test
    void create_shouldReturnId_whenValidCommand() {
        GenProjectInfoCommand command = new GenProjectInfoCommand();
        command.setTableName("sys_user");
        command.setPackageName("com.springddd");
        command.setClassName("SysUser");
        command.setModuleName("user");
        command.setProjectName("spring-ddd");
        command.setRequestName("SysUserRequest");

        GenProjectInfoDomain mockDomain = new GenProjectInfoDomain();
        when(genProjectInfoDomainFactory.newInstance(any(), any())).thenReturn(mockDomain);
        when(genProjectInfoDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(genProjectInfoCommandService.create(command))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void update_shouldComplete_whenValidCommand() {
        GenProjectInfoCommand command = new GenProjectInfoCommand();
        command.setId(1L);
        command.setTableName("sys_role");
        command.setPackageName("com.updated");
        command.setClassName("SysRole");
        command.setModuleName("role");
        command.setProjectName("updated-project");
        command.setRequestName("SysRoleRequest");

        GenProjectInfoDomain mockDomain = new GenProjectInfoDomain();
        when(genProjectInfoDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(genProjectInfoDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(genProjectInfoCommandService.update(command))
                .verifyComplete();
    }

    @Test
    void delete_shouldComplete_whenValidCommand() {
        GenProjectInfoCommand command = new GenProjectInfoCommand();
        command.setId(1L);

        GenProjectInfoDomain mockDomain = new GenProjectInfoDomain();
        mockDomain.setDeleteStatus(false);
        when(genProjectInfoDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(genProjectInfoDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(genProjectInfoCommandService.delete(command))
                .verifyComplete();
    }

    @Test
    void wipeByIds_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeGenInfoByIdsDomainService.wipeByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(genProjectInfoCommandService.wipeByIds(ids))
                .verifyComplete();
    }
}
