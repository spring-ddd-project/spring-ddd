package com.springddd.domain.util;

import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.leaf.service.LeafSegmentIdGenerateDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class IdGeneratorTest {

    private IdGenerator idGenerator;
    private LeafSegmentIdGenerateDomainService leafSegmentIdGenerateDomainService;

    @BeforeEach
    void setUp() {
        leafSegmentIdGenerateDomainService = mock(LeafSegmentIdGenerateDomainService.class);
        when(leafSegmentIdGenerateDomainService.nextId(anyString())).thenReturn(Mono.just(999L));
        idGenerator = new IdGenerator(leafSegmentIdGenerateDomainService);
        SecurityUtils.setUsername("testUser");
    }

    static class EntityWithId {
        @Id
        @IdGenerate
        private Long id;
    }

    static class EntityWithIdAndCreatedBy {
        @Id
        @IdGenerate
        private Long id;
        @CreatedBy
        private String createdBy;
        @LastModifiedBy
        private String lastModifiedBy;
    }

    static class EntityWithExistingId {
        @Id
        @IdGenerate
        private Long id = 100L;
        @LastModifiedBy
        private String lastModifiedBy;
    }

    static class EntityWithoutIdGenerate {
        @Id
        private Long id;
    }

    static class EntityWithMultipleIdGenerate {
        @Id
        @IdGenerate
        private Long id1;
        @Id
        @IdGenerate
        private Long id2;
    }

    @IdGenerate(strategy = IdGenerateStrategy.LEAF_SEGMENT, key = "class_key")
    static class EntityWithLeafSegmentClassLevel {
        @Id
        private Long id;
    }

    static class EntityWithLeafSegmentFieldLevel {
        @Id
        @IdGenerate(strategy = IdGenerateStrategy.LEAF_SEGMENT, key = "field_key")
        private Long id;
    }

    @IdGenerate(strategy = IdGenerateStrategy.LEAF_SEGMENT)
    static class SysUserEntity {
        @Id
        private Long id;
    }

    @IdGenerate(strategy = IdGenerateStrategy.LEAF_SEGMENT)
    static class Entity {
        @Id
        private Long id;
    }

    @IdGenerate
    static class EntityWithIdGenerateButNoIdField {
        private String name;
    }

    static class EntityWithExistingIdNoLastModifiedBy {
        @Id
        @IdGenerate
        private Long id = 100L;
    }

    static class EntityWithEmptyFieldKey {
        @Id
        @IdGenerate(strategy = IdGenerateStrategy.LEAF_SEGMENT)
        private Long id;
    }

    @IdGenerate(strategy = IdGenerateStrategy.LEAF_SEGMENT)
    static class EntityWithExtraFieldForResolveKey {
        @Id
        private Long id;
        private String name;
    }

    static class EntityWithoutIdGenerateAndNoIdField {
        private String name;
    }

    static class EntityWithMixedIdGenerate {
        @Id
        @IdGenerate
        private Long id1;
        @Id
        private Long id2;
    }

    @Test
    void shouldGenerateIdForNullField() {
        EntityWithId entity = new EntityWithId();
        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> {
                    assertNotNull(((EntityWithId) result).id);
                    assertTrue(((EntityWithId) result).id > 0);
                })
                .verifyComplete();
    }

    @Test
    void shouldSetCreatedByAndLastModifiedBy() {
        EntityWithIdAndCreatedBy entity = new EntityWithIdAndCreatedBy();
        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> {
                    EntityWithIdAndCreatedBy e = (EntityWithIdAndCreatedBy) result;
                    assertNotNull(e.id);
                    assertEquals("testUser", e.createdBy);
                    assertEquals("testUser", e.lastModifiedBy);
                })
                .verifyComplete();
    }

    @Test
    void shouldOnlySetLastModifiedByForExistingId() {
        EntityWithExistingId entity = new EntityWithExistingId();
        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> {
                    EntityWithExistingId e = (EntityWithExistingId) result;
                    assertEquals(100L, e.id);
                    assertEquals("testUser", e.lastModifiedBy);
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnEntityAsIs_whenNoIdGenerateAnnotation() {
        EntityWithoutIdGenerate entity = new EntityWithoutIdGenerate();
        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> {
                    assertSame(entity, result);
                    assertNull(((EntityWithoutIdGenerate) result).id);
                })
                .verifyComplete();
    }

    @Test
    void shouldThrow_whenMultipleIdGenerateFields() {
        EntityWithMultipleIdGenerate entity = new EntityWithMultipleIdGenerate();
        assertThrows(IllegalStateException.class, () ->
                idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")).block()
        );
    }

    @Test
    void shouldThrowRuntimeException_whenGetFieldValueThrowsIllegalAccessException() throws Exception {
        EntityWithId entity = new EntityWithId();
        Field field = EntityWithId.class.getDeclaredField("id");
        // Intentionally not calling setAccessible(true) to trigger IllegalAccessException

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                idGenerator.getFieldValue(field, entity)
        );
        assertTrue(ex.getMessage().contains("Failed to access field"));
        assertTrue(ex.getCause() instanceof IllegalAccessException);
    }

    @Test
    void shouldThrowRuntimeException_whenSetFieldValueThrowsIllegalAccessException() throws Exception {
        EntityWithId entity = new EntityWithId();
        Field field = EntityWithId.class.getDeclaredField("id");
        // Intentionally not calling setAccessible(true) to trigger IllegalAccessException

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                idGenerator.setFieldValue(field, entity, 123L)
        );
        assertTrue(ex.getMessage().contains("Failed to set field"));
        assertTrue(ex.getCause() instanceof IllegalAccessException);
    }

    // ===== LEAF_SEGMENT strategy tests =====

    @Test
    void shouldCallLeafSegmentServiceWithClassLevelKey() {
        EntityWithLeafSegmentClassLevel entity = new EntityWithLeafSegmentClassLevel();
        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> {
                    EntityWithLeafSegmentClassLevel e = (EntityWithLeafSegmentClassLevel) result;
                    assertEquals(999L, e.id);
                })
                .verifyComplete();
        verify(leafSegmentIdGenerateDomainService).nextId("class_key");
    }

    @Test
    void shouldCallLeafSegmentServiceWithFieldLevelKey() {
        EntityWithLeafSegmentFieldLevel entity = new EntityWithLeafSegmentFieldLevel();
        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> {
                    EntityWithLeafSegmentFieldLevel e = (EntityWithLeafSegmentFieldLevel) result;
                    assertEquals(999L, e.id);
                })
                .verifyComplete();
        verify(leafSegmentIdGenerateDomainService).nextId("field_key");
    }

    @Test
    void shouldDeriveKeyFromClassName() {
        SysUserEntity entity = new SysUserEntity();
        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> {
                    SysUserEntity e = (SysUserEntity) result;
                    assertEquals(999L, e.id);
                })
                .verifyComplete();
        verify(leafSegmentIdGenerateDomainService).nextId("sys_user");
    }

    @Test
    void shouldThrow_whenLeafSegmentKeyIsEmpty() {
        Entity entity = new Entity();
        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    void shouldNotRegenerateId_whenIdAlreadyExists() {
        EntityWithExistingId entity = new EntityWithExistingId();
        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> {
                    EntityWithExistingId e = (EntityWithExistingId) result;
                    assertEquals(100L, e.id);
                })
                .verifyComplete();
        verifyNoInteractions(leafSegmentIdGenerateDomainService);
    }

    @Test
    void shouldReturnEntity_whenIdFieldsEmpty() {
        EntityWithIdGenerateButNoIdField entity = new EntityWithIdGenerateButNoIdField();
        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> {
                    assertSame(entity, result);
                })
                .verifyComplete();
    }

    @Test
    void shouldNotSetLastModifiedBy_whenFieldAbsent_andIdExists() {
        EntityWithExistingIdNoLastModifiedBy entity = new EntityWithExistingIdNoLastModifiedBy();
        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> {
                    EntityWithExistingIdNoLastModifiedBy e = (EntityWithExistingIdNoLastModifiedBy) result;
                    assertEquals(100L, e.id);
                })
                .verifyComplete();
        verifyNoInteractions(leafSegmentIdGenerateDomainService);
    }

    @Test
    void shouldResolveEmptyKeyFromFieldName() {
        EntityWithEmptyFieldKey entity = new EntityWithEmptyFieldKey();
        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> {
                    EntityWithEmptyFieldKey e = (EntityWithEmptyFieldKey) result;
                    assertEquals(999L, e.id);
                })
                .verifyComplete();
        verify(leafSegmentIdGenerateDomainService).nextId("entity_with_empty_field_key");
    }

    @Test
    void shouldResolveKeyWithNonIdField() {
        EntityWithExtraFieldForResolveKey entity = new EntityWithExtraFieldForResolveKey();
        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> {
                    EntityWithExtraFieldForResolveKey e = (EntityWithExtraFieldForResolveKey) result;
                    assertEquals(999L, e.id);
                })
                .verifyComplete();
        verify(leafSegmentIdGenerateDomainService).nextId("entity_with_extra_field_for_resolve_key");
    }

    @Test
    void shouldResolveIdGenerateWithNonIdField() {
        EntityWithoutIdGenerateAndNoIdField entity = new EntityWithoutIdGenerateAndNoIdField();
        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> {
                    assertSame(entity, result);
                })
                .verifyComplete();
    }

    @Test
    void shouldHandleCamelToSnakeWithNull() throws Exception {
        Method method = IdGenerator.class.getDeclaredMethod("camelToSnake", String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(null, (String) null);
        assertEquals("", result);
    }

    @Test
    void shouldFilterIdFieldsWithMixedAnnotations() {
        EntityWithMixedIdGenerate entity = new EntityWithMixedIdGenerate();
        StepVerifier.create(idGenerator.onBeforeConvert(entity, SqlIdentifier.quoted("test")))
                .assertNext(result -> {
                    EntityWithMixedIdGenerate e = (EntityWithMixedIdGenerate) result;
                    assertNotNull(e.id1);
                    assertNull(e.id2);
                })
                .verifyComplete();
    }

}
