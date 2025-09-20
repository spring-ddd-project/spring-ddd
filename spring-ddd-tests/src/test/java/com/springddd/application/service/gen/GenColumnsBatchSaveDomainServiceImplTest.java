package com.springddd.application.service.gen;

import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.entity.GenColumnsEntity;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenColumnsBatchSaveDomainServiceImplTest {

    @Mock
    private GenColumnsRepository genColumnsRepository;

    @InjectMocks
    private GenColumnsBatchSaveDomainServiceImpl genColumnsBatchSaveDomainService;

    private GenColumnsDomain mockDomain;

    @BeforeEach
    void setUp() {
        mockDomain = new GenColumnsDomain();
        mockDomain.setId(new ColumnsId(1L));
        mockDomain.setInfoId(new InfoId(1L));
        mockDomain.setProp(new Prop("pri", "column_name", "varchar", "comment", "String", "entity"));
        mockDomain.setTable(new Table(true, true, false, null, null));
        mockDomain.setForm(new Form((byte) 1, true, false));
        mockDomain.setI18n(new I18n("en", "locale"));
        mockDomain.setExtendInfo(new GenColumnsExtendInfo(1L, (byte) 1));
        mockDomain.setDeleteStatus(false);
    }

    @Test
    void batchSave_shouldSaveAllDomains() {
        when(genColumnsRepository.saveAll(anyList())).thenReturn(Flux.empty());

        List<GenColumnsDomain> domains = Arrays.asList(mockDomain);

        StepVerifier.create(genColumnsBatchSaveDomainService.batchSave(domains))
                .verifyComplete();

        verify(genColumnsRepository).saveAll(anyList());
    }
}
