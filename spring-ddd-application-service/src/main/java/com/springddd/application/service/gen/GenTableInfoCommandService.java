package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenDownloadDomainService;
import com.springddd.domain.gen.GenerateDomainService;
import com.springddd.domain.gen.WipeGenDataDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GenTableInfoCommandService {

    private final WipeGenDataDomainService wipeGenDataDomainService;

    private final GenerateDomainService generateDomainService;

    private final GenDownloadDomainService genDownloadDomainService;

    public Mono<Void> wipe() {
        return wipeGenDataDomainService.wipe();
    }

    public Mono<Void> generate(String tableName) {
        return generateDomainService.generate(tableName);
    }

    public Mono<ResponseEntity<Resource>> download() {
        return genDownloadDomainService.download();
    }
}
