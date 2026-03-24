package com.springddd.domain.gen;

import com.springddd.application.service.gen.GenColumnsBatchSaveDomainServiceImpl;
import com.springddd.infrastructure.persistence.entity.GenColumnsEntity;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenColumnsBatchSaveDomainServiceTest {

    @Mock
    private GenColumnsRepository genColumnsRepository;

    @InjectMocks
    private GenColumnsBatchSaveDomainServiceImpl genColumnsBatchSaveDomainService;

    private GenColumnsDomain mockDomain;

    @BeforeEach
    void setUp() {
        mockDomain = new GenColumnsDomain();
        mockDomain.setId(new ColumnsId(1L));
        mockDomain.setInfoId(new InfoId(100L));

        Prop prop = new Prop("pri", "column_name", "varchar", "comment", "String", "EntityName");
        mockDomain.setProp(prop);

        Table table = new Table(true, true, false, null, null);
        mockDomain.setTable(table);

        Form form = new Form((byte) 1, true, false);
        mockDomain.setForm(form);

        I18n i18n = new I18n("en_US", "zh_CN");
        mockDomain.setI18n(i18n);

        GenColumnsExtendInfo extendInfo = new GenColumnsExtendInfo(1L, (byte) 1);
        mockDomain.setExtendInfo(extendInfo);

        mockDomain.setDeleteStatus(false);
        mockDomain.setCreateBy("admin");
        mockDomain.setCreateTime(LocalDateTime.now());
        mockDomain.setUpdateBy("admin");
        mockDomain.setUpdateTime(LocalDateTime.now());
        mockDomain.setVersion(0);
    }

    @Test
    void batchSave_shouldSaveAllDomains() {
        when(genColumnsRepository.saveAll(anyList())).thenReturn(Flux.empty());

        List<GenColumnsDomain> domains = Arrays.asList(mockDomain);

        StepVerifier.create(genColumnsBatchSaveDomainService.batchSave(domains))
                .verifyComplete();
    }

    @Test
    void batchSave_shouldHandleEmptyList() {
        when(genColumnsRepository.saveAll(anyList())).thenReturn(Flux.empty());

        List<GenColumnsDomain> domains = Collections.emptyList();

        StepVerifier.create(genColumnsBatchSaveDomainService.batchSave(domains))
                .verifyComplete();
    }

    @Test
    void batchSave_shouldSaveMultipleDomains() {
        GenColumnsDomain domain2 = new GenColumnsDomain();
        domain2.setInfoId(new InfoId(100L));
        domain2.setProp(new Prop("pri", "column_name_2", "varchar", "comment 2", "Integer", "EntityName2"));
        domain2.setTable(new Table(true, false, true, (byte) 1, (byte) 1));
        domain2.setForm(new Form((byte) 2, false, true));
        domain2.setI18n(new I18n("en_US", "zh_CN"));
        domain2.setExtendInfo(new GenColumnsExtendInfo(2L, (byte) 2));
        domain2.setDeleteStatus(false);

        when(genColumnsRepository.saveAll(anyList())).thenReturn(Flux.empty());

        List<GenColumnsDomain> domains = Arrays.asList(mockDomain, domain2);

        StepVerifier.create(genColumnsBatchSaveDomainService.batchSave(domains))
                .verifyComplete();
    }
}
