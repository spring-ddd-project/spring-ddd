package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenerateDomainService;
import com.springddd.application.service.gen.dto.GenProjectInfoDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GenerateDomainServiceImpl implements GenerateDomainService {

    @Override
    public Mono<Void> generate(String tableName) {
        return Mono.empty();
    }

    public Mono<Void> generateProject(GenProjectInfoDTO dto) {
        return Mono.empty();
    }
}
