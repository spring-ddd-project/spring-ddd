package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenColumnsCommand;
import com.springddd.domain.gen.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenColumnsCommandService {

    private final GenColumnsDomainRepository genColumnsDomainRepository;

    private final GenColumnsDomainFactory genColumnsDomainFactory;

    private final WipeGenColumnsByIdsDomainService wipeGenColumnsByIdsDomainService;

    private final GenColumnsBatchSaveDomainService genColumnsBatchSaveDomainService;

    public Mono<Long> create(GenColumnsCommand command) {
        GenColumnsBasicInfo basicInfo = new GenColumnsBasicInfo(command.getPropColumnKey(), command.getPropColumnName(), command.getPropColumnType(), command.getPropColumnComment(), command.getPropJavaType(), command.getPropJavaEntity());
        GenColumnsExtendInfo extendInfo = new GenColumnsExtendInfo(command.getPropDictId(), command.getTableVisible(), command.getTableOrder(), command.getTableFilter(), command.getTableFilterComponent(), command.getTableFilterType(), command.getTypescriptType(), command.getFormComponent(), command.getFormVisible(), command.getFormRequired());
        GenColumnsDomain domain = genColumnsDomainFactory.newInstance(new GenProjectInfoId(command.getInfoId()), basicInfo, extendInfo);
        domain.create();
        return genColumnsDomainRepository.save(domain);
    }

    public Mono<Void> update(GenColumnsCommand command) {
        return genColumnsDomainRepository.load(new GenColumnsId(command.getId())).flatMap(domain -> {
            GenColumnsBasicInfo basicInfo = new GenColumnsBasicInfo(command.getPropColumnKey(), command.getPropColumnName(), command.getPropColumnType(), command.getPropColumnComment(), command.getPropJavaType(), command.getPropJavaEntity());
            GenColumnsExtendInfo extendInfo = new GenColumnsExtendInfo(command.getPropDictId(), command.getTableVisible(), command.getTableOrder(), command.getTableFilter(), command.getTableFilterComponent(), command.getTableFilterType(), command.getTypescriptType(), command.getFormComponent(), command.getFormVisible(), command.getFormRequired());
            domain.update(basicInfo, extendInfo);
            return genColumnsDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> delete(GenColumnsCommand command) {
        return genColumnsDomainRepository.load(new GenColumnsId(command.getId())).flatMap(domain -> {
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
                    GenColumnsBasicInfo basicInfo = new GenColumnsBasicInfo(command.getPropColumnKey(), command.getPropColumnName(), command.getPropColumnType(), command.getPropColumnComment(), command.getPropJavaType(), command.getPropJavaEntity());
                    GenColumnsExtendInfo extendInfo = new GenColumnsExtendInfo(command.getPropDictId(), command.getTableVisible(), command.getTableOrder(), command.getTableFilter(), command.getTableFilterComponent(), command.getTableFilterType(), command.getTypescriptType(), command.getFormComponent(), command.getFormVisible(), command.getFormRequired());
                    GenColumnsDomain domain = genColumnsDomainFactory.newInstance(new GenProjectInfoId(command.getInfoId()), basicInfo, extendInfo);
                    domain.create();
                    return domain;
                })
                .collectList()
                .flatMap(genColumnsBatchSaveDomainService::batchSave)
                .then();
    }

    public Mono<Void> batchUpdate(List<GenColumnsCommand> commands) {
        return Flux.fromIterable(commands)
                .flatMap(command -> genColumnsDomainRepository.load(new GenColumnsId(command.getId()))
                        .map(domain -> {
                            GenColumnsBasicInfo basicInfo = new GenColumnsBasicInfo(command.getPropColumnKey(), command.getPropColumnName(), command.getPropColumnType(), command.getPropColumnComment(), command.getPropJavaType(), command.getPropJavaEntity());
                            GenColumnsExtendInfo extendInfo = new GenColumnsExtendInfo(command.getPropDictId(), command.getTableVisible(), command.getTableOrder(), command.getTableFilter(), command.getTableFilterComponent(), command.getTableFilterType(), command.getTypescriptType(), command.getFormComponent(), command.getFormVisible(), command.getFormRequired());
                            domain.update(basicInfo, extendInfo);
                            return domain;
                        })
                )
                .collectList()
                .flatMap(genColumnsBatchSaveDomainService::batchSave)
                .then();
    }
}
