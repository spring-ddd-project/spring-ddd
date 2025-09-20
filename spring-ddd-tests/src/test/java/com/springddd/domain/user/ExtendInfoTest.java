package com.springddd.domain.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExtendInfoTest {

    @Test
    void defaultConstructor_ShouldCreateEmptyExtendInfo() {
        // When
        ExtendInfo extendInfo = new ExtendInfo();

        // Then
        assertNull(extendInfo.getAvatar());
        assertNull(extendInfo.getSex());
    }

    @Test
    void setters_ShouldUpdateValues() {
        // Given
        ExtendInfo extendInfo = new ExtendInfo();

        // When
        extendInfo.setAvatar("avatar.png");
        extendInfo.setSex(true);

        // Then
        assertEquals("avatar.png", extendInfo.getAvatar());
        assertTrue(extendInfo.getSex());
    }

    @Test
    void equals_ShouldWorkCorrectly() {
        // Given
        ExtendInfo info1 = new ExtendInfo();
        ExtendInfo info2 = new ExtendInfo();
        ExtendInfo info3 = new ExtendInfo();

        info1.setAvatar("avatar.png");
        info2.setAvatar("avatar.png");
        info3.setAvatar("other.png");

        // Then
        assertEquals(info1, info2);
        assertNotEquals(info1, info3);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        // Given
        ExtendInfo info1 = new ExtendInfo();
        ExtendInfo info2 = new ExtendInfo();

        info1.setAvatar("avatar.png");
        info2.setAvatar("avatar.png");

        // Then
        assertEquals(info1.hashCode(), info2.hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectFormat() {
        // Given
        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar("avatar.png");

        // When
        String result = extendInfo.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("avatar.png"));
    }

    @Test
    void sex_ShouldToggleCorrectly() {
        // Given
        ExtendInfo extendInfo = new ExtendInfo();

        // When & Then
        assertNull(extendInfo.getSex());
        extendInfo.setSex(true);
        assertTrue(extendInfo.getSex());
        extendInfo.setSex(false);
        assertFalse(extendInfo.getSex());
    }
}
