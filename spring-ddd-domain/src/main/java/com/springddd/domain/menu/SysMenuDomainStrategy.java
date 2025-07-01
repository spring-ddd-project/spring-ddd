package com.springddd.domain.menu;

public interface SysMenuDomainStrategy {

    boolean check(Integer type);

    SysMenuDomain handle(MenuBasicInfo menuBasicInfo, MenuExtendInfo menuExtendInfo);
}
