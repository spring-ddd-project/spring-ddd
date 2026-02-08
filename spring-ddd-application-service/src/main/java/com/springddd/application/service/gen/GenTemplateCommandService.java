package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenTemplateCommand;
import com.springddd.domain.gen.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenTemplateCommandService {

    private final GenTemplateDomainRepository genTemplateDomainRepository;

    private final GenTemplateDomainFactory genTemplateDomainFactory;

    private final DeleteGenTemplateDomainService deleteGenTemplateDomainService;

    public Mono<Long> create(GenTemplateCommand command) {
        TemplateInfo info = new TemplateInfo(command.getTemplateName(), command.getTemplateContent());
        GenTemplateDomain domain = genTemplateDomainFactory.newInstance(info);
        domain.create();
        return genTemplateDomainRepository.save(domain);
    }

    public Mono<Void> update(GenTemplateCommand command) {
        return genTemplateDomainRepository.load(new TemplateId(command.getId()))
                .flatMap(domain -> {
                    TemplateInfo info = new TemplateInfo(command.getTemplateName(), command.getTemplateContent());
                    domain.update(info);
                    return genTemplateDomainRepository.save(domain);
                }).then();
    }

    public Mono<Void> delete(List<Long> ids) {
        return deleteGenTemplateDomainService.deleteByIds(ids);
    }
}
