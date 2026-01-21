package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenInfoCommand;
import com.springddd.domain.gen.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenInfoCommandService {

    private final GenInfoDomainRepository genInfoDomainRepository;

    private final GenInfoDomainFactory genInfoDomainFactory;

    private final WipeGenInfoByIdsDomainService wipeGenInfoByIdsDomainService;

    public Mono<Long> create(GenInfoCommand command) {
        GenInfoBasicInfo basicInfo = new GenInfoBasicInfo(new TableName(command.getTableName()), new PackageName(command.getPackageName()), new ClassName(command.getClassName()), new RequestName(command.getRequestName()));
        GenInfoExtendInfo extendInfo = new GenInfoExtendInfo(command.getPropValueObject(),
                command.getPropColumnKey(),
                command.getPropColumnName(),
                command.getPropColumnType(),
                command.getPropColumnComment(),
                command.getPropJavaEntity(),
                command.getPropJavaType(),
                command.getPropDictId(),
                command.getTableVisible(),
                command.getTableOrder(),
                command.getTableFilter(),
                command.getTableFilterComponent(),
                command.getTableFilterType(),
                command.getFormComponent(),
                command.getFormVisible(),
                command.getFormRequired()
        );
        GenInfoDomain genInfoDomain = genInfoDomainFactory.newInstance(basicInfo, extendInfo);
        genInfoDomain.create(extendInfo);
        return genInfoDomainRepository.save(genInfoDomain);
    }

    public Mono<Void> update(GenInfoCommand command) {
        return genInfoDomainRepository.load(new GenInfoId(command.getId())).flatMap(domain -> {
            GenInfoBasicInfo basicInfo = new GenInfoBasicInfo(new TableName(command.getTableName()), new PackageName(command.getPackageName()), new ClassName(command.getClassName()), new RequestName(command.getRequestName()));
            GenInfoExtendInfo extendInfo = new GenInfoExtendInfo(command.getPropValueObject(),
                    command.getPropColumnKey(),
                    command.getPropColumnName(),
                    command.getPropColumnType(),
                    command.getPropColumnComment(),
                    command.getPropJavaEntity(),
                    command.getPropJavaType(),
                    command.getPropDictId(),
                    command.getTableVisible(),
                    command.getTableOrder(),
                    command.getTableFilter(),
                    command.getTableFilterComponent(),
                    command.getTableFilterType(),
                    command.getFormComponent(),
                    command.getFormVisible(),
                    command.getFormRequired());
            domain.update(basicInfo, extendInfo);
            return genInfoDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> delete(GenInfoCommand command) {
        return genInfoDomainRepository.load(new GenInfoId(command.getId())).flatMap(domain -> {
            domain.delete();
            return genInfoDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> wipeByIds(List<Long> ids) {
        return wipeGenInfoByIdsDomainService.wipeByIds(ids);
    }
}
