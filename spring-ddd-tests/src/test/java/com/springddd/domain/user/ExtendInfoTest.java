package com.springddd.domain.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExtendInfoTest {

    @Test
    void shouldCreateExtendInfoWithDefaultConstructor() {
        ExtendInfo info = new ExtendInfo();
        assertNull(info.getAvatar());
        assertNull(info.getSex());
    }

    @Test
    void shouldSetAndGetAvatar() {
        ExtendInfo info = new ExtendInfo();
        info.setAvatar("avatar.png");
        assertEquals("avatar.png", info.getAvatar());
    }

    @Test
    void shouldSetAndGetSex() {
        ExtendInfo info = new ExtendInfo();
        info.setSex(true);
        assertTrue(info.getSex());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        ExtendInfo info1 = new ExtendInfo();
        info1.setAvatar("avatar.png");
        info1.setSex(true);

        ExtendInfo info2 = new ExtendInfo();
        info2.setAvatar("avatar.png");
        info2.setSex(true);

        assertEquals(info1, info2);
    }

    @Test
    void equals_shouldFailForDifferentValues() {
        ExtendInfo info1 = new ExtendInfo();
        info1.setAvatar("avatar1.png");
        info1.setSex(true);

        ExtendInfo info2 = new ExtendInfo();
        info2.setAvatar("avatar2.png");
        info2.setSex(false);

        assertNotEquals(info1, info2);
    }

    @Test
    void toString_shouldReturnValues() {
        ExtendInfo info = new ExtendInfo();
        info.setAvatar("test.png");
        info.setSex(false);
        String result = info.toString();
        assertTrue(result.contains("test.png"));
    }
}