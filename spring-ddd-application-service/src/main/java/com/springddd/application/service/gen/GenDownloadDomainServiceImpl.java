package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenProjectInfoDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GenDownloadDomainServiceImpl {

    public Mono<ResponseEntity<Resource>> downloadCode(GenProjectInfoDTO dto) {
        return Mono.empty();
    }
}
