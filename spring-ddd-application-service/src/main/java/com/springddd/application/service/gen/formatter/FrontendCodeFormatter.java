package com.springddd.application.service.gen.formatter;

import com.springddd.domain.gen.CodeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FrontendCodeFormatter implements CodeFormatter {

    private static final Set<String> SUPPORTED_EXTENSIONS = Set.of(".vue", ".ts", ".js", ".json");

    @Override
    public Mono<String> format(String filePath, String content) {
        if (!isFrontendFile(filePath) || content == null || content.isBlank()) {
            return Mono.just(content == null ? "" : content);
        }

        return Mono.fromCallable(() -> formatPureJava(content))
                .onErrorResume(e -> {
                    log.warn("Frontend formatting failed for {}: {}", filePath, e.getMessage());
                    return Mono.just(content);
                });
    }

    private boolean isFrontendFile(String filePath) {
        return SUPPORTED_EXTENSIONS.stream().anyMatch(filePath::endsWith);
    }

    private String formatPureJava(String content) {
        // 1. Normalize line endings to \n
        String normalized = content.replace("\r\n", "\n").replace("\r", "\n");

        // 2. Split lines and process each
        String[] lines = normalized.split("\n", -1);
        String formatted = java.util.Arrays.stream(lines)
                .map(line -> {
                    // 2a. Trim trailing whitespace
                    String trimmed = line.stripTrailing();
                    // 2b. Replace tabs with 2 spaces
                    return trimmed.replace("\t", "  ");
                })
                .collect(Collectors.joining("\n"));

        // 3. Ensure single trailing newline
        formatted = formatted.trim() + "\n";

        // 4. (Optional) Basic cleanup: Reduce more than 2 consecutive blank lines to 1
        return formatted.replaceAll("\n{3,}", "\n\n");
    }
}
