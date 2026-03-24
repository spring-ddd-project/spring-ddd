package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenColumnsCommand;
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
class GenColumnsCommandServiceTest {

    @Mock
    private GenColumnsDomainRepository genColumnsDomainRepository;

    @Mock
    private GenColumnsDomainFactory genColumnsDomainFactory;

    @Mock
    private WipeGenColumnsByIdsDomainService wipeGenColumnsByIdsDomainService;

    @Mock
    private GenColumnsBatchSaveDomainService genColumnsBatchSaveDomainService;

    @InjectMocks
    private GenColumnsCommandService genColumnsCommandService;

    private GenColumnsCommand createCommand;
    private GenColumnsDomain mockDomain;

    @BeforeEach
    void setUp() {
        createCommand = new GenColumnsCommand();
        createCommand.setInfoId(1L);
        createCommand.setPropColumnKey("pri");
        createCommand.setPropColumnName("column_name");
        createCommand.setPropColumnType("varchar");
        createCommand.setPropColumnComment("comment");
        createCommand.setPropJavaType("String");
        createCommand.setPropJavaEntity("entity");
        createCommand.setTableVisible(true);
        createCommand.setTableOrder(true);
        createCommand.setTableFilter(false);
        createCommand.setFormComponent((byte) 1);
        createCommand.setFormVisible(true);
        createCommand.setFormRequired(false);
        createCommand.setEn("en");
        createCommand.setLocale("locale");

        mockDomain = new GenColumnsDomain();
        mockDomain.setInfoId(new InfoId(1L));
        mockDomain.setDeleteStatus(false);
    }

    @Test
    void create_shouldCreateColumn() {
        when(genColumnsDomainFactory.newInstance(any(InfoId.class), any(Prop.class), any(Table.class), any(Form.class), any(I18n.class), any(GenColumnsExtendInfo.class)))
                .thenReturn(mockDomain);
        when(genColumnsDomainRepository.save(any(GenColumnsDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(genColumnsCommandService.create(createCommand))
                .expectNext(1L)
                .verifyComplete();

        verify(genColumnsDomainFactory).newInstance(any(InfoId.class), any(Prop.class), any(Table.class), any(Form.class), any(I18n.class), any(GenColumnsExtendInfo.class));
        verify(genColumnsDomainRepository).save(any(GenColumnsDomain.class));
    }

    @Test
    void wipe_shouldCallWipeDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeGenColumnsByIdsDomainService.wipeByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(genColumnsCommandService.wipe(ids))
                .verifyComplete();

        verify(wipeGenColumnsByIdsDomainService).wipeByIds(ids);
    }
}
