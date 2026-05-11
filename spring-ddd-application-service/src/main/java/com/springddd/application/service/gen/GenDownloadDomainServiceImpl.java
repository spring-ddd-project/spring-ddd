package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenProjectInfoDTO;
import com.springddd.domain.gen.GenDownloadDomainService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GenDownloadDomainServiceImpl implements GenDownloadDomainService {

    public Mono<ResponseEntity<Resource>> downloadCode(GenProjectInfoDTO dto) {
        return Mono.empty();
    }

    @Override
    public Mono<ResponseEntity<Resource>> download() {
        return Mono.empty();
    }
}
