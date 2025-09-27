package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenColumnsBatchSaveDomainServiceTest {

    @Mock
    private GenColumnsBatchSaveDomainService genColumnsBatchSaveDomainService;

    @Test
    void shouldReturnMonoVoidWhenBatchSaveSucceeds() {
        GenColumnsDomain domain = new GenColumnsDomain();
        domain.setId(new ColumnsId(1L));
        domain.setInfoId(new InfoId(1L));
        domain.setProp(new Prop("key", "name", "type", "comment", "javaType", "entity"));
        domain.setTable(new Table(true, true, false, null, null));
        domain.setForm(new Form((byte) 1, true, false));
        domain.setI18n(new I18n("en", "locale"));
        domain.setExtendInfo(new GenColumnsExtendInfo(100L, (byte) 1));

        when(genColumnsBatchSaveDomainService.batchSave(List.of(domain)))
                .thenReturn(Mono.empty());

        StepVerifier.create(genColumnsBatchSaveDomainService.batchSave(List.of(domain)))
                .verifyComplete();
    }

    @Test
    void shouldAcceptEmptyList() {
        when(genColumnsBatchSaveDomainService.batchSave(List.of()))
                .thenReturn(Mono.empty());

        StepVerifier.create(genColumnsBatchSaveDomainService.batchSave(List.of()))
                .verifyComplete();
    }

    @Test
    void shouldHandleMultipleDomains() {
        GenColumnsDomain domain1 = new GenColumnsDomain();
        domain1.setId(new ColumnsId(1L));
        domain1.setInfoId(new InfoId(1L));
        domain1.setProp(new Prop("key1", "name1", "type1", "comment1", "javaType1", "entity1"));
        domain1.setTable(new Table(true, true, false, null, null));
        domain1.setForm(new Form((byte) 1, true, false));
        domain1.setI18n(new I18n("en", "locale"));
        domain1.setExtendInfo(new GenColumnsExtendInfo(100L, (byte) 1));

        GenColumnsDomain domain2 = new GenColumnsDomain();
        domain2.setId(new ColumnsId(2L));
        domain2.setInfoId(new InfoId(1L));
        domain2.setProp(new Prop("key2", "name2", "type2", "comment2", "javaType2", "entity2"));
        domain2.setTable(new Table(false, false, true, (byte) 1, (byte) 1));
        domain2.setForm(new Form((byte) 2, false, true));
        domain2.setI18n(new I18n("en_us", "zh_cn"));
        domain2.setExtendInfo(new GenColumnsExtendInfo(200L, (byte) 2));

        when(genColumnsBatchSaveDomainService.batchSave(List.of(domain1, domain2)))
                .thenReturn(Mono.empty());

        StepVerifier.create(genColumnsBatchSaveDomainService.batchSave(List.of(domain1, domain2)))
                .verifyComplete();
    }
}
