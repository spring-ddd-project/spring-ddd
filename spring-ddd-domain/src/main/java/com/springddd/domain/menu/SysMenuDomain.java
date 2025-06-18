package com.springddd.domain.menu;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysMenuDomain extends AbstractDomainMask {

    private MenuId menuId;

    private MenuId parentId;

    private MenuBasicInfo menuBasicInfo;

    private MenuExtendInfo menuExtendInfo;
}
