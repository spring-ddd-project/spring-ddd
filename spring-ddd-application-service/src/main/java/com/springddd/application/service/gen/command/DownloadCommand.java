package com.springddd.application.service.gen.command;

import com.springddd.application.service.gen.GenDownloadDomainServiceImpl;
import com.springddd.application.service.gen.dto.GenProjectInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class DownloadCommand implements GenerateCommand {

    private final GenDownloadDomainServiceImpl genDownloadDomainService;

    @Override
    public Mono<Void> execute(GenProjectInfoDTO dto) {
        return genDownloadDomainService.downloadCode(dto).then();
    }
}


















