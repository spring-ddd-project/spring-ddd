package com.springddd.domain.gen.factory;

import com.springddd.domain.gen.GenAggregateValueObject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenAggregateValueObjectFlyweightFactoryTest {

    @Test
    void testGetValueObjectCreatesNew() {
        GenAggregateValueObject vo = GenAggregateValueObjectFlyweightFactory.getValueObject("TestName", "testValue", (byte) 1);
        assertThat(vo.objectName()).isEqualTo("TestName");
        assertThat(vo.objectValue()).isEqualTo("testValue");
        assertThat(vo.objectType()).isEqualTo((byte) 1);
    }

    @Test
    void testGetValueObjectReturnsCached() {
        GenAggregateValueObject vo1 = GenAggregateValueObjectFlyweightFactory.getValueObject("Cached", "cachedValue", (byte) 2);
        GenAggregateValueObject vo2 = GenAggregateValueObjectFlyweightFactory.getValueObject("Cached", "cachedValue", (byte) 2);
        assertThat(vo1).isSameAs(vo2);
    }

    @Test
    void testDifferentKeysCreateDifferentObjects() {
        GenAggregateValueObject vo1 = GenAggregateValueObjectFlyweightFactory.getValueObject("A", "v1", (byte) 1);
        GenAggregateValueObject vo2 = GenAggregateValueObjectFlyweightFactory.getValueObject("B", "v2", (byte) 1);
        assertThat(vo1).isNotSameAs(vo2);
    }

    @Test
    void testConstructor_shouldCreateInstance() {
        GenAggregateValueObjectFlyweightFactory factory = new GenAggregateValueObjectFlyweightFactory();
        assertThat(factory).isNotNull();
    }
}
