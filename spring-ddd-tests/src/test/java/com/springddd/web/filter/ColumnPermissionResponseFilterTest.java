package com.springddd.web.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.application.service.permission.EntityPathResolver;
import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.auth.ReactiveSecurityUtils;
import com.springddd.domain.role.ColumnRule;
import com.springddd.domain.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ColumnPermissionResponseFilterTest {

    @Mock
    private EntityPathResolver entityPathResolver;

    @Mock
    private ServerWebExchange exchange;

    @Mock
    private ServerHttpRequest request;

    @Mock
    private WebFilterChain chain;

    @Mock
    private RequestPath requestPath;

    private ColumnPermissionResponseFilter filter;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        filter = new ColumnPermissionResponseFilter(objectMapper, entityPathResolver);
    }

    @Test
    @DisplayName("filter 当路径无法解析实体时应直接放行")
    void filter_whenNoEntityCode_shouldPassThrough() {
        when(exchange.getRequest()).thenReturn(request);
        when(request.getPath()).thenReturn(requestPath);
        when(requestPath.value()).thenReturn("/api/unknown");
        when(entityPathResolver.resolveEntityCode("/api/unknown")).thenReturn(Optional.empty());
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        verify(chain).filter(exchange);
    }

    @Test
    @DisplayName("filter 当非 GET/POST 请求时应直接放行")
    void filter_whenNotQueryMethod_shouldPassThrough() {
        when(exchange.getRequest()).thenReturn(request);
        when(request.getPath()).thenReturn(requestPath);
        when(requestPath.value()).thenReturn("/api/sys-user");
        when(request.getMethod()).thenReturn(HttpMethod.DELETE);
        when(entityPathResolver.resolveEntityCode("/api/sys-user")).thenReturn(Optional.of("sys_user"));
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        verify(chain).filter(exchange);
    }

    @Test
    @DisplayName("filterResponse 当 JSON 无 data 字段时应原样返回")
    void filterResponse_whenNoData_shouldReturnOriginal() throws Exception {
        Method method = ColumnPermissionResponseFilter.class.getDeclaredMethod("filterResponse", String.class, String.class);
        method.setAccessible(true);

        String json = "{\"code\":200}";
        Mono<String> result = (Mono<String>) method.invoke(filter, json, "sys_user");

        StepVerifier.create(result)
                .assertNext(s -> assertThat(s).isEqualTo(json))
                .verifyComplete();
    }

    @Test
    @DisplayName("filterResponse 当无可见列限制时应原样返回")
    void filterResponse_whenNoVisibleColumns_shouldReturnOriginal() throws Exception {
        Method method = ColumnPermissionResponseFilter.class.getDeclaredMethod("filterResponse", String.class, String.class);
        method.setAccessible(true);

        String json = "{\"data\":{\"id\":1,\"name\":\"test\"}}";
        Mono<String> result = (Mono<String>) method.invoke(filter, json, "sys_user");

        StepVerifier.create(result.contextWrite(withAuthUser(createUserWithEmptyColumns())))
                .assertNext(s -> assertThat(s).isEqualTo(json))
                .verifyComplete();
    }

    @Test
    @DisplayName("filterResponse 当 data 为对象时应过滤字段")
    void filterResponse_whenDataObject_shouldFilterColumns() throws Exception {
        Method method = ColumnPermissionResponseFilter.class.getDeclaredMethod("filterResponse", String.class, String.class);
        method.setAccessible(true);

        String json = "{\"data\":{\"id\":1,\"name\":\"test\",\"secret\":\"hidden\"}}";
        Mono<String> result = (Mono<String>) method.invoke(filter, json, "sys_user");

        StepVerifier.create(result.contextWrite(withAuthUser(createUserWithColumns())))
                .assertNext(s -> {
                    assertThat(s).contains("\"id\":1");
                    assertThat(s).contains("\"name\":\"test\"");
                    assertThat(s).doesNotContain("secret");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("filterResponse 当 data 为数组时应过滤每个元素")
    void filterResponse_whenDataArray_shouldFilterEachItem() throws Exception {
        Method method = ColumnPermissionResponseFilter.class.getDeclaredMethod("filterResponse", String.class, String.class);
        method.setAccessible(true);

        String json = "{\"data\":[{\"id\":1,\"name\":\"a\",\"secret\":\"x\"}]}";
        Mono<String> result = (Mono<String>) method.invoke(filter, json, "sys_user");

        StepVerifier.create(result.contextWrite(withAuthUser(createUserWithColumns())))
                .assertNext(s -> {
                    assertThat(s).contains("\"id\":1");
                    assertThat(s).doesNotContain("secret");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("filterResponse 当 data 包含 list 时应过滤列表")
    void filterResponse_whenDataList_shouldFilterList() throws Exception {
        Method method = ColumnPermissionResponseFilter.class.getDeclaredMethod("filterResponse", String.class, String.class);
        method.setAccessible(true);

        String json = "{\"data\":{\"list\":[{\"id\":1,\"name\":\"a\",\"secret\":\"x\"}],\"total\":1}}";
        Mono<String> result = (Mono<String>) method.invoke(filter, json, "sys_user");

        StepVerifier.create(result.contextWrite(withAuthUser(createUserWithColumns())))
                .assertNext(s -> {
                    assertThat(s).contains("\"id\":1");
                    assertThat(s).doesNotContain("secret");
                    assertThat(s).contains("\"total\":1");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("filterResponse 当解析异常时应返回原始 JSON")
    void filterResponse_whenParseError_shouldReturnOriginal() throws Exception {
        Method method = ColumnPermissionResponseFilter.class.getDeclaredMethod("filterResponse", String.class, String.class);
        method.setAccessible(true);

        String json = "not valid json";
        Mono<String> result = (Mono<String>) method.invoke(filter, json, "sys_user");

        StepVerifier.create(result)
                .assertNext(s -> assertThat(s).isEqualTo(json))
                .verifyComplete();
    }

    private Context withAuthUser(AuthUser user) {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        return ReactiveSecurityContextHolder.withAuthentication(auth);
    }

    private AuthUser createUserWithEmptyColumns() {
        AuthUser user = new AuthUser();
        user.setUserId(new UserId(1L));
        user.setUsername("test");
        user.setRoles(List.of("ADMIN"));
        user.setPermissions(List.of("sys:user:index"));
        user.setMenuIds(List.of(1L));
        user.setColumnPermissions(new HashMap<>());
        return user;
    }

    private AuthUser createUserWithColumns() {
        AuthUser user = new AuthUser();
        user.setUserId(new UserId(1L));
        user.setUsername("test");
        user.setRoles(List.of("ADMIN"));
        user.setPermissions(List.of("sys:user:index"));
        user.setMenuIds(List.of(1L));
        Map<String, Set<String>> perms = new HashMap<>();
        perms.put("sys_user", Set.of("id", "name"));
        user.setColumnPermissions(perms);
        return user;
    }

    @Test
    @DisplayName("filterResponse 当 getVisibleColumns 返回 empty 时应返回原始 JSON")
    void filterResponse_whenGetVisibleColumnsReturnsEmpty_shouldReturnOriginal() throws Exception {
        Method method = ColumnPermissionResponseFilter.class.getDeclaredMethod("filterResponse", String.class, String.class);
        method.setAccessible(true);

        String json = "{\"data\":{\"id\":1,\"name\":\"test\"}}";

        try (MockedStatic<ReactiveSecurityUtils> mocked = mockStatic(ReactiveSecurityUtils.class)) {
            mocked.when(() -> ReactiveSecurityUtils.getVisibleColumns("sys_user"))
                    .thenReturn(Mono.empty());

            Mono<String> result = (Mono<String>) method.invoke(filter, json, "sys_user");
            StepVerifier.create(result)
                    .assertNext(s -> assertThat(s).isEqualTo(json))
                    .verifyComplete();
        }
    }

    @Test
    @DisplayName("filterResponse 当 data 同时包含 list 和 items 时应使用 list 过滤并同时设置两者")
    void filterResponse_whenDataHasBothListAndItems_shouldFilterUsingList() throws Exception {
        Method method = ColumnPermissionResponseFilter.class.getDeclaredMethod("filterResponse", String.class, String.class);
        method.setAccessible(true);

        String json = "{\"data\":{\"list\":[{\"id\":1,\"name\":\"a\",\"secret\":\"x\"}],\"items\":[{\"id\":2,\"name\":\"b\",\"secret\":\"y\"}],\"total\":2}}";
        Mono<String> result = (Mono<String>) method.invoke(filter, json, "sys_user");

        StepVerifier.create(result.contextWrite(withAuthUser(createUserWithColumns())))
                .assertNext(s -> {
                    assertThat(s).contains("\"id\":1");
                    assertThat(s).doesNotContain("secret");
                    assertThat(s).contains("\"total\":2");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("filterResponse 当 writeValueAsString 抛出异常时应返回原始 JSON")
    void filterResponse_whenWriteValueAsStringThrows_shouldReturnOriginal() throws Exception {
        ObjectMapper spyMapper = spy(new ObjectMapper());
        doThrow(new RuntimeException("write fail")).when(spyMapper).writeValueAsString(any(JsonNode.class));
        ColumnPermissionResponseFilter filterWithSpy = new ColumnPermissionResponseFilter(spyMapper, entityPathResolver);

        Method method = ColumnPermissionResponseFilter.class.getDeclaredMethod("filterResponse", String.class, String.class);
        method.setAccessible(true);

        String json = "{\"data\":{\"id\":1,\"name\":\"test\",\"secret\":\"hidden\"}}";
        Mono<String> result = (Mono<String>) method.invoke(filterWithSpy, json, "sys_user");

        StepVerifier.create(result.contextWrite(withAuthUser(createUserWithColumns())))
                .assertNext(s -> assertThat(s).isEqualTo(json))
                .verifyComplete();
    }

    @Test
    @DisplayName("filter 当 GET 请求解析到 entityCode 时应装饰响应并过滤 JSON")
    void filter_whenGetRequestWithEntityCode_shouldDecorateAndFilterResponse() {
        MockServerHttpRequest request = MockServerHttpRequest.method(HttpMethod.GET, "/api/sys-user").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(entityPathResolver.resolveEntityCode("/api/sys-user")).thenReturn(Optional.of("sys_user"));
        when(chain.filter(any(ServerWebExchange.class))).thenAnswer(invocation -> {
            ServerWebExchange mutated = invocation.getArgument(0);
            ServerHttpResponse decoratedResponse = mutated.getResponse();
            decoratedResponse.setStatusCode(org.springframework.http.HttpStatus.OK);

            DataBuffer buffer = decoratedResponse.bufferFactory().wrap(
                    "{\"data\":{\"id\":1,\"name\":\"test\",\"secret\":\"hidden\"}}".getBytes(StandardCharsets.UTF_8));

            return decoratedResponse.writeWith(Flux.just(buffer));
        });

        try (MockedStatic<ReactiveSecurityUtils> mocked = mockStatic(ReactiveSecurityUtils.class)) {
            mocked.when(() -> ReactiveSecurityUtils.getVisibleColumns("sys_user"))
                    .thenReturn(Mono.just(Set.of("id", "name")));

            StepVerifier.create(filter.filter(exchange, chain))
                    .verifyComplete();
        }

        StepVerifier.create(exchange.getResponse().getBodyAsString())
                .assertNext(body -> {
                    assertThat(body).contains("\"id\":1");
                    assertThat(body).contains("\"name\":\"test\"");
                    assertThat(body).doesNotContain("secret");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("filter 当 POST 请求解析到 entityCode 时应装饰响应并过滤 JSON 数组")
    void filter_whenPostRequestWithEntityCode_shouldDecorateAndFilterArrayResponse() {
        MockServerHttpRequest request = MockServerHttpRequest.method(HttpMethod.POST, "/api/sys-user").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(entityPathResolver.resolveEntityCode("/api/sys-user")).thenReturn(Optional.of("sys_user"));
        when(chain.filter(any(ServerWebExchange.class))).thenAnswer(invocation -> {
            ServerWebExchange mutated = invocation.getArgument(0);
            ServerHttpResponse decoratedResponse = mutated.getResponse();
            decoratedResponse.setStatusCode(org.springframework.http.HttpStatus.OK);

            DataBuffer buffer = decoratedResponse.bufferFactory().wrap(
                    "{\"data\":[{\"id\":1,\"name\":\"a\",\"secret\":\"x\"}]}".getBytes(StandardCharsets.UTF_8));

            return decoratedResponse.writeWith(Flux.just(buffer));
        });

        try (MockedStatic<ReactiveSecurityUtils> mocked = mockStatic(ReactiveSecurityUtils.class)) {
            mocked.when(() -> ReactiveSecurityUtils.getVisibleColumns("sys_user"))
                    .thenReturn(Mono.just(Set.of("id", "name")));

            StepVerifier.create(filter.filter(exchange, chain))
                    .verifyComplete();
        }

        StepVerifier.create(exchange.getResponse().getBodyAsString())
                .assertNext(body -> {
                    assertThat(body).contains("\"id\":1");
                    assertThat(body).doesNotContain("secret");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("filter 当响应状态非 OK 时应直接透传不过滤")
    void filter_whenNonOkStatus_shouldPassThroughWithoutFiltering() {
        MockServerHttpRequest request = MockServerHttpRequest.method(HttpMethod.GET, "/api/sys-user").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(entityPathResolver.resolveEntityCode("/api/sys-user")).thenReturn(Optional.of("sys_user"));
        when(chain.filter(any(ServerWebExchange.class))).thenAnswer(invocation -> {
            ServerWebExchange mutated = invocation.getArgument(0);
            ServerHttpResponse decoratedResponse = mutated.getResponse();
            decoratedResponse.setStatusCode(org.springframework.http.HttpStatus.BAD_REQUEST);

            DataBuffer buffer = decoratedResponse.bufferFactory().wrap(
                    "{\"error\":\"bad request\"}".getBytes(StandardCharsets.UTF_8));

            return decoratedResponse.writeWith(Flux.just(buffer));
        });

        StepVerifier.create(filter.filter(exchange, chain)
                        .contextWrite(withAuthUser(createUserWithColumns())))
                .verifyComplete();

        StepVerifier.create(exchange.getResponse().getBodyAsString())
                .assertNext(body -> assertThat(body).isEqualTo("{\"error\":\"bad request\"}"))
                .verifyComplete();
    }

    @Test
    @DisplayName("filter 当 body 不是 Flux 时应直接透传")
    void filter_whenNonFluxBody_shouldPassThrough() {
        MockServerHttpRequest request = MockServerHttpRequest.method(HttpMethod.GET, "/api/sys-user").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(entityPathResolver.resolveEntityCode("/api/sys-user")).thenReturn(Optional.of("sys_user"));
        when(chain.filter(any(ServerWebExchange.class))).thenAnswer(invocation -> {
            ServerWebExchange mutated = invocation.getArgument(0);
            ServerHttpResponse decoratedResponse = mutated.getResponse();

            DataBuffer buffer = decoratedResponse.bufferFactory().wrap(
                    "{\"data\":{\"id\":1}}".getBytes(StandardCharsets.UTF_8));

            return decoratedResponse.writeWith(Mono.just(buffer));
        });

        StepVerifier.create(filter.filter(exchange, chain)
                        .contextWrite(withAuthUser(createUserWithColumns())))
                .verifyComplete();

        StepVerifier.create(exchange.getResponse().getBodyAsString())
                .assertNext(body -> assertThat(body).isEqualTo("{\"data\":{\"id\":1}}"))
                .verifyComplete();
    }

    @Test
    @DisplayName("filter 当响应状态为 null 时应直接透传")
    void filter_whenNullStatus_shouldPassThrough() {
        MockServerHttpRequest request = MockServerHttpRequest.method(HttpMethod.GET, "/api/sys-user").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(entityPathResolver.resolveEntityCode("/api/sys-user")).thenReturn(Optional.of("sys_user"));
        when(chain.filter(any(ServerWebExchange.class))).thenAnswer(invocation -> {
            ServerWebExchange mutated = invocation.getArgument(0);
            ServerHttpResponse decoratedResponse = mutated.getResponse();
            // Do not set status code; leave it as null

            DataBuffer buffer = decoratedResponse.bufferFactory().wrap(
                    "{\"data\":{\"id\":1}}".getBytes(StandardCharsets.UTF_8));

            return decoratedResponse.writeWith(Flux.just(buffer));
        });

        StepVerifier.create(filter.filter(exchange, chain)
                        .contextWrite(withAuthUser(createUserWithColumns())))
                .verifyComplete();

        StepVerifier.create(exchange.getResponse().getBodyAsString())
                .assertNext(body -> assertThat(body).isEqualTo("{\"data\":{\"id\":1}}"))
                .verifyComplete();
    }

    @Test
    @DisplayName("filterNode 当 node 为 null 时应返回 null")
    void filterNode_whenNull_shouldReturnNull() throws Exception {
        Method method = ColumnPermissionResponseFilter.class.getDeclaredMethod("filterNode", JsonNode.class, Set.class);
        method.setAccessible(true);
        JsonNode result = (JsonNode) method.invoke(filter, (JsonNode) null, Set.of("id"));
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("filterNode 当 node 为数组时应返回原数组")
    void filterNode_whenArray_shouldReturnOriginal() throws Exception {
        Method method = ColumnPermissionResponseFilter.class.getDeclaredMethod("filterNode", JsonNode.class, Set.class);
        method.setAccessible(true);
        JsonNode arrayNode = objectMapper.readTree("[{\"id\":1,\"name\":\"a\"}]");
        JsonNode result = (JsonNode) method.invoke(filter, arrayNode, Set.of("id"));
        assertThat(result).isEqualTo(arrayNode);
    }

    @Test
    @DisplayName("filterNode 当 node 为嵌套对象时应保留嵌套结构")
    void filterNode_whenNestedObject_shouldPreserveNestedStructure() throws Exception {
        Method method = ColumnPermissionResponseFilter.class.getDeclaredMethod("filterNode", JsonNode.class, Set.class);
        method.setAccessible(true);
        JsonNode node = objectMapper.readTree("{\"id\":1,\"name\":\"test\",\"nested\":{\"secret\":\"hidden\"}}");
        JsonNode result = (JsonNode) method.invoke(filter, node, Set.of("id", "nested"));
        assertThat(result.get("id").asInt()).isEqualTo(1);
        assertThat(result.get("nested").get("secret").asText()).isEqualTo("hidden");
        assertThat(result.has("name")).isFalse();
    }

    @Test
    @DisplayName("filterResponse 当 data 包含 items 数组时应过滤 items")
    void filterResponse_whenDataHasItems_shouldFilterItems() throws Exception {
        Method method = ColumnPermissionResponseFilter.class.getDeclaredMethod("filterResponse", String.class, String.class);
        method.setAccessible(true);
        String json = "{\"data\":{\"items\":[{\"id\":1,\"name\":\"a\",\"secret\":\"x\"}],\"total\":1}}";
        Mono<String> result = (Mono<String>) method.invoke(filter, json, "sys_user");
        StepVerifier.create(result.contextWrite(withAuthUser(createUserWithColumns())))
                .assertNext(s -> {
                    assertThat(s).contains("\"id\":1");
                    assertThat(s).doesNotContain("secret");
                    assertThat(s).contains("\"total\":1");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("filterResponse 当 data.list 不是数组时应过滤 dataNode")
    void filterResponse_whenDataListNotArray_shouldFilterDataNode() throws Exception {
        Method method = ColumnPermissionResponseFilter.class.getDeclaredMethod("filterResponse", String.class, String.class);
        method.setAccessible(true);
        String json = "{\"data\":{\"list\":\"not-array\",\"id\":1,\"name\":\"test\",\"secret\":\"hidden\"}}";
        Mono<String> result = (Mono<String>) method.invoke(filter, json, "sys_user");
        StepVerifier.create(result.contextWrite(withAuthUser(createUserWithColumns())))
                .assertNext(s -> {
                    assertThat(s).contains("\"id\":1");
                    assertThat(s).contains("\"name\":\"test\"");
                    assertThat(s).doesNotContain("secret");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("filterResponse 当单对象包含嵌套字段时应保留嵌套字段")
    void filterResponse_whenSingleObjectWithNestedFields_shouldPreserveNested() throws Exception {
        Method method = ColumnPermissionResponseFilter.class.getDeclaredMethod("filterResponse", String.class, String.class);
        method.setAccessible(true);
        String json = "{\"data\":{\"id\":1,\"name\":\"test\",\"nested\":{\"secret\":\"hidden\"}}}";
        Mono<String> result = (Mono<String>) method.invoke(filter, json, "sys_user");

        AuthUser user = new AuthUser();
        user.setUserId(new UserId(1L));
        user.setUsername("test");
        user.setRoles(List.of("ADMIN"));
        user.setPermissions(List.of("sys:user:index"));
        user.setMenuIds(List.of(1L));
        Map<String, Set<String>> perms = new HashMap<>();
        perms.put("sys_user", new HashSet<>(Set.of("id", "name", "nested")));
        user.setColumnPermissions(perms);

        StepVerifier.create(result.contextWrite(withAuthUser(user)))
                .assertNext(s -> {
                    assertThat(s).contains("\"id\":1");
                    assertThat(s).contains("\"name\":\"test\"");
                    assertThat(s).contains("\"nested\":{\"secret\":\"hidden\"}");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("filter 当状态 OK 但 body 不是 Flux 时应直接透传")
    void filter_whenOkStatusAndNonFluxBody_shouldPassThrough() {
        MockServerHttpRequest request = MockServerHttpRequest.method(HttpMethod.GET, "/api/sys-user").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(entityPathResolver.resolveEntityCode("/api/sys-user")).thenReturn(Optional.of("sys_user"));
        when(chain.filter(any(ServerWebExchange.class))).thenAnswer(invocation -> {
            ServerWebExchange mutated = invocation.getArgument(0);
            ServerHttpResponse decoratedResponse = mutated.getResponse();
            decoratedResponse.setStatusCode(org.springframework.http.HttpStatus.OK);

            DataBuffer buffer = decoratedResponse.bufferFactory().wrap(
                    "{\"data\":{\"id\":1,\"name\":\"test\",\"secret\":\"hidden\"}}".getBytes(StandardCharsets.UTF_8));

            return decoratedResponse.writeWith(Mono.just(buffer));
        });

        try (MockedStatic<ReactiveSecurityUtils> mocked = mockStatic(ReactiveSecurityUtils.class)) {
            mocked.when(() -> ReactiveSecurityUtils.getVisibleColumns("sys_user"))
                    .thenReturn(Mono.just(Set.of("id", "name")));

            StepVerifier.create(filter.filter(exchange, chain))
                    .verifyComplete();
        }

        StepVerifier.create(exchange.getResponse().getBodyAsString())
                .assertNext(body -> assertThat(body).isEqualTo("{\"data\":{\"id\":1,\"name\":\"test\",\"secret\":\"hidden\"}}"))
                .verifyComplete();
    }

    @Test
    @DisplayName("filterResponse 当 JSON root 为数组时应原样返回")
    void filterResponse_whenRootIsArray_shouldReturnOriginal() throws Exception {
        Method method = ColumnPermissionResponseFilter.class.getDeclaredMethod("filterResponse", String.class, String.class);
        method.setAccessible(true);

        String json = "[{\"id\":1}]";
        Mono<String> result = (Mono<String>) method.invoke(filter, json, "sys_user");

        StepVerifier.create(result)
                .assertNext(s -> assertThat(s).isEqualTo(json))
                .verifyComplete();
    }
}
