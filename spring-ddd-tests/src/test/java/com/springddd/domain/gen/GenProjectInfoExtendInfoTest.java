package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GenProjectInfoExtendInfoTest {

    @Test
    void shouldCreateWithValidRequestName() {
        GenProjectInfoExtendInfo info = new GenProjectInfoExtendInfo("TestRequest");
        assertEquals("TestRequest", info.requestName());
    }

    @Test
    void shouldCreateWithNullRequestName() {
        GenProjectInfoExtendInfo info = new GenProjectInfoExtendInfo(null);
        assertNull(info.requestName());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        GenProjectInfoExtendInfo info1 = new GenProjectInfoExtendInfo("TestRequest");
        GenProjectInfoExtendInfo info2 = new GenProjectInfoExtendInfo("TestRequest");
        assertEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentValues() {
        GenProjectInfoExtendInfo info1 = new GenProjectInfoExtendInfo("Request1");
        GenProjectInfoExtendInfo info2 = new GenProjectInfoExtendInfo("Request2");
        assertNotEquals(info1, info2);
    }

    @Test
    void toString_shouldReturnValue() {
        GenProjectInfoExtendInfo info = new GenProjectInfoExtendInfo("TestRequest");
        String result = info.toString();
        assertTrue(result.contains("TestRequest"));
    }
}