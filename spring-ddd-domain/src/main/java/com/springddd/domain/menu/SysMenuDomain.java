package com.springddd.domain.menu;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysMenuDomain extends AbstractDomainMask {

    private MenuId menuId;

    private MenuId parentId;

    private MenuBasicInfo menuBasicInfo;

    private MenuExtendInfo menuExtendInfo;

    public void create() {}

    public void update(MenuId parentId,
                       MenuBasicInfo menuBasicInfo,
                       MenuExtendInfo menuExtendInfo, Long deptId, String updateBy) {
        this.parentId = parentId;
        this.menuBasicInfo = menuBasicInfo;
        this.menuExtendInfo = menuExtendInfo;
        super.setDeptId(deptId);
        super.setUpdateBy(updateBy);
        super.setUpdateTime(LocalDateTime.now());
    }

    public void delete(String updateBy) {
        super.setDeleteStatus("1");
        super.setUpdateBy(updateBy);
        super.setUpdateTime(LocalDateTime.now());
    }
}
