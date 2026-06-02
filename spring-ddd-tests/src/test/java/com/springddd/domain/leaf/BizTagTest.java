package com.springddd.domain.leaf;

import com.springddd.domain.leaf.exception.BizTagEmptyException;
import com.springddd.domain.leaf.exception.BizTagNullException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BizTagTest {

    @Test
    void shouldCreateBizTagWithValidValue() {
        BizTag bizTag = new BizTag("order");
        assertEquals("order", bizTag.value());
    }

    @Test
    void shouldTrimWhitespace() {
        BizTag bizTag = new BizTag("  order  ");
        assertEquals("order", bizTag.value());
    }

    @Test
    void shouldThrowBizTagNullExceptionWhenValueIsNull() {
        assertThrows(BizTagNullException.class, () -> new BizTag(null));
    }

    @Test
    void shouldThrowBizTagNullExceptionWhenValueIsEmpty() {
        assertThrows(BizTagNullException.class, () -> new BizTag(""));
    }

    @Test
    void shouldThrowBizTagEmptyExceptionWhenValueIsWhitespaceOnly() {
        assertThrows(BizTagEmptyException.class, () -> new BizTag("   "));
    }

    @Test
    void equals_shouldWorkForSameValue() {
        BizTag bizTag1 = new BizTag("order");
        BizTag bizTag2 = new BizTag("order");
        assertEquals(bizTag1, bizTag2);
    }

    @Test
    void equals_shouldFailForDifferentValue() {
        BizTag bizTag1 = new BizTag("order");
        BizTag bizTag2 = new BizTag("user");
        assertNotEquals(bizTag1, bizTag2);
    }

    @Test
    void hashCode_shouldBeConsistentForSameValue() {
        BizTag bizTag1 = new BizTag("order");
        BizTag bizTag2 = new BizTag("order");
        assertEquals(bizTag1.hashCode(), bizTag2.hashCode());
    }

    @Test
    void toString_shouldContainValue() {
        BizTag bizTag = new BizTag("order");
        assertTrue(bizTag.toString().contains("order"));
    }
}
