package com.springddd.domain.util;

import com.springddd.domain.auth.SecurityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IdGeneratorTest {

    private IdGenerator idGenerator;

    @Mock
    private SqlIdentifier mockTable;

    @BeforeEach
    void setUp() {
        idGenerator = new IdGenerator();
        SecurityUtils.setUsername("testUser");
    }

    @AfterEach
    void tearDown() {
        SecurityUtils.setUsername(null);
    }

    @Test
    void shouldImplementBeforeConvertCallback() {
        // Then
        assertTrue(idGenerator instanceof BeforeConvertCallback);
    }

    @Test
    void onBeforeConvert_WithNullId_ShouldGenerateId() {
        // Given
        TestEntity entity = new TestEntity();

        // When
        Mono<Object> resultMono = idGenerator.onBeforeConvert(entity, mockTable);
        Object result = resultMono.block();

        // Then
        assertNotNull(result);
        assertNotNull(entity.getId());
        assertTrue(entity.getId() > 0);
    }

    @Test
    void onBeforeConvert_WithExistingId_ShouldNotOverwrite() {
        // Given
        TestEntityWithId entity = new TestEntityWithId();
        entity.setId(999L);

        // When
        Mono<Object> resultMono = idGenerator.onBeforeConvert(entity, mockTable);
        Object result = resultMono.block();

        // Then
        assertNotNull(result);
        assertEquals(999L, entity.getId(), "Existing ID should not be overwritten");
    }

    @Test
    void onBeforeConvert_ShouldSetCreatedByAndLastModifiedBy() {
        // Given
        TestEntity entity = new TestEntity();

        // When
        idGenerator.onBeforeConvert(entity, mockTable).block();

        // Then
        assertEquals("testUser", entity.getCreatedBy());
        assertEquals("testUser", entity.getLastModifiedBy());
    }

    @Test
    void onBeforeConvert_ShouldUpdateLastModifiedByWhenIdExists() {
        // Given
        TestEntityWithId entity = new TestEntityWithId();
        entity.setId(999L);

        // When
        idGenerator.onBeforeConvert(entity, mockTable).block();

        // Then
        assertEquals("testUser", entity.getLastModifiedBy());
    }

    // Test helper classes
    private static class TestEntity {
        @Id
        @IdGenerate
        private Long id;

        @CreatedBy
        private String createdBy;

        @LastModifiedBy
        private String lastModifiedBy;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getLastModifiedBy() {
            return lastModifiedBy;
        }

        public void setLastModifiedBy(String lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
        }
    }

    private static class TestEntityWithId {
        @Id
        @IdGenerate
        private Long id;

        @CreatedBy
        private String createdBy;

        @LastModifiedBy
        private String lastModifiedBy;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getLastModifiedBy() {
            return lastModifiedBy;
        }

        public void setLastModifiedBy(String lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
        }
    }
}
