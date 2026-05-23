package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.FilterComponentNullException;
import com.springddd.domain.gen.exception.FilterNullException;
import com.springddd.domain.gen.exception.OrderNullException;
import com.springddd.domain.gen.exception.VisibleNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        Table table = new Table(true, true, true, (byte) 1, (byte) 1);
        assertThat(table.tableVisible()).isTrue();
        assertThat(table.tableOrder()).isTrue();
        assertThat(table.tableFilter()).isTrue();
        assertThat(table.tableFilterComponent()).isEqualTo((byte) 1);
        assertThat(table.tableFilterType()).isEqualTo((byte) 1);
    }

    @Test
    @DisplayName("tableVisible 为 null 应抛 VisibleNullException")
    void constructor_withNullTableVisible_shouldThrowException() {
        assertThatThrownBy(() -> new Table(null, true, true, (byte) 1, (byte) 1))
                .isInstanceOf(VisibleNullException.class);
    }

    @Test
    @DisplayName("tableOrder 为 null 应抛 OrderNullException")
    void constructor_withNullTableOrder_shouldThrowException() {
        assertThatThrownBy(() -> new Table(true, null, true, (byte) 1, (byte) 1))
                .isInstanceOf(OrderNullException.class);
    }

    @Test
    @DisplayName("tableFilter 为 null 应抛 FilterNullException")
    void constructor_withNullTableFilter_shouldThrowException() {
        assertThatThrownBy(() -> new Table(true, true, null, (byte) 1, (byte) 1))
                .isInstanceOf(FilterNullException.class);
    }

    @Test
    @DisplayName("tableFilter 为 true 且 tableFilterComponent 为 null 应抛 FilterComponentNullException")
    void constructor_withFilterTrueAndNullComponent_shouldThrowException() {
        assertThatThrownBy(() -> new Table(true, true, true, null, (byte) 1))
                .isInstanceOf(FilterComponentNullException.class);
    }

    @Test
    @DisplayName("tableFilter 为 false 且 tableFilterComponent 为 null 不应抛异常")
    void constructor_withFilterFalseAndNullComponent_shouldNotThrowException() {
        Table table = new Table(true, true, false, null, (byte) 1);
        assertThat(table.tableFilter()).isFalse();
        assertThat(table.tableFilterComponent()).isNull();
    }
}
