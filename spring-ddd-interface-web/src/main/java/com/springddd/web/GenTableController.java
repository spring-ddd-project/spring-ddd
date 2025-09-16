package com.springddd.web;

import com.springddd.application.service.gen.GenTableInfoCommandService;
import com.springddd.application.service.gen.GenTableInfoQueryService;
import com.springddd.application.service.gen.dto.GenTableInfoPageQuery;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/gen/table")
@RequiredArgsConstructor
public class GenTableController {

    private final GenTableInfoQueryService genTableInfoQueryService;

    private final GenTableInfoCommandService genTableInfoCommandService;

    @PostMapping("/index")
    public Mono<ApiResponse> tableIndex(@RequestBody @Validated Mono<GenTableInfoPageQuery> query) {
        return ApiResponse.validated(query, genTableInfoQueryService::index);
    }

    @DeleteMapping("/wipe")
    public Mono<ApiResponse> wipe() {
        return ApiResponse.ok(genTableInfoCommandService.wipe());
    }

    @PostMapping("/generate")
    public Mono<ResponseEntity<ByteArrayResource>> generate(
            @RequestParam("tableName") String tableName,
            @RequestParam("projectName") String projectName) {

        return genTableInfoCommandService.generate(tableName, projectName)
                .map(bytes -> {
                    ByteArrayResource resource = new ByteArrayResource(bytes);
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"generated.zip\"")
                            .contentLength(bytes.length)
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .body(resource);
                });
    }

}
