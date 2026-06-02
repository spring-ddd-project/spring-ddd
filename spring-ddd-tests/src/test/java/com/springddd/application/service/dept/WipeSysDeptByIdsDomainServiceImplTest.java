package com.springddd.application.service.dept;

import com.springddd.application.service.dept.dto.SysDeptView;
import com.springddd.domain.dept.*;
import com.springddd.infrastructure.persistence.r2dbc.SysDeptRepository;
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
class WipeSysDeptByIdsDomainServiceImplTest {

    @Mock
    private SysDeptRepository sysDeptRepository;

    @Mock
    private SysDeptQueryService sysDeptQueryService;

    private WipeSysDeptByIdsDomainServiceImpl wipeSysDeptByIdsDomainService;

    @BeforeEach
    void setUp() {
        wipeSysDeptByIdsDomainService = new WipeSysDeptByIdsDomainServiceImpl(
                sysDeptRepository,
                sysDeptQueryService
        );
    }

    @Test
    void deleteByIds_shouldComplete_whenValidIds() {
        List<Long> ids = Arrays.asList(1L);
        SysDeptView view = new SysDeptView();
        view.setId(1L);
        view.setParentId(0L);

        when(sysDeptQueryService.queryAllDept()).thenReturn(Mono.just(Collections.singletonList(view)));
        when(sysDeptRepository.deleteById(any(Long.class))).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysDeptByIdsDomainService.deleteByIds(ids))
                .verifyComplete();
    }
}
