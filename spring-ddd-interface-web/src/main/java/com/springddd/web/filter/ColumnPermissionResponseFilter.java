package com.springddd.web.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.springddd.application.service.permission.EntityPathResolver;
import com.springddd.domain.auth.ReactiveSecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Set;

@Slf4j
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 100)
@RequiredArgsConstructor
public class ColumnPermissionResponseFilter implements WebFilter {

    private final ObjectMapper objectMapper;
    private final EntityPathResolver entityPathResolver;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        String entityCode = resolveEntityCode(path);
        if (entityCode == null || !isQueryMethod(exchange)) {
            return chain.filter(exchange);
        }

        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();

        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(org.reactivestreams.Publisher<? extends DataBuffer> body) {
                HttpStatus statusCode = (HttpStatus) getStatusCode();
                if (statusCode == null || statusCode != HttpStatus.OK) {
                    return super.writeWith(body);
                }

                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.collectList()
                            .flatMap(dataBuffers -> {
                                DataBuffer joined = bufferFactory.join(dataBuffers);
                                byte[] content = new byte[joined.readableByteCount()];
                                joined.read(content);
                                DataBufferUtils.release(joined);
                                String responseBody = new String(content, StandardCharsets.UTF_8);
                                return filterResponse(responseBody, entityCode)
                                        .map(filtered -> bufferFactory.wrap(filtered.getBytes(StandardCharsets.UTF_8)));
                            }));
                }
                return super.writeWith(body);
            }
        };

        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    private boolean isQueryMethod(ServerWebExchange exchange) {
        String method = exchange.getRequest().getMethod().name();
        return "POST".equals(method) || "GET".equals(method);
    }

    private String resolveEntityCode(String path) {
        return entityPathResolver.resolveEntityCode(path).orElse(null);
    }

    private Mono<String> filterResponse(String json, String entityCode) {
        try {
            JsonNode root = objectMapper.readTree(json);
            if (!root.isObject() || !root.has("data")) {
                return Mono.just(json);
            }
            return ReactiveSecurityUtils.getVisibleColumns(entityCode)
                    .flatMap(visibleColumns -> {
                        if (visibleColumns.isEmpty()) {
                            return Mono.just(json);
                        }
                        try {
                            JsonNode dataNode = root.get("data");
                            if (dataNode.isArray()) {
                                ArrayNode filtered = objectMapper.createArrayNode();
                                for (JsonNode item : dataNode) {
                                    filtered.add(filterNode(item, visibleColumns));
                                }
                                ((ObjectNode) root).set("data", filtered);
                            } else if (dataNode.has("list") || dataNode.has("items")) {
                                JsonNode listNode = dataNode.has("list") ? dataNode.get("list") : dataNode.get("items");
                                if (listNode.isArray()) {
                                    ArrayNode filtered = objectMapper.createArrayNode();
                                    for (JsonNode item : listNode) {
                                        filtered.add(filterNode(item, visibleColumns));
                                    }
                                    ObjectNode dataObj = (ObjectNode) dataNode;
                                    dataObj.set("list", filtered);
                                    dataObj.set("items", filtered);
                                    ((ObjectNode) root).set("data", dataObj);
                                } else {
                                    ((ObjectNode) root).set("data", filterNode(dataNode, visibleColumns));
                                }
                            } else {
                                ((ObjectNode) root).set("data", filterNode(dataNode, visibleColumns));
                            }
                            return Mono.just(objectMapper.writeValueAsString(root));
                        } catch (Exception e) {
                            log.warn("Column permission filter error for {}: {}", entityCode, e.getMessage());
                            return Mono.just(json);
                        }
                    })
                    .switchIfEmpty(Mono.just(json));
        } catch (Exception e) {
            log.warn("Failed to parse response for column permission filtering: {}", e.getMessage());
            return Mono.just(json);
        }
    }

    private JsonNode filterNode(JsonNode node, Set<String> visibleColumns) {
        if (node == null || !node.isObject()) {
            return node;
        }
        ObjectNode result = objectMapper.createObjectNode();
        node.fields().forEachRemaining(entry -> {
            if (visibleColumns.contains(entry.getKey())) {
                result.set(entry.getKey(), entry.getValue());
            }
        });
        return result;
    }
}
