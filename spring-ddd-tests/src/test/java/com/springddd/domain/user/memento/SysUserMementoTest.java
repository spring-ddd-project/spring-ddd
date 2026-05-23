package com.springddd.domain.user.memento;

import com.springddd.domain.user.Account;
import com.springddd.domain.user.ExtendInfo;
import com.springddd.domain.user.Password;
import com.springddd.domain.user.SysUserDomain;
import com.springddd.domain.user.Username;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SysUserMementoTest {

    @Test
    @DisplayName("应从构造函数正确创建 Memento")
    void constructor_shouldCreateMementoWithCorrectValues() {
        Account account = new Account(new Username("testuser"), new Password("pass123"), "test@test.com", "13800138000", false);
        ExtendInfo extendInfo = new ExtendInfo("avatar.png", true);
        Long deptId = 1L;

        SysUserMemento memento = new SysUserMemento(account, extendInfo, deptId);

        assertThat(memento.getAccount()).isEqualTo(account);
        assertThat(memento.getExtendInfo()).isEqualTo(extendInfo);
        assertThat(memento.getDeptId()).isEqualTo(deptId);
    }

    @Test
    @DisplayName("应从聚合根正确创建 Memento")
    void saveToMemento_shouldCreateMementoFromDomain() {
        SysUserDomain domain = new SysUserDomain();
        domain.setAccount(new Account(new Username("admin"), new Password("admin123"), "admin@test.com", "13900139000", true));
        domain.setExtendInfo(new ExtendInfo("admin.png", false));
        domain.setDeptId(2L);

        SysUserMemento memento = domain.saveToMemento();

        assertThat(memento.getAccount().username().value()).isEqualTo("admin");
        assertThat(memento.getAccount().lockStatus()).isTrue();
        assertThat(memento.getExtendInfo().avatar()).isEqualTo("admin.png");
        assertThat(memento.getExtendInfo().sex()).isFalse();
        assertThat(memento.getDeptId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("应从 Memento 正确恢复聚合根")
    void restoreFromMemento_shouldRestoreDomainValues() {
        SysUserDomain domain = new SysUserDomain();
        SysUserMemento memento = new SysUserMemento(
                new Account(new Username("guest"), new Password("guest123"), "guest@test.com", "13700137000", false),
                new ExtendInfo("guest.png", true),
                3L
        );

        domain.restoreFromMemento(memento);

        assertThat(domain.getAccount().username().value()).isEqualTo("guest");
        assertThat(domain.getAccount().email()).isEqualTo("guest@test.com");
        assertThat(domain.getExtendInfo().avatar()).isEqualTo("guest.png");
        assertThat(domain.getExtendInfo().sex()).isTrue();
        assertThat(domain.getDeptId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("Memento 字段值应通过 getter 正确返回")
    void getters_shouldReturnCorrectFieldValues() {
        Account account = new Account(new Username("operator"), new Password("op123"), "op@test.com", "13600136000", false);
        ExtendInfo extendInfo = new ExtendInfo("op.png", true);
        Long deptId = 5L;

        SysUserMemento memento = new SysUserMemento(account, extendInfo, deptId);

        assertThat(memento.getAccount().username().value()).isEqualTo("operator");
        assertThat(memento.getAccount().phone()).isEqualTo("13600136000");
        assertThat(memento.getExtendInfo().sex()).isTrue();
        assertThat(memento.getDeptId()).isEqualTo(5L);
    }
}
