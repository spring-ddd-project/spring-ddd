package com.springddd.application.service.dept;

import com.springddd.application.service.dept.dto.SysDeptCommand;
import com.springddd.domain.dept.*;
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
class SysDeptCommandServiceTest {

    @Mock
    private SysDeptDomainRepository sysDeptDomainRepository;

    @Mock
    private SysDeptDomainFactory sysDeptDomainFactory;

    @Mock
    private WipeSysDeptByIdsDomainService wipeSysDeptByIdsDomainService;

    @Mock
    private DeleteSysDeptByIdDomainService deleteSysDeptByIdDomainService;

    @Mock
    private RestoreSysDeptByIdDomainService restoreSysDeptByIdDomainService;

    @InjectMocks
    private SysDeptCommandService sysDeptCommandService;

    private SysDeptCommand createCommand;
    private SysDeptDomain mockDomain;

    @BeforeEach
    void setUp() {
        createCommand = new SysDeptCommand();
        createCommand.setDeptName("Test Dept");
        createCommand.setParentId(0L);
        createCommand.setSortOrder(1);
        createCommand.setDeptStatus(true);

        mockDomain = new SysDeptDomain();
        mockDomain.setDeptBasicInfo(new DeptBasicInfo("Test Dept"));
        mockDomain.setDeptExtendInfo(new DeptExtendInfo(1, true));
        mockDomain.setDeleteStatus(false);
    }

    @Test
    void create_shouldCreateDepartment() {
        when(sysDeptDomainFactory.newInstance(any(DeptId.class), any(DeptBasicInfo.class), any(DeptExtendInfo.class)))
                .thenReturn(mockDomain);
        when(sysDeptDomainRepository.save(any(SysDeptDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(sysDeptCommandService.create(createCommand))
                .expectNext(1L)
                .verifyComplete();

        verify(sysDeptDomainFactory).newInstance(any(DeptId.class), any(DeptBasicInfo.class), any(DeptExtendInfo.class));
        verify(sysDeptDomainRepository).save(any(SysDeptDomain.class));
    }

    @Test
    void create_shouldReturnIdOnSuccess() {
        when(sysDeptDomainFactory.newInstance(any(DeptId.class), any(DeptBasicInfo.class), any(DeptExtendInfo.class)))
                .thenReturn(mockDomain);
        when(sysDeptDomainRepository.save(any(SysDeptDomain.class))).thenReturn(Mono.just(100L));

        StepVerifier.create(sysDeptCommandService.create(createCommand))
                .expectNext(100L)
                .verifyComplete();
    }

    @Test
    void update_shouldUpdateDepartment() {
        SysDeptCommand updateCommand = new SysDeptCommand();
        updateCommand.setId(1L);
        updateCommand.setDeptName("Updated Dept");
        updateCommand.setParentId(0L);
        updateCommand.setSortOrder(2);
        updateCommand.setDeptStatus(false);

        when(sysDeptDomainRepository.load(any(DeptId.class))).thenReturn(Mono.just(mockDomain));
        when(sysDeptDomainRepository.save(any(SysDeptDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(sysDeptCommandService.update(updateCommand))
                .verifyComplete();

        verify(sysDeptDomainRepository).load(any(DeptId.class));
        verify(sysDeptDomainRepository).save(any(SysDeptDomain.class));
    }

    @Test
    void update_shouldCompleteWhenDomainNotFound() {
        SysDeptCommand updateCommand = new SysDeptCommand();
        updateCommand.setId(999L);
        updateCommand.setDeptName("Updated Dept");
        updateCommand.setParentId(0L);

        when(sysDeptDomainRepository.load(any(DeptId.class))).thenReturn(Mono.empty());

        StepVerifier.create(sysDeptCommandService.update(updateCommand))
                .verifyComplete();

        verify(sysDeptDomainRepository, never()).save(any());
    }

    @Test
    void delete_shouldCallDeleteDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(deleteSysDeptByIdDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysDeptCommandService.delete(ids))
                .verifyComplete();

        verify(deleteSysDeptByIdDomainService).deleteByIds(ids);
    }

    @Test
    void wipe_shouldCallWipeDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeSysDeptByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysDeptCommandService.wipe(ids))
                .verifyComplete();

        verify(wipeSysDeptByIdsDomainService).deleteByIds(ids);
    }

    @Test
    void restore_shouldCallRestoreDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(restoreSysDeptByIdDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysDeptCommandService.restore(ids))
                .verifyComplete();

        verify(restoreSysDeptByIdDomainService).restoreByIds(ids);
    }
}
