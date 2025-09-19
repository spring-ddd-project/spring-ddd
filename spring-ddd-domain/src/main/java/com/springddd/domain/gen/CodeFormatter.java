package com.springddd.domain.gen;

import reactor.core.publisher.Mono;

public interface CodeFormatter {

    Mono<String> format(String filePath, String content);
}
