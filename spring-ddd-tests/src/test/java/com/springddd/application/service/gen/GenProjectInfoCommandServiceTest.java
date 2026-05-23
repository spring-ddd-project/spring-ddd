package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenProjectInfoCommand;
import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.factory.RepositoryFactory;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenProjectInfoCommandServiceTest {

    @Mock
    private RepositoryFactory repositoryFactory;

    @Mock
    private GenProjectInfoDomainFactory genProjectInfoDomainFactory;

    @Mock
    private WipeGenProjectInfoByIdsDomainService wipeGenInfoByIdsDomainService;

    @Mock
    private GenProjectInfoDomainRepository genProjectInfoDomainRepository;

    @InjectMocks
    private GenProjectInfoCommandService service;

    @Test
    @DisplayName("create 应创建项目信息并返回 ID")
    void create_shouldCreateAndReturnId() {
        GenProjectInfoCommand command = new GenProjectInfoCommand();
        command.setTableName("sys_user");
        command.setPackageName("com.example");
        command.setClassName("SysUser");
        command.setModuleName("user");
        command.setProjectName("demo");
        command.setPackageName("com.example");
        command.setClassName("SysUser");
        command.setModuleName("user");
        command.setProjectName("demo");
        command.setPackageName("com.example");
        command.setClassName("SysUser");
        command.setModuleName("user");
        command.setProjectName("demo");
        command.setRequestName("SysUserRequest");

        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        when(genProjectInfoDomainFactory.newInstance(any(ProjectInfo.class), any(GenProjectInfoExtendInfo.class))).thenReturn(domain);
        when(repositoryFactory.getGenProjectInfoDomainRepository()).thenReturn(genProjectInfoDomainRepository);
        when(genProjectInfoDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.create(command))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();

        verify(genProjectInfoDomainFactory).newInstance(any(ProjectInfo.class), any(GenProjectInfoExtendInfo.class));
        verify(genProjectInfoDomainRepository).save(domain);
    }

    @Test
    @DisplayName("update 应更新项目信息")
    void update_shouldUpdate() {
        GenProjectInfoCommand command = new GenProjectInfoCommand();
        command.setId(1L);
        command.setTableName("sys_user");
        command.setPackageName("com.example");
        command.setClassName("SysUser");
        command.setModuleName("user");
        command.setProjectName("demo");

        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        when(repositoryFactory.getGenProjectInfoDomainRepository()).thenReturn(genProjectInfoDomainRepository);
        when(genProjectInfoDomainRepository.load(new InfoId(1L))).thenReturn(Mono.just(domain));
        when(genProjectInfoDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.update(command))
                .verifyComplete();

        verify(genProjectInfoDomainRepository).load(new InfoId(1L));
        verify(genProjectInfoDomainRepository).save(domain);
    }

    @Test
    @DisplayName("delete 应软删除项目信息")
    void delete_shouldSoftDelete() {
        GenProjectInfoCommand command = new GenProjectInfoCommand();
        command.setId(1L);

        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.setDeleteStatus(false);
        when(repositoryFactory.getGenProjectInfoDomainRepository()).thenReturn(genProjectInfoDomainRepository);
        when(genProjectInfoDomainRepository.load(new InfoId(1L))).thenReturn(Mono.just(domain));
        when(genProjectInfoDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.delete(command))
                .verifyComplete();

        verify(genProjectInfoDomainRepository).load(new InfoId(1L));
        verify(genProjectInfoDomainRepository).save(domain);
    }

    @Test
    @DisplayName("wipeByIds 应调用 wipe 领域服务")
    void wipeByIds_shouldCallDomainService() {
        List<Long> ids = List.of(1L, 2L);
        when(wipeGenInfoByIdsDomainService.wipeByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(service.wipeByIds(ids))
                .verifyComplete();

        verify(wipeGenInfoByIdsDomainService).wipeByIds(ids);
    }
}
