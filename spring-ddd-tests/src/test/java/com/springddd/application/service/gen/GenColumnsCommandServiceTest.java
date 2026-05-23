package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenColumnsCommand;
import com.springddd.domain.gen.*;
import com.springddd.domain.gen.exception.I18nLocaleNullException;
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
class GenColumnsCommandServiceTest {

    @Mock
    private RepositoryFactory repositoryFactory;

    @Mock
    private GenColumnsDomainFactory genColumnsDomainFactory;

    @Mock
    private WipeGenColumnsByIdsDomainService wipeGenColumnsByIdsDomainService;

    @Mock
    private GenColumnsBatchSaveDomainService genColumnsBatchSaveDomainService;

    @Mock
    private GenColumnsDomainRepository genColumnsDomainRepository;

    @InjectMocks
    private GenColumnsCommandService service;

    private GenColumnsCommand sampleCommand() {
        GenColumnsCommand command = new GenColumnsCommand();
        command.setInfoId(1L);
        command.setPropColumnKey("id");
        command.setPropColumnName("ID");
        command.setPropColumnType("bigint");
        command.setPropColumnComment("主键");
        command.setPropJavaType("Long");
        command.setPropJavaEntity("id");
        command.setTableVisible(true);
        command.setTableOrder(true);
        command.setTableFilter(false);
        command.setFormComponent((byte) 1);
        command.setFormVisible(true);
        command.setFormRequired(true);
        command.setEn("Id");
        command.setLocale("主键");
        return command;
    }

    @Test
    @DisplayName("create 应创建列并返回 ID")
    void create_shouldCreateAndReturnId() {
        GenColumnsCommand command = sampleCommand();

        GenColumnsDomain domain = new GenColumnsDomain();
        domain.setI18n(new I18n("en", null));
        when(genColumnsDomainFactory.newInstance(any(InfoId.class), any(Prop.class), any(Table.class), any(Form.class), any(I18n.class), any(GenColumnsExtendInfo.class))).thenReturn(domain);
        when(repositoryFactory.getGenColumnsDomainRepository()).thenReturn(genColumnsDomainRepository);
        when(genColumnsDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.create(command))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("update 应更新列")
    void update_shouldUpdate() {
        GenColumnsCommand command = sampleCommand();
        command.setId(1L);

        GenColumnsDomain domain = new GenColumnsDomain();
        when(repositoryFactory.getGenColumnsDomainRepository()).thenReturn(genColumnsDomainRepository);
        when(genColumnsDomainRepository.load(new ColumnsId(1L))).thenReturn(Mono.just(domain));
        when(genColumnsDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.update(command))
                .verifyComplete();
    }

    @Test
    @DisplayName("delete 应软删除列")
    void delete_shouldSoftDelete() {
        GenColumnsCommand command = sampleCommand();
        command.setId(1L);

        GenColumnsDomain domain = new GenColumnsDomain();
        when(repositoryFactory.getGenColumnsDomainRepository()).thenReturn(genColumnsDomainRepository);
        when(genColumnsDomainRepository.load(new ColumnsId(1L))).thenReturn(Mono.just(domain));
        when(genColumnsDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.delete(command))
                .verifyComplete();
    }

    @Test
    @DisplayName("wipe 应调用 wipe 领域服务")
    void wipe_shouldCallDomainService() {
        List<Long> ids = List.of(1L, 2L);
        when(wipeGenColumnsByIdsDomainService.wipeByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(service.wipe(ids))
                .verifyComplete();
    }

    @Test
    @DisplayName("batchSave 当 locale 全为空时应保存成功")
    void batchSave_whenAllLocaleEmpty_shouldSave() {
        GenColumnsCommand command = sampleCommand();
        command.setLocale(null);

        GenColumnsDomain domain = new GenColumnsDomain();
        domain.setI18n(new I18n("en", null));
        when(genColumnsDomainFactory.newInstance(any(InfoId.class), any(Prop.class), any(Table.class), any(Form.class), any(I18n.class), any(GenColumnsExtendInfo.class))).thenReturn(domain);
        when(genColumnsBatchSaveDomainService.batchSave(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(service.batchSave(List.of(command)))
                .verifyComplete();
    }

    @Test
    @DisplayName("batchSave 当 locale 不一致时应报错")
    void batchSave_whenLocaleMixed_shouldError() {
        GenColumnsCommand cmd1 = sampleCommand();
        cmd1.setLocale("主键");
        GenColumnsCommand cmd2 = sampleCommand();
        cmd2.setLocale(null);

        GenColumnsDomain domain1 = new GenColumnsDomain();
        domain1.setI18n(new I18n("en", "主键"));
        GenColumnsDomain domain2 = new GenColumnsDomain();
        domain2.setI18n(new I18n("en", null));
        when(genColumnsDomainFactory.newInstance(any(InfoId.class), any(Prop.class), any(Table.class), any(Form.class), any(I18n.class), any(GenColumnsExtendInfo.class)))
                .thenReturn(domain1, domain2);

        StepVerifier.create(service.batchSave(List.of(cmd1, cmd2)))
                .expectError(I18nLocaleNullException.class)
                .verify();
    }

    @Test
    @DisplayName("batchUpdate 应更新列列表")
    void batchUpdate_shouldUpdateWithValidCommands() {
        GenColumnsCommand cmd1 = sampleCommand();
        cmd1.setId(1L);
        GenColumnsCommand cmd2 = sampleCommand();
        cmd2.setId(2L);

        GenColumnsDomain domain1 = new GenColumnsDomain();
        domain1.setI18n(new I18n("en", "主键"));
        GenColumnsDomain domain2 = new GenColumnsDomain();
        domain2.setI18n(new I18n("en", "主键"));

        when(repositoryFactory.getGenColumnsDomainRepository()).thenReturn(genColumnsDomainRepository);
        when(genColumnsDomainRepository.load(new ColumnsId(1L))).thenReturn(Mono.just(domain1));
        when(genColumnsDomainRepository.load(new ColumnsId(2L))).thenReturn(Mono.just(domain2));
        when(genColumnsBatchSaveDomainService.batchSave(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(service.batchUpdate(List.of(cmd1, cmd2)))
                .verifyComplete();
    }

    @Test
    @DisplayName("batchUpdate 当命令列表为空时应完成")
    void batchUpdate_shouldCompleteWithEmptyList() {
        when(genColumnsBatchSaveDomainService.batchSave(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(service.batchUpdate(List.of()))
                .verifyComplete();
    }

    @Test
    @DisplayName("batchUpdate 当 locale 不一致时应报错")
    void batchUpdate_whenLocaleMixed_shouldError() {
        GenColumnsCommand cmd1 = sampleCommand();
        cmd1.setId(1L);
        cmd1.setLocale("主键");
        GenColumnsCommand cmd2 = sampleCommand();
        cmd2.setId(2L);
        cmd2.setLocale(null);

        GenColumnsDomain domain1 = new GenColumnsDomain();
        domain1.setI18n(new I18n("en", "主键"));
        GenColumnsDomain domain2 = new GenColumnsDomain();
        domain2.setI18n(new I18n("en", null));

        when(repositoryFactory.getGenColumnsDomainRepository()).thenReturn(genColumnsDomainRepository);
        when(genColumnsDomainRepository.load(new ColumnsId(1L))).thenReturn(Mono.just(domain1));
        when(genColumnsDomainRepository.load(new ColumnsId(2L))).thenReturn(Mono.just(domain2));

        StepVerifier.create(service.batchUpdate(List.of(cmd1, cmd2)))
                .expectError(I18nLocaleNullException.class)
                .verify();
    }
}
