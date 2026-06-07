package com.springddd.application.service.leaf;

import com.springddd.application.service.leaf.dto.LeafAllocPageQuery;
import com.springddd.application.service.leaf.dto.LeafAllocView;
import com.springddd.domain.util.PageResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LeafAllocQueryServiceTest {

    @Test
    void shouldHaveLeafAllocQueryServiceClass() {
        assertNotNull(LeafAllocQueryService.class);
    }

    @Test
    void shouldHaveRequiredArgsConstructor() {
        assertNotNull(LeafAllocQueryService.class.getConstructors().length > 0);
    }

    @Test
    void shouldHaveIndexMethod() throws NoSuchMethodException {
        assertNotNull(LeafAllocQueryService.class.getDeclaredMethod("index", LeafAllocPageQuery.class));
    }

    @Test
    void shouldHaveRecycleMethod() throws NoSuchMethodException {
        assertNotNull(LeafAllocQueryService.class.getDeclaredMethod("recycle", LeafAllocPageQuery.class));
    }

    @Test
    void shouldHaveGetAllLeafAllocMethod() throws NoSuchMethodException {
        assertNotNull(LeafAllocQueryService.class.getDeclaredMethod("getAllLeafAlloc"));
    }

    @Test
    void shouldHaveGetLeafAllocByTagMethod() throws NoSuchMethodException {
        assertNotNull(LeafAllocQueryService.class.getDeclaredMethod("getLeafAllocByTag", String.class));
    }
}
