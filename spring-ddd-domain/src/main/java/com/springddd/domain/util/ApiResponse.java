package com.springddd.domain.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

@Data
@NoArgsConstructor
public class ApiResponse {

    private Integer code;
    private String message;
    private Object data;

    public ApiResponse(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ApiResponse success(Object data) {
        return new ApiResponse(0, "Success", data);
    }

    public static ApiResponse empty() {
        return new ApiResponse(0, "Success", null);
    }

    public static ApiResponse error(Integer code, String message) {
        return new ApiResponse(code, message, null);
    }

    public static ApiResponse error(String message) {
        return new ApiResponse(500, message, null);
    }

    // Reactive return for regular and paginated requests
    public static Mono<ApiResponse> ok(Mono<?> mono) {
        return mono.map(data -> {
            if (data instanceof PageResponse<?> page) {
                return page(page.getItems(), page.getTotal(), page.getPageNum(), page.getPageSize());
            }
            return success(data);
        }).defaultIfEmpty(empty());
    }

    // Reactive return with Validated parameter support for regular and paginated requests
    public static <T, R> Mono<ApiResponse> ok(Mono<T> paramMono, Function<T, Mono<R>> handler) {
        return paramMono
                .flatMap(handler)
                .map(data -> {
                    if (data instanceof PageResponse<?> page) {
                        return page(page.getItems(), page.getTotal(), page.getPageNum(), page.getPageSize());
                    }
                    return success(data);
                })
                .defaultIfEmpty(empty());
    }

    public static Mono<ApiResponse> ok(Flux<?> flux) {
        return flux.collectList()
                .map(ApiResponse::success)
                .defaultIfEmpty(ApiResponse.empty());
    }

    public static <T> ApiResponse page(List<T> records, long total, int pageNum, int pageSize) {
        return success(new PageResponse<>(records, total, pageNum, pageSize));
    }

}

