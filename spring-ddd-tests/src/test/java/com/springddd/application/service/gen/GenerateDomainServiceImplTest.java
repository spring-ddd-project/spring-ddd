package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenAggregateView;
import com.springddd.application.service.gen.dto.ProjectTreeView;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.springddd.application.service.gen.dto.ProjectTreeView;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerateDomainServiceImplTest {

    @Mock
    private GenTableInfoQueryService genTableInfoQueryService;

    @Mock
    private GenTemplateQueryService templateQueryService;

    private Configuration configuration;

    @Mock
    private ReactiveRedisCacheHelper cacheHelper;

    private GenerateDomainServiceImpl service;

    @BeforeEach
    void setUp() {
        configuration = new Configuration(Configuration.VERSION_2_3_32);
        service = new GenerateDomainServiceImpl(genTableInfoQueryService, templateQueryService, configuration, cacheHelper);
    }

    @Test
    void generate_shouldComplete_whenTableExists() throws Exception {
        SecurityUtils.setUserId(1L);

        Map<String, Object> context = new HashMap<>();
        context.put("projectName", "test-project");
        context.put("moduleName", "sys");
        context.put("packageName", "com.example");
        context.put("className", "User");
        context.put("requestName", "user");
        context.put("aggregateViews", List.of());

        com.springddd.application.service.gen.dto.GenTemplateView template = new com.springddd.application.service.gen.dto.GenTemplateView();
        template.setTemplateName("entity");
        template.setTemplateContent("class ${className} {}");

        when(genTableInfoQueryService.buildData("sys_user")).thenReturn(Mono.just(context));
        when(templateQueryService.queryAllTemplate()).thenReturn(Mono.just(List.of(template)));
        when(cacheHelper.setCache(any(), any(), any())).thenReturn(Mono.just(true));

        StepVerifier.create(service.generate("sys_user"))
                .verifyComplete();
    }

    @Test
    void renderTemplate_shouldReturnRenderedString() {
        StepVerifier.create(service.renderTemplate("test", "Hello ${name}!", Map.of("name", "World")))
                .assertNext(result -> assertEquals("Hello World!", result))
                .verifyComplete();
    }

    @Test
    void renderTemplate_shouldError_whenTemplateInvalid() {
        StepVerifier.create(service.renderTemplate("test", "Hello <#broken>", Map.of()))
                .expectError()
                .verify();
    }

    @Test
    void generateFilePath_shouldReturnCorrectPaths() throws Exception {
        Method method = GenerateDomainServiceImpl.class.getDeclaredMethod("generateFilePath", String.class, Map.class, String.class);
        method.setAccessible(true);

        Map<String, Object> context = new HashMap<>();
        context.put("moduleName", "sys");
        context.put("packageName", "com.example");
        context.put("className", "User");
        context.put("requestName", "user");

        GenAggregateView agg1 = new GenAggregateView();
        agg1.setObjectType((byte) 1);
        agg1.setHasCreated(true);
        agg1.setObjectName("UserId");

        GenAggregateView agg2 = new GenAggregateView();
        agg2.setObjectType((byte) 2);
        agg2.setHasCreated(true);
        agg2.setObjectName("Account");

        GenAggregateView agg3 = new GenAggregateView();
        agg3.setObjectType((byte) 3);
        agg3.setHasCreated(true);
        agg3.setObjectName("UserInfo");

        context.put("aggregateViews", List.of(agg1, agg2, agg3));

        assertTrue(((String) method.invoke(service, "entity", context, "test")).contains("UserEntity.java"));
        assertTrue(((String) method.invoke(service, "r2dbc", context, "test")).contains("UserRepository.java"));
        assertTrue(((String) method.invoke(service, "domainRepositoryImpl", context, "test")).contains("UserDomainRepositoryImpl.java"));
        assertTrue(((String) method.invoke(service, "aggregateRoot", context, "test")).contains("UserId.java"));
        assertTrue(((String) method.invoke(service, "objectValue", context, "test")).contains("Account.java"));
        assertTrue(((String) method.invoke(service, "extendInfo", context, "test")).contains("UserInfo.java"));
        assertTrue(((String) method.invoke(service, "domain", context, "test")).contains("UserDomain.java"));
        assertTrue(((String) method.invoke(service, "factory", context, "test")).contains("UserDomainFactory.java"));
        assertTrue(((String) method.invoke(service, "domainRepository", context, "test")).contains("UserDomainRepository.java"));
        assertTrue(((String) method.invoke(service, "deleteDomain", context, "test")).contains("DeleteUserDomainService.java"));
        assertTrue(((String) method.invoke(service, "wipeDomain", context, "test")).contains("WipeUserDomainService.java"));
        assertTrue(((String) method.invoke(service, "restoreDomain", context, "test")).contains("RestoreUserDomainService.java"));
        assertTrue(((String) method.invoke(service, "command", context, "test")).contains("UserCommand.java"));
        assertTrue(((String) method.invoke(service, "query", context, "test")).contains("UserQuery.java"));
        assertTrue(((String) method.invoke(service, "view", context, "test")).contains("UserView.java"));
        assertTrue(((String) method.invoke(service, "mapstruct", context, "test")).contains("UserViewMapStruct.java"));
        assertTrue(((String) method.invoke(service, "pageQuery", context, "test")).contains("UserPageQuery.java"));
        assertTrue(((String) method.invoke(service, "factoryImpl", context, "test")).contains("UserDomainFactoryImpl.java"));
        assertTrue(((String) method.invoke(service, "deleteDomainImpl", context, "test")).contains("DeleteUserDomainServiceImpl.java"));
        assertTrue(((String) method.invoke(service, "wipeDomainImpl", context, "test")).contains("WipeUserDomainServiceImpl.java"));
        assertTrue(((String) method.invoke(service, "restoreDomainImpl", context, "test")).contains("RestoreUserDomainServiceImpl.java"));
        assertTrue(((String) method.invoke(service, "commandService", context, "test")).contains("UserCommandService.java"));
        assertTrue(((String) method.invoke(service, "queryService", context, "test")).contains("UserQueryService.java"));
        assertTrue(((String) method.invoke(service, "controller", context, "test")).contains("UserController.java"));
        assertTrue(((String) method.invoke(service, "index.vue", context, "test")).contains("index.vue"));
        assertTrue(((String) method.invoke(service, "recycle.vue", context, "test")).contains("recycle.vue"));
        assertTrue(((String) method.invoke(service, "form.vue", context, "test")).contains("form.vue"));
        assertTrue(((String) method.invoke(service, "api.ts", context, "test")).contains("index.ts"));
        assertTrue(((String) method.invoke(service, "i18n.en.json", context, "test")).contains(".json"));
        assertTrue(((String) method.invoke(service, "i18n.locale.json", context, "test")).contains(".json"));
        assertTrue(((String) method.invoke(service, "sql", context, "test")).contains("SQL.sql"));
        assertTrue(((String) method.invoke(service, "readme.txt", context, "test")).contains("readme.txt"));
        assertTrue(((String) method.invoke(service, "unknown", context, "test")).contains("User.txt"));
    }

    @Test
    void generate_shouldBuildTreeWithMultipleTemplates() throws Exception {
        SecurityUtils.setUserId(1L);

        Map<String, Object> context = new HashMap<>();
        context.put("projectName", "test-project");
        context.put("moduleName", "sys");
        context.put("packageName", "com.example");
        context.put("className", "User");
        context.put("requestName", "user");
        context.put("aggregateViews", List.of());

        com.springddd.application.service.gen.dto.GenTemplateView template1 = new com.springddd.application.service.gen.dto.GenTemplateView();
        template1.setTemplateName("index.vue");
        template1.setTemplateContent("<template>Vue</template>");

        com.springddd.application.service.gen.dto.GenTemplateView template2 = new com.springddd.application.service.gen.dto.GenTemplateView();
        template2.setTemplateName("sql");
        template2.setTemplateContent("SELECT 1");

        when(genTableInfoQueryService.buildData("sys_user")).thenReturn(Mono.just(context));
        when(templateQueryService.queryAllTemplate()).thenReturn(Mono.just(List.of(template1, template2)));
        when(cacheHelper.setCache(any(), any(), any())).thenReturn(Mono.just(true));

        StepVerifier.create(service.generate("sys_user"))
                .verifyComplete();
    }

    @Test
    void generate_shouldBuildTreeWithBackendAndFrontendTemplates() throws Exception {
        SecurityUtils.setUserId(1L);

        Map<String, Object> context = new HashMap<>();
        context.put("projectName", "test-project");
        context.put("moduleName", "sys");
        context.put("packageName", "com.example");
        context.put("className", "User");
        context.put("requestName", "user");

        GenAggregateView agg = new GenAggregateView();
        agg.setObjectType((byte) 1);
        agg.setHasCreated(true);
        agg.setObjectName("UserId");
        context.put("aggregateViews", List.of(agg));

        com.springddd.application.service.gen.dto.GenTemplateView template1 = new com.springddd.application.service.gen.dto.GenTemplateView();
        template1.setTemplateName("entity");
        template1.setTemplateContent("class ${className} {}");

        com.springddd.application.service.gen.dto.GenTemplateView template2 = new com.springddd.application.service.gen.dto.GenTemplateView();
        template2.setTemplateName("index.vue");
        template2.setTemplateContent("<template></template>");

        com.springddd.application.service.gen.dto.GenTemplateView template3 = new com.springddd.application.service.gen.dto.GenTemplateView();
        template3.setTemplateName("readme.txt");
        template3.setTemplateContent("readme");

        when(genTableInfoQueryService.buildData("sys_user")).thenReturn(Mono.just(context));
        when(templateQueryService.queryAllTemplate()).thenReturn(Mono.just(List.of(template1, template2, template3)));
        when(cacheHelper.setCache(any(), any(), any())).thenReturn(Mono.just(true));

        StepVerifier.create(service.generate("sys_user"))
                .verifyComplete();
    }

    @Test
    void generate_shouldBuildTreeWithAllTemplateTypes() throws Exception {
        SecurityUtils.setUserId(1L);

        Map<String, Object> context = new HashMap<>();
        context.put("projectName", "test-project");
        context.put("moduleName", "sys");
        context.put("packageName", "com.example");
        context.put("className", "User");
        context.put("requestName", "user");
        context.put("aggregateViews", List.of());

        java.util.ArrayList<com.springddd.application.service.gen.dto.GenTemplateView> templates = new java.util.ArrayList<>();

        com.springddd.application.service.gen.dto.GenTemplateView t1 = new com.springddd.application.service.gen.dto.GenTemplateView();
        t1.setTemplateName("domain");
        t1.setTemplateContent("domain ${className}");
        templates.add(t1);

        com.springddd.application.service.gen.dto.GenTemplateView t2 = new com.springddd.application.service.gen.dto.GenTemplateView();
        t2.setTemplateName("command");
        t2.setTemplateContent("command ${className}");
        templates.add(t2);

        com.springddd.application.service.gen.dto.GenTemplateView t3 = new com.springddd.application.service.gen.dto.GenTemplateView();
        t3.setTemplateName("controller");
        t3.setTemplateContent("controller ${className}");
        templates.add(t3);

        com.springddd.application.service.gen.dto.GenTemplateView t4 = new com.springddd.application.service.gen.dto.GenTemplateView();
        t4.setTemplateName("api.ts");
        t4.setTemplateContent("api ${className}");
        templates.add(t4);

        com.springddd.application.service.gen.dto.GenTemplateView t5 = new com.springddd.application.service.gen.dto.GenTemplateView();
        t5.setTemplateName("i18n.en.json");
        t5.setTemplateContent("en ${className}");
        templates.add(t5);

        com.springddd.application.service.gen.dto.GenTemplateView t6 = new com.springddd.application.service.gen.dto.GenTemplateView();
        t6.setTemplateName("i18n.locale.json");
        t6.setTemplateContent("locale ${className}");
        templates.add(t6);

        when(genTableInfoQueryService.buildData("sys_user")).thenReturn(Mono.just(context));
        when(templateQueryService.queryAllTemplate()).thenReturn(Mono.just(templates));
        when(cacheHelper.setCache(any(), any(), any())).thenReturn(Mono.just(true));

        StepVerifier.create(service.generate("sys_user"))
                .verifyComplete();
    }

    @Test
    void processOtherPaths_shouldAddTree_whenRootNotInList() throws Exception {
        java.lang.reflect.Method method = GenerateDomainServiceImpl.class.getDeclaredMethod(
                "processOtherPaths", String.class, String.class,
                com.springddd.application.service.gen.dto.ProjectTreeView.class, List.class);
        method.setAccessible(true);

        com.springddd.application.service.gen.dto.ProjectTreeView root = new com.springddd.application.service.gen.dto.ProjectTreeView();
        root.setLabel("OTHER");
        root.setChildren(new ArrayList<>());

        java.util.ArrayList<com.springddd.application.service.gen.dto.ProjectTreeView> treeList = new java.util.ArrayList<>();

        method.invoke(service, "test.txt", "content", root, treeList);

        assertEquals(1, treeList.size());
    }

    @Test
    void processOtherPaths_shouldUpdateTree_whenRootAlreadyInList() throws Exception {
        java.lang.reflect.Method method = GenerateDomainServiceImpl.class.getDeclaredMethod(
                "processOtherPaths", String.class, String.class,
                com.springddd.application.service.gen.dto.ProjectTreeView.class, List.class);
        method.setAccessible(true);

        com.springddd.application.service.gen.dto.ProjectTreeView root = new com.springddd.application.service.gen.dto.ProjectTreeView();
        root.setLabel("README");
        root.setChildren(new ArrayList<>());

        java.util.ArrayList<com.springddd.application.service.gen.dto.ProjectTreeView> treeList = new java.util.ArrayList<>();
        treeList.add(root);

        method.invoke(service, "test.txt", "content", root, treeList);

        assertEquals(1, treeList.size());
    }

    @Test
    void generateFilePath_shouldThrow_whenAggregateRootHasNoMatch() throws Exception {
        Method method = GenerateDomainServiceImpl.class.getDeclaredMethod("generateFilePath", String.class, Map.class, String.class);
        method.setAccessible(true);

        Map<String, Object> context = new HashMap<>();
        context.put("moduleName", "sys");
        context.put("packageName", "com.example");
        context.put("className", "User");
        context.put("requestName", "user");
        context.put("aggregateViews", List.of());

        java.lang.reflect.InvocationTargetException thrown = org.junit.jupiter.api.Assertions.assertThrows(
                java.lang.reflect.InvocationTargetException.class,
                () -> method.invoke(service, "aggregateRoot", context, "test"));
        assertTrue(thrown.getCause() instanceof java.util.NoSuchElementException);
    }

    @Test
    void generateFilePath_shouldThrow_whenObjectValueHasNoMatch() throws Exception {
        Method method = GenerateDomainServiceImpl.class.getDeclaredMethod("generateFilePath", String.class, Map.class, String.class);
        method.setAccessible(true);

        Map<String, Object> context = new HashMap<>();
        context.put("moduleName", "sys");
        context.put("packageName", "com.example");
        context.put("className", "User");
        context.put("requestName", "user");
        context.put("aggregateViews", List.of());

        java.lang.reflect.InvocationTargetException thrown = org.junit.jupiter.api.Assertions.assertThrows(
                java.lang.reflect.InvocationTargetException.class,
                () -> method.invoke(service, "objectValue", context, "test"));
        assertTrue(thrown.getCause() instanceof java.util.NoSuchElementException);
    }

    @Test
    void generateFilePath_shouldThrow_whenExtendInfoHasNoMatch() throws Exception {
        Method method = GenerateDomainServiceImpl.class.getDeclaredMethod("generateFilePath", String.class, Map.class, String.class);
        method.setAccessible(true);

        Map<String, Object> context = new HashMap<>();
        context.put("moduleName", "sys");
        context.put("packageName", "com.example");
        context.put("className", "User");
        context.put("requestName", "user");
        context.put("aggregateViews", List.of());

        java.lang.reflect.InvocationTargetException thrown = org.junit.jupiter.api.Assertions.assertThrows(
                java.lang.reflect.InvocationTargetException.class,
                () -> method.invoke(service, "extendInfo", context, "test"));
        assertTrue(thrown.getCause() instanceof java.util.NoSuchElementException);
    }

    @Test
    void generateFilePath_shouldReturnCorrectPath_forRecycleVue() throws Exception {
        Method method = GenerateDomainServiceImpl.class.getDeclaredMethod("generateFilePath", String.class, Map.class, String.class);
        method.setAccessible(true);

        Map<String, Object> context = new HashMap<>();
        context.put("moduleName", "sys");
        context.put("packageName", "com.example");
        context.put("className", "User");
        context.put("requestName", "user");
        context.put("aggregateViews", List.of());

        String result = (String) method.invoke(service, "recycle.vue", context, "test");
        assertTrue(result.contains("recycle.vue"));
    }

    @Test
    void generateFilePath_shouldReturnCorrectPath_forFormVue() throws Exception {
        Method method = GenerateDomainServiceImpl.class.getDeclaredMethod("generateFilePath", String.class, Map.class, String.class);
        method.setAccessible(true);

        Map<String, Object> context = new HashMap<>();
        context.put("moduleName", "sys");
        context.put("packageName", "com.example");
        context.put("className", "User");
        context.put("requestName", "user");
        context.put("aggregateViews", List.of());

        String result = (String) method.invoke(service, "form.vue", context, "test");
        assertTrue(result.contains("form.vue"));
    }

    @Test
    void generateFilePath_aggregateRoot_shouldFilterMismatchingObjectType() throws Exception {
        Method method = GenerateDomainServiceImpl.class.getDeclaredMethod("generateFilePath", String.class, Map.class, String.class);
        method.setAccessible(true);

        GenAggregateView wrongType = new GenAggregateView();
        wrongType.setObjectType((byte) 2);
        wrongType.setHasCreated(true);
        wrongType.setObjectName("Wrong");

        GenAggregateView rightType = new GenAggregateView();
        rightType.setObjectType((byte) 1);
        rightType.setHasCreated(true);
        rightType.setObjectName("UserId");

        Map<String, Object> context = new HashMap<>();
        context.put("moduleName", "sys");
        context.put("packageName", "com.example");
        context.put("className", "User");
        context.put("requestName", "user");
        context.put("aggregateViews", List.of(wrongType, rightType));

        String result = (String) method.invoke(service, "aggregateRoot", context, "test");
        assertTrue(result.contains("UserId.java"));
    }

    @Test
    void generateFilePath_objectValue_notCreated_shouldThrow() throws Exception {
        Method method = GenerateDomainServiceImpl.class.getDeclaredMethod("generateFilePath", String.class, Map.class, String.class);
        method.setAccessible(true);

        GenAggregateView notCreated = new GenAggregateView();
        notCreated.setObjectType((byte) 2);
        notCreated.setHasCreated(false);
        notCreated.setObjectName("Account");

        Map<String, Object> context = new HashMap<>();
        context.put("moduleName", "sys");
        context.put("packageName", "com.example");
        context.put("className", "User");
        context.put("requestName", "user");
        context.put("aggregateViews", List.of(notCreated));

        java.lang.reflect.InvocationTargetException thrown = org.junit.jupiter.api.Assertions.assertThrows(
                java.lang.reflect.InvocationTargetException.class,
                () -> method.invoke(service, "objectValue", context, "test"));
        assertTrue(thrown.getCause() instanceof java.util.NoSuchElementException);
    }

    @Test
    void generateFilePath_extendInfo_notCreated_shouldThrow() throws Exception {
        Method method = GenerateDomainServiceImpl.class.getDeclaredMethod("generateFilePath", String.class, Map.class, String.class);
        method.setAccessible(true);

        GenAggregateView notCreated = new GenAggregateView();
        notCreated.setObjectType((byte) 3);
        notCreated.setHasCreated(false);
        notCreated.setObjectName("UserInfo");

        Map<String, Object> context = new HashMap<>();
        context.put("moduleName", "sys");
        context.put("packageName", "com.example");
        context.put("className", "User");
        context.put("requestName", "user");
        context.put("aggregateViews", List.of(notCreated));

        java.lang.reflect.InvocationTargetException thrown = org.junit.jupiter.api.Assertions.assertThrows(
                java.lang.reflect.InvocationTargetException.class,
                () -> method.invoke(service, "extendInfo", context, "test"));
        assertTrue(thrown.getCause() instanceof java.util.NoSuchElementException);
    }

    @Test
    void generateFilePath_aggregateRoot_hasCreatedFalse_shouldThrow() throws Exception {
        Method method = GenerateDomainServiceImpl.class.getDeclaredMethod("generateFilePath", String.class, Map.class, String.class);
        method.setAccessible(true);

        GenAggregateView notCreated = new GenAggregateView();
        notCreated.setObjectType((byte) 1);
        notCreated.setHasCreated(false);
        notCreated.setObjectName("UserId");

        Map<String, Object> context = new HashMap<>();
        context.put("moduleName", "sys");
        context.put("packageName", "com.example");
        context.put("className", "User");
        context.put("requestName", "user");
        context.put("aggregateViews", List.of(notCreated));

        java.lang.reflect.InvocationTargetException thrown = assertThrows(
                java.lang.reflect.InvocationTargetException.class,
                () -> method.invoke(service, "aggregateRoot", context, "test"));
        assertTrue(thrown.getCause() instanceof java.util.NoSuchElementException);
    }

    @Test
    void saveGeneratedFile_shouldProcessFrontendPath() throws Exception {
        Method method = GenerateDomainServiceImpl.class.getDeclaredMethod(
                "saveGeneratedFile", String.class, String.class, List.class, String.class);
        method.setAccessible(true);

        List<ProjectTreeView> treeList = new ArrayList<>();
        method.invoke(service, "apps/web-ele/src/views/sys/user/index.vue", "<template></template>", treeList, "test-project");

        Optional<ProjectTreeView> uiRoot = treeList.stream()
                .filter(t -> t.getLabel().equals("test-project-ui"))
                .findFirst();
        assertTrue(uiRoot.isPresent());
        assertFalse(uiRoot.get().getChildren().isEmpty());
    }
}
