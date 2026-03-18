package com.springddd.application.service.gen.formatter;

import com.springddd.domain.gen.CodeFormatter;
import io.spring.javaformat.formatter.Formatter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
public class JavaCodeFormatter implements CodeFormatter {

    private final Formatter formatter = new Formatter();

    @Override
    public Mono<String> format(String filePath, String content) {
        if (!filePath.endsWith(".java")) {
            return Mono.just(content);
        }
        return Mono.fromCallable(() -> {
            TextEdit edit = formatter.format(content);
            Document doc = new Document(content);
            edit.apply(doc);
            return doc.get();
        })
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorResume(e -> {
                    log.warn("Java formatting failed for {}: {}", filePath, e.getMessage());
                    return Mono.just(content);
                });
    }
}
