package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.I18nEnNullException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class I18nTest {

    @Test
    void shouldCreateI18nWithValidValues() {
        I18n i18n = new I18n("hello", "en_US");
        assertEquals("hello", i18n.en());
        assertEquals("en_US", i18n.locale());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        I18n i18n1 = new I18n("hello", "en_US");
        I18n i18n2 = new I18n("hello", "en_US");
        assertEquals(i18n1, i18n2);
    }

    @Test
    void toString_shouldReturnValueAsString() {
        I18n i18n = new I18n("hello", "en_US");
        String str = i18n.toString();
        assertTrue(str.contains("I18n"));
    }

    @Test
    void shouldThrowWhenEnIsNull() {
        assertThrows(I18nEnNullException.class, () -> new I18n(null, "en_US"));
    }

    @Test
    void shouldThrowWhenEnIsEmpty() {
        assertThrows(I18nEnNullException.class, () -> new I18n("", "en_US"));
    }
}
