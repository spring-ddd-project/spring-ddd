package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenColumnBindCommand;
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
class GenColumnBindCommandServiceTest {

    @Mock
    private GenColumnBindDomainRepository genColumnBindDomainRepository;

    @Mock
    private GenColumnBindDomainFactory genColumnBindDomainFactory;

    @Mock
    private WipeGenColumnBindByIdsDomainService wipeGenColumnBindByIdsDomainService;

    @Mock
    private DeleteGenColumnBindDomainService deleteGenColumnBindDomainService;

    @Mock
    private RestoreGenColumnBindDomainService restoreGenColumnBindDomainService;

    @InjectMocks
    private GenColumnBindCommandService genColumnBindCommandService;

    private GenColumnBindCommand createCommand;
    private GenColumnBindDomain mockDomain;

    @BeforeEach
    void setUp() {
        createCommand = new GenColumnBindCommand();
        createCommand.setColumnType("varchar");
        createCommand.setEntityType("String");
        createCommand.setComponentType((byte) 1);
        createCommand.setTypescriptType((byte) 1);

        mockDomain = new GenColumnBindDomain();
        mockDomain.setBasicInfo(new GenColumnBindBasicInfo("varchar", "String", (byte) 1, (byte) 1));
        mockDomain.setDeleteStatus(false);
    }

    @Test
    void create_shouldCreateColumnBind() {
        when(genColumnBindDomainFactory.newInstance(any(GenColumnBindBasicInfo.class)))
                .thenReturn(mockDomain);
        when(genColumnBindDomainRepository.save(any(GenColumnBindDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(genColumnBindCommandService.create(createCommand))
                .expectNext(1L)
                .verifyComplete();

        verify(genColumnBindDomainFactory).newInstance(any(GenColumnBindBasicInfo.class));
        verify(genColumnBindDomainRepository).save(any(GenColumnBindDomain.class));
    }

    @Test
    void delete_shouldCallDeleteDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(deleteGenColumnBindDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(genColumnBindCommandService.delete(ids))
                .verifyComplete();

        verify(deleteGenColumnBindDomainService).deleteByIds(ids);
    }

    @Test
    void restore_shouldCallRestoreDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(restoreGenColumnBindDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(genColumnBindCommandService.restore(ids))
                .verifyComplete();

        verify(restoreGenColumnBindDomainService).restoreByIds(ids);
    }
}
