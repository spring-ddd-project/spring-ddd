package com.springddd.application.service.gen.command;

import com.springddd.application.service.gen.GenerateDomainServiceImpl;
import com.springddd.application.service.gen.dto.GenProjectInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PreviewCommand implements GenerateCommand {

    private final GenerateDomainServiceImpl generateDomainService;

    @Override
    public Mono<Void> execute(GenProjectInfoDTO dto) {
        return generateDomainService.generateProject(dto).then();
    }
}













