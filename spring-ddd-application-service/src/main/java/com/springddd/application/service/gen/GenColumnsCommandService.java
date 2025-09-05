package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenColumnsCommand;
import com.springddd.domain.gen.*;
import com.springddd.domain.gen.exception.I18nLocaleNullException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class GenColumnsCommandService {

    private final GenColumnsDomainRepository genColumnsDomainRepository;

    private final GenColumnsDomainFactory genColumnsDomainFactory;

    private final WipeGenColumnsByIdsDomainService wipeGenColumnsByIdsDomainService;

    private final GenColumnsBatchSaveDomainService genColumnsBatchSaveDomainService;

    public Mono<Long> create(GenColumnsCommand command) {
        Prop prop = new Prop(command.getPropColumnKey(), command.getPropColumnName(), command.getPropColumnType(), command.getPropColumnComment(), command.getPropJavaType(), command.getPropJavaEntity());
        Table table = new Table(command.getTableVisible(), command.getTableOrder(), command.getTableFilter(), command.getTableFilterComponent(), command.getTableFilterType());
        Form form = new Form(command.getFormComponent(), command.getFormVisible(), command.getFormRequired());
        I18n i18n = new I18n(command.getEn(), command.getLocale());
        GenColumnsExtendInfo extendInfo = new GenColumnsExtendInfo(command.getPropDictId(), command.getTypescriptType());
        GenColumnsDomain domain = genColumnsDomainFactory.newInstance(new InfoId(command.getInfoId()), prop, table, form, i18n, extendInfo);
        domain.create();
        return genColumnsDomainRepository.save(domain);
    }

    public Mono<Void> update(GenColumnsCommand command) {
        return genColumnsDomainRepository.load(new ColumnsId(command.getId())).flatMap(domain -> {
            Prop prop = new Prop(command.getPropColumnKey(), command.getPropColumnName(), command.getPropColumnType(), command.getPropColumnComment(), command.getPropJavaType(), command.getPropJavaEntity());
            Table table = new Table(command.getTableVisible(), command.getTableOrder(), command.getTableFilter(), command.getTableFilterComponent(), command.getTableFilterType());
            Form form = new Form(command.getFormComponent(), command.getFormVisible(), command.getFormRequired());
            I18n i18n = new I18n(command.getEn(), command.getLocale());
            GenColumnsExtendInfo extendInfo = new GenColumnsExtendInfo(command.getPropDictId(), command.getTypescriptType());
            domain.update(prop, table, form, i18n, extendInfo);
            return genColumnsDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> delete(GenColumnsCommand command) {
        return genColumnsDomainRepository.load(new ColumnsId(command.getId())).flatMap(domain -> {
            domain.delete();
            return genColumnsDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> wipe(List<Long> ids) {
        return wipeGenColumnsByIdsDomainService.wipeByIds(ids);
    }

    public Mono<Void> batchSave(List<GenColumnsCommand> commands) {
        return Flux.fromIterable(commands)
                .map(command -> {
                    Prop prop = new Prop(command.getPropColumnKey(), command.getPropColumnName(), command.getPropColumnType(), command.getPropColumnComment(), command.getPropJavaType(), command.getPropJavaEntity());
                    Table table = new Table(command.getTableVisible(), command.getTableOrder(), command.getTableFilter(), command.getTableFilterComponent(), command.getTableFilterType());
                    Form form = new Form(command.getFormComponent(), command.getFormVisible(), command.getFormRequired());
                    I18n i18n = new I18n(command.getEn(), command.getLocale());
                    GenColumnsExtendInfo extendInfo = new GenColumnsExtendInfo(command.getPropDictId(), command.getTypescriptType());
                    GenColumnsDomain domain = genColumnsDomainFactory.newInstance(new InfoId(command.getInfoId()), prop, table, form, i18n, extendInfo);
                    domain.create();
                    return domain;
                })
                .collectList()
                .flatMap(localeValidation())
                .then();
    }

    public Mono<Void> batchUpdate(List<GenColumnsCommand> commands) {
        return Flux.fromIterable(commands)
                .flatMap(command -> genColumnsDomainRepository.load(new ColumnsId(command.getId()))
                        .map(domain -> {
                            Prop prop = new Prop(command.getPropColumnKey(), command.getPropColumnName(), command.getPropColumnType(), command.getPropColumnComment(), command.getPropJavaType(), command.getPropJavaEntity());
                            Table table = new Table(command.getTableVisible(), command.getTableOrder(), command.getTableFilter(), command.getTableFilterComponent(), command.getTableFilterType());
                            Form form = new Form(command.getFormComponent(), command.getFormVisible(), command.getFormRequired());
                            I18n i18n = new I18n(command.getEn(), command.getLocale());
                            GenColumnsExtendInfo extendInfo = new GenColumnsExtendInfo(command.getPropDictId(), command.getTypescriptType());
                            domain.update(prop, table, form, i18n, extendInfo);
                            return domain;
                        })
                )
                .collectList()
                .flatMap(localeValidation())
                .then();
    }

    private Function<List<GenColumnsDomain>, Mono<? extends Void>> localeValidation() {
        return list -> {
            List<String> locales = list.stream()
                    .map(d -> d.getI18n().locale())
                    .toList();

            boolean allEmpty = locales.stream().allMatch(l -> l == null || l.isBlank());
            boolean allNonEmpty = locales.stream().allMatch(l -> l != null && !l.isBlank());

            if (allEmpty || allNonEmpty) {
                return genColumnsBatchSaveDomainService.batchSave(list);
            } else {
                return Mono.error(new I18nLocaleNullException());
            }
        };
    }
}
