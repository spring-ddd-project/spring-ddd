package com.springddd.application.service.gen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.application.service.gen.dto.GenFileView;
import com.springddd.domain.gen.GenerateDomainService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
@RequiredArgsConstructor
public class GenerateDomainServiceImpl implements GenerateDomainService {

    private final GenTableInfoQueryService genTableInfoQueryService;

    private final GenTemplateQueryService templateQueryService;

    private final Configuration configuration;

    private final ObjectMapper objectMapper;

    @Override
    public Mono<byte[]> generate(String tableName, String projectName) {
        return genTableInfoQueryService.buildData(tableName)
                .flatMap(context -> templateQueryService.queryAllTemplate()
                        .flatMapMany(Flux::fromIterable)
                        .flatMap(template -> renderTemplate(template.getTemplateName(), template.getTemplateContent(), context)
                                .map(renderedContent -> {
                                    String layer;
                                    String fileDir;
                                    String fileName = template.getTemplateName() + ".java";

                                    fileDir = switch (template.getTemplateName()) {
                                        case "entity" -> {
                                            layer = projectName + "-infrastructure-persistence";
                                            yield "com.ddd.infrastructure.persistence.entity";
                                        }
                                        case "r2dbc" -> {
                                            layer = projectName + "-infrastructure-persistence";
                                            yield "com.ddd.infrastructure.persistence.r2dbc";
                                        }
                                        default -> {
                                            layer = "others";
                                            yield "others";
                                        }
                                    };

                                    return new GenFileView(layer, fileDir, fileName, renderedContent);
                                }))
                        .collectList()
                        .doOnNext(fileResults -> {
                            try {
                                System.out.println("file json = " + objectMapper.writeValueAsString(fileResults));
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .flatMap(fileResults -> {
                            try {
                                byte[] zipBytes = createZip(fileResults);
                                return Mono.just(zipBytes);
                            } catch (IOException e) {
                                return Mono.error(e);
                            }
                        })
                );
    }

    /**
     * build zip data
     */
    private byte[] createZip(List<GenFileView> files) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(b)) {
            for (GenFileView file : files) {
                // zip location, example: infrastructure-persistence/entity/Entity.java
                String zipEntryPath = file.getLayer() + "/" + file.getFileDir() + "/" + file.getFileName();
                zos.putNextEntry(new ZipEntry(zipEntryPath));
                zos.write(file.getContent().getBytes(StandardCharsets.UTF_8));
                zos.closeEntry();
            }
        }
        return b.toByteArray();
    }

    public Mono<String> renderTemplate(String templateName, String templateContent, Map<String, Object> dataModel) {
        try {
            Template template = new Template(templateName, new StringReader(templateContent), configuration);
            StringWriter out = new StringWriter();
            template.process(dataModel, out);
            return Mono.just(out.toString());
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
