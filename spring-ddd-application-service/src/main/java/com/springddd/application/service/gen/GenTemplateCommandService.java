package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenTemplateCommand;
import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.factory.RepositoryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenTemplateCommandService {

    private final RepositoryFactory repositoryFactory;

    private final GenTemplateDomainFactory genTemplateDomainFactory;

    private final DeleteGenTemplateDomainService deleteGenTemplateDomainService;

    private final RestoreGenTemplateDomainService restoreGenTemplateDomainService;

    private final WipeGenTemplateDomainService wipeGenTemplateDomainService;

    public Mono<Long> create(GenTemplateCommand command) {
        TemplateInfo info = new TemplateInfo(command.getTemplateName(), command.getTemplateContent());
        GenTemplateDomain domain = genTemplateDomainFactory.newInstance(info);
        domain.create();
        return repositoryFactory.getGenTemplateDomainRepository().save(domain);
    }

    public Mono<Void> update(GenTemplateCommand command) {
        return repositoryFactory.getGenTemplateDomainRepository().load(new TemplateId(command.getId()))
                .flatMap(domain -> {
                    TemplateInfo info = new TemplateInfo(command.getTemplateName(), command.getTemplateContent());
                    domain.update(info);
                    return repositoryFactory.getGenTemplateDomainRepository().save(domain);
                }).then();
    }

    public Mono<Void> delete(List<Long> ids) {
        return deleteGenTemplateDomainService.deleteByIds(ids);
    }

    public Mono<Void> restore(List<Long> ids) {
        return restoreGenTemplateDomainService.restoreByIds(ids);
    }

    public Mono<Void> wipe(List<Long> ids) {
        return wipeGenTemplateDomainService.wipeByIds(ids);
    }
}

















