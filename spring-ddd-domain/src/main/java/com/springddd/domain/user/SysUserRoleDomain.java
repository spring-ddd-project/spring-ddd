package com.springddd.domain.user;

import com.springddd.domain.AbstractDomainMask;
import com.springddd.domain.role.RoleId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserRoleDomain extends AbstractDomainMask {

    private UserRoleId userRoleId;

    private UserId userId;

    private RoleId roleId;

    public void create() {}

    public void update(UserId userId, RoleId roleId, String updateBy) {
        this.userId = userId;
        this.roleId = roleId;
        super.setUpdateBy(updateBy);
        super.setUpdateTime(LocalDateTime.now());
    }

    public void delete(String updateBy) {
        super.setDeleteStatus("1");
    }
}
