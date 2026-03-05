package com.springddd.domain.menu;

public interface SysMenuDomainStrategy {

    boolean check(Integer type);

    SysMenuDomain handle(Catalog catalog, Menu menu, Button button, MenuExtendInfo menuExtendInfo);
}
