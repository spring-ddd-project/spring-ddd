package com.springddd.application.service.gen.strategy;

import com.springddd.application.service.gen.dto.GenAggregateView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DomainFilePathStrategyTest {

    private final DomainFilePathStrategy strategy = new DomainFilePathStrategy();
    private final String projectName = "spring-ddd";
    private final Map<String, Object> context = Map.of(
            "moduleName", "user",
            "packageName", "com.springddd",
            "className", "SysUser",
            "aggregateViews", List.of()
    );

    @Test
    @DisplayName("supports should return true for known templates")
    void supports_knownTemplates_shouldReturnTrue() {
        assertThat(strategy.supports("aggregateRoot")).isTrue();
        assertThat(strategy.supports("objectValue")).isTrue();
        assertThat(strategy.supports("extendInfo")).isTrue();
        assertThat(strategy.supports("domain")).isTrue();
        assertThat(strategy.supports("factory")).isTrue();
        assertThat(strategy.supports("domainRepository")).isTrue();
        assertThat(strategy.supports("deleteDomain")).isTrue();
        assertThat(strategy.supports("wipeDomain")).isTrue();
        assertThat(strategy.supports("restoreDomain")).isTrue();
    }

    @Test
    @DisplayName("supports should return false for unknown templates")
    void supports_unknownTemplates_shouldReturnFalse() {
        assertThat(strategy.supports("unknown")).isFalse();
        assertThat(strategy.supports("controller")).isFalse();
        assertThat(strategy.supports("entity")).isFalse();
    }

    @Test
    @DisplayName("generatePath should return correct path for domain template")
    void generatePath_domain_shouldReturnCorrectPath() {
        String path = strategy.generatePath("domain", context, projectName);
        assertThat(path).isEqualTo("spring-ddd-domain/src/main/java/com/springddd/domain/user/SysUserDomain.java");
    }

    @Test
    @DisplayName("generatePath should return correct path for factory template")
    void generatePath_factory_shouldReturnCorrectPath() {
        String path = strategy.generatePath("factory", context, projectName);
        assertThat(path).isEqualTo("spring-ddd-domain/src/main/java/com/springddd/domain/user/SysUserDomainFactory.java");
    }

    @Test
    @DisplayName("generatePath should return correct path for domainRepository template")
    void generatePath_domainRepository_shouldReturnCorrectPath() {
        String path = strategy.generatePath("domainRepository", context, projectName);
        assertThat(path).isEqualTo("spring-ddd-domain/src/main/java/com/springddd/domain/user/SysUserDomainRepository.java");
    }

    @Test
    @DisplayName("generatePath should return correct path for deleteDomain template")
    void generatePath_deleteDomain_shouldReturnCorrectPath() {
        String path = strategy.generatePath("deleteDomain", context, projectName);
        assertThat(path).isEqualTo("spring-ddd-domain/src/main/java/com/springddd/domain/user/DeleteSysUserDomainService.java");
    }

    @Test
    @DisplayName("generatePath should return correct path for wipeDomain template")
    void generatePath_wipeDomain_shouldReturnCorrectPath() {
        String path = strategy.generatePath("wipeDomain", context, projectName);
        assertThat(path).isEqualTo("spring-ddd-domain/src/main/java/com/springddd/domain/user/WipeSysUserDomainService.java");
    }

    @Test
    @DisplayName("generatePath should return correct path for restoreDomain template")
    void generatePath_restoreDomain_shouldReturnCorrectPath() {
        String path = strategy.generatePath("restoreDomain", context, projectName);
        assertThat(path).isEqualTo("spring-ddd-domain/src/main/java/com/springddd/domain/user/RestoreSysUserDomainService.java");
    }

    @Test
    @DisplayName("generatePath should return correct path for aggregateRoot using aggregateViews")
    void generatePath_aggregateRoot_shouldUseAggregateViews() {
        GenAggregateView aggregateView = new GenAggregateView();
        aggregateView.setObjectType((byte) 1);
        aggregateView.setHasCreated(true);
        aggregateView.setObjectName("SysUserAggregate");

        Map<String, Object> ctx = Map.of(
                "moduleName", "user",
                "packageName", "com.springddd",
                "className", "SysUser",
                "aggregateViews", List.of(aggregateView)
        );

        String path = strategy.generatePath("aggregateRoot", ctx, projectName);
        assertThat(path).isEqualTo("spring-ddd-domain/src/main/java/com/springddd/domain/user/SysUserAggregate.java");
    }

    @Test
    @DisplayName("generatePath should return correct path for objectValue using aggregateViews")
    void generatePath_objectValue_shouldUseAggregateViews() {
        GenAggregateView valueObject = new GenAggregateView();
        valueObject.setObjectType((byte) 2);
        valueObject.setHasCreated(true);
        valueObject.setObjectName("UserInfo");

        Map<String, Object> ctx = Map.of(
                "moduleName", "user",
                "packageName", "com.springddd",
                "className", "SysUser",
                "aggregateViews", List.of(valueObject)
        );

        String path = strategy.generatePath("objectValue", ctx, projectName);
        assertThat(path).isEqualTo("spring-ddd-domain/src/main/java/com/springddd/domain/user/UserInfo.java");
    }

    @Test
    @DisplayName("generatePath should return correct path for extendInfo using aggregateViews")
    void generatePath_extendInfo_shouldUseAggregateViews() {
        GenAggregateView extendInfo = new GenAggregateView();
        extendInfo.setObjectType((byte) 3);
        extendInfo.setHasCreated(true);
        extendInfo.setObjectName("UserExtendInfo");

        Map<String, Object> ctx = Map.of(
                "moduleName", "user",
                "packageName", "com.springddd",
                "className", "SysUser",
                "aggregateViews", List.of(extendInfo)
        );

        String path = strategy.generatePath("extendInfo", ctx, projectName);
        assertThat(path).isEqualTo("spring-ddd-domain/src/main/java/com/springddd/domain/user/UserExtendInfo.java");
    }

    @Test
    @DisplayName("generatePath should return null for unsupported template")
    void generatePath_unsupported_shouldReturnNull() {
        String path = strategy.generatePath("unsupported", context, projectName);
        assertThat(path).isNull();
    }

    @Test
    @DisplayName("generatePath should throw when aggregateRoot has no matching view")
    void generatePath_aggregateRoot_noMatch_shouldThrow() {
        GenAggregateView wrongType = new GenAggregateView();
        wrongType.setObjectType((byte) 2);
        wrongType.setHasCreated(true);
        wrongType.setObjectName("Wrong");

        Map<String, Object> ctx = Map.of(
                "moduleName", "user",
                "packageName", "com.springddd",
                "className", "SysUser",
                "aggregateViews", List.of(wrongType)
        );

        org.junit.jupiter.api.Assertions.assertThrows(java.util.NoSuchElementException.class,
                () -> strategy.generatePath("aggregateRoot", ctx, projectName));
    }

    @Test
    @DisplayName("generatePath should throw when objectValue has no matching view")
    void generatePath_objectValue_noMatch_shouldThrow() {
        GenAggregateView wrongType = new GenAggregateView();
        wrongType.setObjectType((byte) 1);
        wrongType.setHasCreated(true);
        wrongType.setObjectName("Wrong");

        Map<String, Object> ctx = Map.of(
                "moduleName", "user",
                "packageName", "com.springddd",
                "className", "SysUser",
                "aggregateViews", List.of(wrongType)
        );

        org.junit.jupiter.api.Assertions.assertThrows(java.util.NoSuchElementException.class,
                () -> strategy.generatePath("objectValue", ctx, projectName));
    }

    @Test
    @DisplayName("generatePath should throw when extendInfo has no matching view")
    void generatePath_extendInfo_noMatch_shouldThrow() {
        GenAggregateView wrongType = new GenAggregateView();
        wrongType.setObjectType((byte) 1);
        wrongType.setHasCreated(true);
        wrongType.setObjectName("Wrong");

        Map<String, Object> ctx = Map.of(
                "moduleName", "user",
                "packageName", "com.springddd",
                "className", "SysUser",
                "aggregateViews", List.of(wrongType)
        );

        org.junit.jupiter.api.Assertions.assertThrows(java.util.NoSuchElementException.class,
                () -> strategy.generatePath("extendInfo", ctx, projectName));
    }

    @Test
    @DisplayName("generatePath should throw when matching view has hasCreated false")
    void generatePath_aggregateRoot_notCreated_shouldThrow() {
        GenAggregateView notCreated = new GenAggregateView();
        notCreated.setObjectType((byte) 1);
        notCreated.setHasCreated(false);
        notCreated.setObjectName("UserId");

        Map<String, Object> ctx = Map.of(
                "moduleName", "user",
                "packageName", "com.springddd",
                "className", "SysUser",
                "aggregateViews", List.of(notCreated)
        );

        org.junit.jupiter.api.Assertions.assertThrows(java.util.NoSuchElementException.class,
                () -> strategy.generatePath("aggregateRoot", ctx, projectName));
    }

    @Test
    @DisplayName("generatePath aggregateRoot 当第一个不匹配第二个匹配时应返回正确路径")
    void generatePath_aggregateRoot_firstMismatchSecondMatch_shouldReturnPath() {
        GenAggregateView view1 = new GenAggregateView();
        view1.setObjectType((byte) 2);
        view1.setHasCreated(true);
        view1.setObjectName("Wrong");

        GenAggregateView view2 = new GenAggregateView();
        view2.setObjectType((byte) 1);
        view2.setHasCreated(true);
        view2.setObjectName("CorrectAggregate");

        Map<String, Object> ctx = Map.of(
                "moduleName", "user",
                "packageName", "com.springddd",
                "className", "SysUser",
                "aggregateViews", List.of(view1, view2)
        );

        String path = strategy.generatePath("aggregateRoot", ctx, projectName);
        assertThat(path).isEqualTo("spring-ddd-domain/src/main/java/com/springddd/domain/user/CorrectAggregate.java");
    }

    @Test
    @DisplayName("generatePath objectValue 当第一个不匹配第二个匹配时应返回正确路径")
    void generatePath_objectValue_firstMismatchSecondMatch_shouldReturnPath() {
        GenAggregateView view1 = new GenAggregateView();
        view1.setObjectType((byte) 1);
        view1.setHasCreated(true);
        view1.setObjectName("Wrong");

        GenAggregateView view2 = new GenAggregateView();
        view2.setObjectType((byte) 2);
        view2.setHasCreated(true);
        view2.setObjectName("CorrectValue");

        Map<String, Object> ctx = Map.of(
                "moduleName", "user",
                "packageName", "com.springddd",
                "className", "SysUser",
                "aggregateViews", List.of(view1, view2)
        );

        String path = strategy.generatePath("objectValue", ctx, projectName);
        assertThat(path).isEqualTo("spring-ddd-domain/src/main/java/com/springddd/domain/user/CorrectValue.java");
    }

    @Test
    @DisplayName("generatePath extendInfo 当第一个不匹配第二个匹配时应返回正确路径")
    void generatePath_extendInfo_firstMismatchSecondMatch_shouldReturnPath() {
        GenAggregateView view1 = new GenAggregateView();
        view1.setObjectType((byte) 1);
        view1.setHasCreated(true);
        view1.setObjectName("Wrong");

        GenAggregateView view2 = new GenAggregateView();
        view2.setObjectType((byte) 3);
        view2.setHasCreated(true);
        view2.setObjectName("CorrectExtend");

        Map<String, Object> ctx = Map.of(
                "moduleName", "user",
                "packageName", "com.springddd",
                "className", "SysUser",
                "aggregateViews", List.of(view1, view2)
        );

        String path = strategy.generatePath("extendInfo", ctx, projectName);
        assertThat(path).isEqualTo("spring-ddd-domain/src/main/java/com/springddd/domain/user/CorrectExtend.java");
    }

    @Test
    @DisplayName("generatePath should throw when objectValue has hasCreated false")
    void generatePath_objectValue_notCreated_shouldThrow() {
        GenAggregateView notCreated = new GenAggregateView();
        notCreated.setObjectType((byte) 2);
        notCreated.setHasCreated(false);
        notCreated.setObjectName("Account");

        Map<String, Object> ctx = Map.of(
                "moduleName", "user",
                "packageName", "com.springddd",
                "className", "SysUser",
                "aggregateViews", List.of(notCreated)
        );

        org.junit.jupiter.api.Assertions.assertThrows(java.util.NoSuchElementException.class,
                () -> strategy.generatePath("objectValue", ctx, projectName));
    }

    @Test
    @DisplayName("generatePath should throw when extendInfo has hasCreated false")
    void generatePath_extendInfo_notCreated_shouldThrow() {
        GenAggregateView notCreated = new GenAggregateView();
        notCreated.setObjectType((byte) 3);
        notCreated.setHasCreated(false);
        notCreated.setObjectName("UserExtendInfo");

        Map<String, Object> ctx = Map.of(
                "moduleName", "user",
                "packageName", "com.springddd",
                "className", "SysUser",
                "aggregateViews", List.of(notCreated)
        );

        org.junit.jupiter.api.Assertions.assertThrows(java.util.NoSuchElementException.class,
                () -> strategy.generatePath("extendInfo", ctx, projectName));
    }
}
