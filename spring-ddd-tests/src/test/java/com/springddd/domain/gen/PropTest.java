package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.ColumnCommentNullException;
import com.springddd.domain.gen.exception.ColumnNameNullException;
import com.springddd.domain.gen.exception.ColumnTypeNullException;
import com.springddd.domain.gen.exception.JavaEntityNullException;
import com.springddd.domain.gen.exception.JavaTypeNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PropTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        Prop prop = new Prop("id", "ID", "bigint", "主键", "Long", "Long");
        assertThat(prop.propColumnKey()).isEqualTo("id");
        assertThat(prop.propColumnName()).isEqualTo("ID");
        assertThat(prop.propColumnType()).isEqualTo("bigint");
        assertThat(prop.propColumnComment()).isEqualTo("主键");
        assertThat(prop.propJavaType()).isEqualTo("Long");
        assertThat(prop.propJavaEntity()).isEqualTo("Long");
    }

    @Test
    @DisplayName("propColumnName 为 null 应抛 ColumnNameNullException")
    void constructor_withNullColumnName_shouldThrowException() {
        assertThatThrownBy(() -> new Prop("id", null, "bigint", "主键", "Long", "Long"))
                .isInstanceOf(ColumnNameNullException.class);
    }

    @Test
    @DisplayName("propColumnName 为空字符串应抛 ColumnNameNullException")
    void constructor_withEmptyColumnName_shouldThrowException() {
        assertThatThrownBy(() -> new Prop("id", "", "bigint", "主键", "Long", "Long"))
                .isInstanceOf(ColumnNameNullException.class);
    }

    @Test
    @DisplayName("propColumnType 为 null 应抛 ColumnTypeNullException")
    void constructor_withNullColumnType_shouldThrowException() {
        assertThatThrownBy(() -> new Prop("id", "ID", null, "主键", "Long", "Long"))
                .isInstanceOf(ColumnTypeNullException.class);
    }

    @Test
    @DisplayName("propColumnType 为空字符串应抛 ColumnTypeNullException")
    void constructor_withEmptyColumnType_shouldThrowException() {
        assertThatThrownBy(() -> new Prop("id", "ID", "", "主键", "Long", "Long"))
                .isInstanceOf(ColumnTypeNullException.class);
    }

    @Test
    @DisplayName("propColumnComment 为 null 应抛 ColumnCommentNullException")
    void constructor_withNullColumnComment_shouldThrowException() {
        assertThatThrownBy(() -> new Prop("id", "ID", "bigint", null, "Long", "Long"))
                .isInstanceOf(ColumnCommentNullException.class);
    }

    @Test
    @DisplayName("propColumnComment 为空字符串应抛 ColumnCommentNullException")
    void constructor_withEmptyColumnComment_shouldThrowException() {
        assertThatThrownBy(() -> new Prop("id", "ID", "bigint", "", "Long", "Long"))
                .isInstanceOf(ColumnCommentNullException.class);
    }

    @Test
    @DisplayName("propJavaType 为 null 应抛 JavaTypeNullException")
    void constructor_withNullJavaType_shouldThrowException() {
        assertThatThrownBy(() -> new Prop("id", "ID", "bigint", "主键", null, "Long"))
                .isInstanceOf(JavaTypeNullException.class);
    }

    @Test
    @DisplayName("propJavaType 为空字符串应抛 JavaTypeNullException")
    void constructor_withEmptyJavaType_shouldThrowException() {
        assertThatThrownBy(() -> new Prop("id", "ID", "bigint", "主键", "", "Long"))
                .isInstanceOf(JavaTypeNullException.class);
    }

    @Test
    @DisplayName("propJavaEntity 为 null 应抛 JavaEntityNullException")
    void constructor_withNullJavaEntity_shouldThrowException() {
        assertThatThrownBy(() -> new Prop("id", "ID", "bigint", "主键", "Long", null))
                .isInstanceOf(JavaEntityNullException.class);
    }

    @Test
    @DisplayName("propJavaEntity 为空字符串应抛 JavaEntityNullException")
    void constructor_withEmptyJavaEntity_shouldThrowException() {
        assertThatThrownBy(() -> new Prop("id", "ID", "bigint", "主键", "Long", ""))
                .isInstanceOf(JavaEntityNullException.class);
    }
}
