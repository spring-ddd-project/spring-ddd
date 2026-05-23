package com.springddd.domain.util;

import com.springddd.domain.leaf.LeafSegmentDomainService;
import com.springddd.domain.leaf.SnowflakeDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IdGeneratorTest {

    @Mock
    private LeafSegmentDomainService leafSegmentDomainService;

    @Mock
    private SnowflakeDomainService snowflakeDomainService;

    @InjectMocks
    private IdGenerator idGenerator;

    @Test
    @DisplayName("onBeforeConvert 当无 @Id 和 @LeafId 时应返回原实体")
    void onBeforeConvert_whenNoIdAnnotations_shouldReturnEntity() {
        class SimpleEntity {
            private Long id;
        }

        SimpleEntity entity = new SimpleEntity();
        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> assertThat(result).isSameAs(entity))
                .verifyComplete();
    }

    @Test
    @DisplayName("onBeforeConvert 当 id 已有值时应只更新 lastModifiedBy")
    void onBeforeConvert_whenIdExists_shouldUpdateLastModifiedBy() {
        class EntityWithId {
            @Id
            @LeafId
            private Long id = 1L;
            @LastModifiedBy
            private String updateBy;
        }

        EntityWithId entity = new EntityWithId();
        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> assertThat(result).isSameAs(entity))
                .verifyComplete();
    }

    @Test
    @DisplayName("onBeforeConvert 当 id 为空且有 bizTag 时应使用 segment 模式")
    void onBeforeConvert_whenEmptyIdAndBizTag_shouldUseSegmentMode() {
        class EntityWithBizTag {
            @Id
            @LeafId("test_tag")
            private Long id;
            @CreatedBy
            private String createBy;
            @LastModifiedBy
            private String updateBy;
        }

        EntityWithBizTag entity = new EntityWithBizTag();
        when(leafSegmentDomainService.getId("test_tag")).thenReturn(Mono.just(100L));

        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> {
                    assertThat(result).isSameAs(entity);
                    assertThat(entity.id).isEqualTo(100L);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("onBeforeConvert 当 id 为空且无 bizTag 时应使用 snowflake 模式")
    void onBeforeConvert_whenEmptyIdAndNoBizTag_shouldUseSnowflakeMode() {
        class EntityWithoutBizTag {
            @Id
            @LeafId
            private Long id;
        }

        EntityWithoutBizTag entity = new EntityWithoutBizTag();
        when(snowflakeDomainService.getId()).thenReturn(Mono.just(999L));

        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> {
                    assertThat(result).isSameAs(entity);
                    assertThat(entity.id).isEqualTo(999L);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("onBeforeConvert 当只有 @Id 无 @LeafId 时应返回原实体")
    void onBeforeConvert_whenIdWithoutLeafId_shouldReturnEntity() {
        class EntityWithIdOnly {
            @Id
            private Long id;
        }

        EntityWithIdOnly entity = new EntityWithIdOnly();
        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> assertThat(result).isSameAs(entity))
                .verifyComplete();
    }

    @Test
    @DisplayName("onBeforeConvert 当多个 @Id+@LeafId 字段时应抛 IllegalStateException")
    void onBeforeConvert_whenMultipleIdLeafIdFields_shouldThrowException() {
        class EntityWithMultipleIds {
            @Id
            @LeafId
            private Long id1;
            @Id
            @LeafId
            private Long id2;
        }

        EntityWithMultipleIds entity = new EntityWithMultipleIds();
        assertThatThrownBy(() -> idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")).block())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("has more than one field annotated with both @Id and @LeafId");
    }

    @Test
    @DisplayName("onBeforeConvert 当只有 @CreatedBy 时应设置 createBy")
    void onBeforeConvert_whenOnlyCreatedBy_shouldSetCreateBy() {
        class EntityWithOnlyCreatedBy {
            @Id
            @LeafId("test_tag")
            private Long id;
            @CreatedBy
            private String createBy;
        }

        EntityWithOnlyCreatedBy entity = new EntityWithOnlyCreatedBy();
        when(leafSegmentDomainService.getId("test_tag")).thenReturn(Mono.just(200L));

        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> {
                    assertThat(result).isSameAs(entity);
                    assertThat(entity.id).isEqualTo(200L);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("onBeforeConvert 当只有 @LastModifiedBy 且 id 存在时应只更新 lastModifiedBy")
    void onBeforeConvert_whenOnlyLastModifiedByAndIdExists_shouldUpdateLastModifiedBy() {
        class EntityWithOnlyLastModifiedBy {
            @Id
            @LeafId
            private Long id = 1L;
            @LastModifiedBy
            private String updateBy;
        }

        EntityWithOnlyLastModifiedBy entity = new EntityWithOnlyLastModifiedBy();
        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> assertThat(result).isSameAs(entity))
                .verifyComplete();
    }

    @Test
    @DisplayName("onBeforeConvert 当无审计字段且 id 为空时应使用 snowflake 模式")
    void onBeforeConvert_whenNoAuditFieldsAndEmptyId_shouldUseSnowflakeMode() {
        class EntityWithNoAuditFields {
            @Id
            @LeafId
            private Long id;
        }

        EntityWithNoAuditFields entity = new EntityWithNoAuditFields();
        when(snowflakeDomainService.getId()).thenReturn(Mono.just(888L));

        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> {
                    assertThat(result).isSameAs(entity);
                    assertThat(entity.id).isEqualTo(888L);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("onBeforeConvert 当无审计字段且 id 存在时应返回实体")
    void onBeforeConvert_whenNoAuditFieldsAndIdExists_shouldReturnEntity() {
        class EntityWithNoAuditFieldsAndExistingId {
            @Id
            @LeafId
            private Long id = 1L;
        }

        EntityWithNoAuditFieldsAndExistingId entity = new EntityWithNoAuditFieldsAndExistingId();
        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> assertThat(result).isSameAs(entity))
                .verifyComplete();
    }

    @Test
    @DisplayName("onBeforeConvert 当 segment 模式设置 final 字段失败时应抛 RuntimeException")
    void onBeforeConvert_whenSegmentModeSetFinalFieldFails_shouldThrowRuntimeException() {
        record EntityWithFinalId(@Id @LeafId("test_tag") Long id) {
        }

        EntityWithFinalId entity = new EntityWithFinalId(null);
        when(leafSegmentDomainService.getId("test_tag")).thenReturn(Mono.just(300L));

        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    @DisplayName("onBeforeConvert 当 snowflake 模式设置 final 字段失败时应抛 RuntimeException")
    void onBeforeConvert_whenSnowflakeModeSetFinalFieldFails_shouldThrowRuntimeException() {
        record EntityWithFinalId(@Id @LeafId Long id) {
        }

        EntityWithFinalId entity = new EntityWithFinalId(null);
        when(snowflakeDomainService.getId()).thenReturn(Mono.just(777L));

        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .expectError(RuntimeException.class)
                .verify();
    }
}
