package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FormTest {

    @Test
    void shouldCreateForm() {
        Form form = new Form((byte) 1, true, false);
        assertEquals((byte) 1, form.formComponent());
        assertTrue(form.formVisible());
        assertFalse(form.formRequired());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        Form form1 = new Form((byte) 1, true, false);
        Form form2 = new Form((byte) 1, true, false);
        assertEquals(form1, form2);
    }

    @Test
    void toString_shouldReturnValueAsString() {
        Form form = new Form((byte) 1, true, false);
        String str = form.toString();
        assertTrue(str.contains("Form"));
    }
}
