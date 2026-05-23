package com.springddd.application.service.dept;

import com.springddd.application.service.dept.dto.SysDeptCommand;
import com.springddd.application.service.permission.DataScopeCriteriaBuilder;
import com.springddd.domain.dept.*;
import com.springddd.infrastructure.persistence.factory.RepositoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SysDeptCommandServiceTest {

    @Mock
    private RepositoryFactory repositoryFactory;

    @Mock
    private SysDeptDomainFactory sysDeptDomainFactory;

    @Mock
    private WipeSysDeptByIdsDomainService wipeSysDeptByIdsDomainService;

    @Mock
    private DeleteSysDeptByIdDomainService deleteSysDeptByIdDomainService;

    @Mock
    private RestoreSysDeptByIdDomainService restoreSysDeptByIdDomainService;

    @Mock
    private DataScopeCriteriaBuilder dataScopeCriteriaBuilder;

    @Mock
    private SysDeptDomainRepository sysDeptDomainRepository;

    @InjectMocks
    private SysDeptCommandService sysDeptCommandService;

    @BeforeEach
    void setUp() {
        when(repositoryFactory.getSysDeptDomainRepository()).thenReturn(sysDeptDomainRepository);
        when(dataScopeCriteriaBuilder.evictDeptTreeCache()).thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("create 应调用 factory 和 repository 保存")
    void create_shouldCallFactoryAndSave() {
        SysDeptCommand command = new SysDeptCommand();
        command.setParentId(0L);
        command.setDeptName("Test Dept");
        command.setSortOrder(1);
        command.setDeptStatus(true);

        SysDeptDomain domain = mock(SysDeptDomain.class);
        when(sysDeptDomainFactory.newInstance(any(DeptId.class), any(DeptBasicInfo.class), any(DeptExtendInfo.class)))
                .thenReturn(domain);
        when(sysDeptDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(sysDeptCommandService.create(command))
                .expectNext(1L)
                .verifyComplete();

        verify(domain).create();
        verify(sysDeptDomainRepository).save(domain);
        verify(dataScopeCriteriaBuilder).evictDeptTreeCache();
    }

    @Test
    @DisplayName("update 应加载 domain 并更新保存")
    void update_shouldLoadAndUpdate() {
        SysDeptCommand command = new SysDeptCommand();
        command.setId(1L);
        command.setParentId(0L);
        command.setDeptName("Updated Dept");
        command.setSortOrder(2);
        command.setDeptStatus(true);

        SysDeptDomain domain = mock(SysDeptDomain.class);
        when(sysDeptDomainRepository.load(new DeptId(1L))).thenReturn(Mono.just(domain));
        when(sysDeptDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(sysDeptCommandService.update(command))
                .verifyComplete();

        verify(domain).update(any(DeptId.class), any(DeptBasicInfo.class), any(DeptExtendInfo.class));
        verify(sysDeptDomainRepository).save(domain);
        verify(dataScopeCriteriaBuilder).evictDeptTreeCache();
    }

    @Test
    @DisplayName("delete 应调用 delete domain service")
    void delete_shouldCallDeleteDomainService() {
        when(deleteSysDeptByIdDomainService.deleteByIds(List.of(1L, 2L))).thenReturn(Mono.empty());

        StepVerifier.create(sysDeptCommandService.delete(List.of(1L, 2L)))
                .verifyComplete();

        verify(deleteSysDeptByIdDomainService).deleteByIds(List.of(1L, 2L));
        verify(dataScopeCriteriaBuilder).evictDeptTreeCache();
    }

    @Test
    @DisplayName("wipe 应调用 wipe domain service")
    void wipe_shouldCallWipeDomainService() {
        when(wipeSysDeptByIdsDomainService.deleteByIds(List.of(1L, 2L))).thenReturn(Mono.empty());

        StepVerifier.create(sysDeptCommandService.wipe(List.of(1L, 2L)))
                .verifyComplete();

        verify(wipeSysDeptByIdsDomainService).deleteByIds(List.of(1L, 2L));
        verify(dataScopeCriteriaBuilder).evictDeptTreeCache();
    }

    @Test
    @DisplayName("restore 应调用 restore domain service")
    void restore_shouldCallRestoreDomainService() {
        when(restoreSysDeptByIdDomainService.restoreByIds(List.of(1L, 2L))).thenReturn(Mono.empty());

        StepVerifier.create(sysDeptCommandService.restore(List.of(1L, 2L)))
                .verifyComplete();

        verify(restoreSysDeptByIdDomainService).restoreByIds(List.of(1L, 2L));
        verify(dataScopeCriteriaBuilder).evictDeptTreeCache();
    }
}
