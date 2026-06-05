package com.springddd.application.service.dept;

import com.springddd.application.service.dept.dto.SysDeptView;
import com.springddd.domain.dept.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DeleteSysDeptByIdDomainServiceImplTest {

    @Mock
    private SysDeptDomainRepository sysDeptDomainRepository;

    @Mock
    private SysDeptQueryService sysDeptQueryService;

    private DeleteSysDeptByIdDomainServiceImpl deleteSysDeptByIdDomainService;

    @BeforeEach
    void setUp() {
        deleteSysDeptByIdDomainService = new DeleteSysDeptByIdDomainServiceImpl(
                sysDeptDomainRepository,
                sysDeptQueryService
        );
    }

    @Test
    void deleteByIds_shouldComplete_whenValidIds() {
        List<Long> ids = Arrays.asList(1L);
        SysDeptView view = new SysDeptView();
        view.setId(1L);
        view.setParentId(0L);

        SysDeptDomain mockDomain = new SysDeptDomain();
        mockDomain.setDeleteStatus(false);
        when(sysDeptQueryService.queryAllDept()).thenReturn(Mono.just(Collections.singletonList(view)));
        when(sysDeptDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(sysDeptDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(deleteSysDeptByIdDomainService.deleteByIds(ids))
                .verifyComplete();
    }
}
