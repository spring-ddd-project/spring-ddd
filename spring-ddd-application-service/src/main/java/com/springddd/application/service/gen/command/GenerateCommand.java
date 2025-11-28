package com.springddd.application.service.gen.command;

import com.springddd.application.service.gen.dto.GenProjectInfoDTO;
import reactor.core.publisher.Mono;

public interface GenerateCommand {
    Mono<Void> execute(GenProjectInfoDTO dto);
}






















