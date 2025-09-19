package com.springddd.application.service.gen.formatter;

import com.springddd.domain.gen.CodeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Slf4j
@Component
public class FrontendCodeFormatter implements CodeFormatter {

    private static final Set<String> SUPPORTED_EXTENSIONS = Set.of(".vue", ".ts", ".js", ".json");

    @Value("${gen.ui-project-path:}")
    private String uiProjectPath;

    @Override
    public Mono<String> format(String filePath, String content) {
        if (!isFrontendFile(filePath) || uiProjectPath.isBlank()) {
            return Mono.just(content);
        }
        return Mono.fromCallable(() -> formatWithEslint(filePath, content))
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorResume(e -> {
                    log.warn("Frontend formatting failed for {}: {}", filePath, e.getMessage());
                    return Mono.just(content);
                });
    }

    private boolean isFrontendFile(String filePath) {
        return SUPPORTED_EXTENSIONS.stream().anyMatch(filePath::endsWith);
    }

    private String formatWithEslint(String filePath, String content) throws Exception {
        String stdinFilename = filePath.endsWith(".json") ? "temp.json"
                : filePath.endsWith(".ts") ? "temp.ts"
                        : filePath.endsWith(".js") ? "temp.js"
                                : "temp.vue";

        ProcessBuilder pb = new ProcessBuilder(
                "npx", "eslint", "--fix", "--stdin", "--stdin-filename", stdinFilename);
        pb.directory(new File(uiProjectPath));
        pb.redirectErrorStream(false);

        Process process = pb.start();

        try (OutputStream os = process.getOutputStream()) {
            os.write(content.getBytes(StandardCharsets.UTF_8));
            os.flush();
        }

        String formatted;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            formatted = sb.toString();
        }

        int exitCode = process.waitFor();
        if (exitCode != 0 || formatted.isBlank()) {
            log.warn("ESLint exited with code {} for {}, using original content", exitCode, filePath);
            return content;
        }
        return formatted;
    }
}
